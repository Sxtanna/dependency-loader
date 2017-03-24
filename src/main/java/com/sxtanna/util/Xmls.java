package com.sxtanna.util;

import com.sxtanna.DLoader;
import com.sxtanna.base.Dependency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Handy Dandy Utility class for reading POM Files
 *
 * <p>Can be expanded to generically read XML Files</p>
 * <p>A la, {@link Xmls#readTag(Element, String)}</p>
 *
 */
public final class Xmls {

	private static final String
			TAG_SCOPE = "scope",
			TAG_GROUP = "groupId",
			TAG_VERSION = "version",
			TAG_OPTIONAL = "optional",
			TAG_ARTIFACT = "artifactId",
			TAG_DEPENDENCY = "dependency";

	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


	/**
	 * Prevent Instantiation
	 */
	private Xmls() {}


	/**
	 * Read all necessary Dependencies from a POM file and return them as {@link Dependency} instances
	 *
	 * @param pomFile The POM file
	 * @return The List of Dependencies or an Empty list if none
	 */
	public static List<Dependency> readDependencies(File pomFile) {
		final List<Dependency> dependencies = new ArrayList<>();

		try {
			final DocumentBuilder builder  = factory.newDocumentBuilder();
			final Document        document = builder.parse(pomFile);

			document.normalize();

			NodeList pomDependencies = document.getElementsByTagName(TAG_DEPENDENCY);
			if (pomDependencies == null) return Collections.emptyList();

			DLoader.debug("Found " + pomDependencies.getLength() + " Dependencies");

			for (int i = 0; i < pomDependencies.getLength(); i++) {
				Element dependency = ((Element) pomDependencies.item(i));

				final String groupId = readTag(dependency, TAG_GROUP);
				final String artifactId = readTag(dependency, TAG_ARTIFACT);
				final String scope = readTag(dependency, TAG_SCOPE);

				if (!scope.equals("provided") && !scope.isEmpty()) {
					DLoader.debug("Not going to load " + groupId + ":" + artifactId + ", its scope is included in the jar as '" + scope + "'");
					continue;
				}

				String version = readTag(dependency, TAG_VERSION);

				if (version.startsWith("${")) {
					String propertyName = version.substring(2, version.length() - 1);
					version = readTag(document.getDocumentElement(), propertyName);
				}

				final String optional = readTag(dependency, TAG_OPTIONAL);
				if (!optional.isEmpty() && optional.equalsIgnoreCase("true")) continue;

				DLoader.debug("Version " + version);
				DLoader.debug("GroupId " + groupId);
				DLoader.debug("ArtifactId " + artifactId);

				dependencies.add(new Dependency(groupId + ':' + artifactId + ':' + version, version, groupId, artifactId));
			}

		} catch (Exception e) {
			DLoader.log(Level.SEVERE, "Failed to load dependencies for pom " + pomFile.getName());
			e.printStackTrace();

			return Collections.emptyList();
		}

		return dependencies;
	}

	private static String readTag(Element element, String tagName) {
		Node item = element.getElementsByTagName(tagName).item(0);
		if (item == null) return "";

		return item.getTextContent();
	}

}
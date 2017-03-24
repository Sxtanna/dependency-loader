package com.sxtanna.util;

import com.sxtanna.DLoader;
import com.sxtanna.base.Dependency;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.function.BiConsumer;
import java.util.logging.Level;

/**
 * URL Utility class, to make my life easier
 * <p> </p>
 * <p>Mostly for attempting to pull Dependencies from Central</p>
 * <p>Also contains methods for getting the URL to either the Dependency's Jar or POM File</p>
 */
@SuppressWarnings("WeakerAccess")
public final class Urls {

	/**
	 * Maven Central URL
	 */
	public static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2/";


	/**
	 * Prevent Instantiation
	 */
	private Urls() {}


	/**
	 * The Base URL of this Dependency
	 * <p>This URL denotes the path to all pertinent resources of this Dependency</p>
	 *
	 * @param dependency The Dependency
	 * @return The URL as a String
	 */
	public static String getBaseUrl(Dependency dependency) {
		return MAVEN_CENTRAL +
				dependency.getGroupId().replace('.', '/') + '/' +
				dependency.getArtifactId() + '/' + dependency.getVersion() + '/';
	}

	/**
	 * Get the URL Pointing to this Dependency's Jar file in Central
	 *
	 * @param dependency The Dependency
	 * @return The URL pointing to its Jar
	 */
	public static String getJarUrl(Dependency dependency) {
		return getBaseUrl(dependency) + dependency.getJarName();
	}

	/**
	 * Get the URL Pointing to this Dependency's POM file in Central
	 *
	 * @param dependency The Dependency
	 * @return The URL pointing to its POM
	 */
	public static String getPomUrl(Dependency dependency) {
		return getBaseUrl(dependency) + dependency.getPomName();
	}


	/**
	 *
	 * Attempt to download a Dependency from Central
	 *
	 * <p>This will attempt to download its Jar and POM File</p>
	 *
	 * @param dependency The Dependency to be downloaded
	 * @param folder The Folder where the files will be saved
	 * @param whenDone Operation to be ran when they are downloaded, first File is the Jar, second is the POM
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void download(Dependency dependency, File folder, BiConsumer<File, File> whenDone) {
		final File jarFile = new File(folder, dependency.getJarName());
		final File pomFile = new File(folder, dependency.getPomName());

		if (jarFile.exists()) {
			whenDone.accept(jarFile, pomFile);
			return;
		}

		if (!folder.exists()) folder.mkdirs();

		try {
			URL pomUrl = new URL(getPomUrl(dependency));
			URL jarUrl = new URL(getJarUrl(dependency));

			pullFromUrlToFile(pomUrl, pomFile);
			pullFromUrlToFile(jarUrl, jarFile);

			whenDone.accept(jarFile, pomFile);
		} catch (Exception e) {
			DLoader.log(Level.SEVERE, "Failed to download dependency " + dependency.getName());
			e.printStackTrace();
		}
	}


	/**
	 * <b>VERY IMPORTANT METHOD</b>
	 *
	 * <p>This is basically the core of this entire damn thing, believe it or not..</p>
	 * <p>This will download the file this URL points to</p>
	 *
	 * @param url The URL pointing to a file
	 * @param file The local file it will be saved to
	 */
	private static void pullFromUrlToFile(URL url, File file) {
		try(InputStream stream = url.openStream()) {
			Files.copy(stream, file.toPath());
		}
		catch (Exception e) {
			DLoader.log(Level.SEVERE, "Failed to download url " + url.getFile() + " to file " + file.getName());
			e.printStackTrace();
		}
	}

}
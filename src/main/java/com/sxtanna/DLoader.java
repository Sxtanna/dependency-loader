package com.sxtanna;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sxtanna.base.Dependency;
import com.sxtanna.util.Urls;
import com.sxtanna.util.Xmls;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dependency Loader Main Class
 * <p>
 * Handles loading Dependencies from the Config and other Plugins
 */
@SuppressWarnings("WeakerAccess")
public final class DLoader extends JavaPlugin {

	private static DLoader instance;


	private static Method method;
	private static URLClassLoader classLoader = ((URLClassLoader) ClassLoader.getSystemClassLoader());

	private static boolean working = true, showDebug = false, enforceFileCheck = true;

	static {
		try {
			method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			log(Level.SEVERE, "Failed to initialize URLClassLoader, Dependencies will not be loaded!");
			e.printStackTrace();

			working = false;
		}
	}


	private File dependencyFolder;
	private final Map<String, Dependency> dependencies = Maps.newHashMap();


	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public void onLoad() {
		instance = this;
		if (!working) return;

		dependencyFolder = new File(getDataFolder(), "Dependencies");
		if (!dependencyFolder.exists()) dependencyFolder.mkdirs();

		saveDefaultConfig();

		FileConfiguration config = getConfig();
		showDebug = config.getBoolean("options.showDebug", false);
		enforceFileCheck = config.getBoolean("options.showDebug", true);

		Urls.REPOSITORIES.addAll(config.getStringList("options.repositories"));
		Urls.REPOSITORIES.replaceAll(url -> url.endsWith("/") ? url : url + '/');

		ConfigurationSection configDependencies = config.getConfigurationSection("dependencies");
		Set<String>          keys               = configDependencies == null ? Collections.emptySet() : configDependencies.getKeys(false);

		PluginDescriptionFile pluginDesc = getDescription();

		log(Level.INFO,
				" ", " ",
				blockBar(45),
				"<  ",
				"< Dependency Loader " + pluginDesc.getVersion() + " by - " + pluginDesc.getAuthors(),
				"<  ",
				"< Showing Debug Messages? -> " + showDebug,
				"< Enforcing File Check? -> " + enforceFileCheck,
				"< Dependencies In Config -> " + keys.size(),
				"<  ",
				blockBar(45),
				" ", " ");


		keys.forEach(name -> {

			String  groupId      = configDependencies.getString(name + ".group", "");
			String  version      = configDependencies.getString(name + ".version", "");
			String  artifactId   = configDependencies.getString(name + ".artifact", "");
			String  customRepo   = configDependencies.getString(name + ".repository");
			boolean alwaysUpdate = configDependencies.getBoolean(name + ".always-update", false);

			if (version.isEmpty() || groupId.isEmpty() || artifactId.isEmpty()) {
				log(Level.SEVERE,
						" ", " ",
						blockBar(45),
						"< ",
						"< Dependency " + name + " has incomplete details",
						"< Requires, case-sensitive",
						"< 'version', 'group', 'artifact'",
						"< ",
						blockBar(45),
						" ", " ");
			} else {
				final Dependency dependency = new Dependency(name.toLowerCase(), version, groupId, artifactId, customRepo, alwaysUpdate);
				if (dependencies.containsValue(dependency)) debug("Dependency " + name + " has a duplicate");

				load(dependency);
				debug("Loaded Dependency " + name + " From Config");
			}
		});

	}


	/**
	 * Get the current instance of {@link DLoader}
	 *
	 * @return The Instance
	 */
	public static DLoader getInstance() {
		return instance;
	}


	public static boolean isShowingDebug() {
		return showDebug;
	}

	public static boolean isEnforcingFileCheck() {
		return enforceFileCheck;
	}

	/**
	 * Load a {@link Dependency} onto the Classpath
	 * <p>
	 * <p>Either called by DLoader to load from Config</p>
	 * <p>Or by a Plugin</p>
	 *
	 * @param dependency The Dependency to be loaded
	 * @see DLoader#load(Dependency, Runnable)
	 */
	public void load(Dependency dependency) {
		load(dependency, () -> {
		});
	}

	/**
	 * Load a {@link Dependency} onto the Classpath, and then execute a block of code
	 * <p>
	 * <p>Runnable runs after the dependency and all child dependencies are loaded</p>
	 *
	 * @param dependency The Dependency to be loaded
	 * @param whenDone   Block of code ran after everything is loaded
	 * @see DLoader#load(Dependency)
	 */
	public void load(Dependency dependency, Runnable whenDone) {
		debug(" ", " ", blockBar(60), " ", blockArrow(dependency, "v"));
		Urls.download(dependency, new File(dependencyFolder, dependency.getGroupId()), (jar, pom) -> {
			loadJar(dependency, jar);
			loadChildren(dependency, pom, whenDone);
		});
	}

	/**
	 * Retrieve a Dependency ID by name
	 *
	 * @param name The name of the Dependency
	 * @return An Optional containing either the Dependency, or null if not loaded
	 */
	public Optional<Dependency> get(String name) {
		return Optional.ofNullable(dependencies.get(name.toLowerCase()));
	}


	private void loadChildren(Dependency dependency, File pomFile, Runnable whenDone) {
		debug("Loading child dependencies of " + dependency.getName());
		List<Dependency> children = Xmls.readDependencies(pomFile);
		if (children.isEmpty()) {
			debug("No children found in " + dependency.getName(), blockArrow(dependency, "^"), blockBar(60), " ", " ");
			whenDone.run();
			return;
		}

		final int[] loaded = {0};

		children.forEach(child -> {
			child.setParent(dependency);

			load(child, () -> {
				if (++loaded[0] == children.size()) {
					debug("Finished loading children from " + dependency.getName(), blockArrow(dependency, "^"), " ", blockBar(60), " ", " ");
					whenDone.run();
				}
			});
		});
	}

	private void loadJar(Dependency dependency, File jarFile) {
		try {
			method.invoke(classLoader, jarFile.toURI().toURL());
			debug("Added " + jarFile.getName() + " to ClassLoader");
			dependencies.put(dependency.getName().toLowerCase(), dependency);
		} catch (Exception e) {
			log(Level.SEVERE, "Failed to load Jar File " + jarFile.getName());
			e.printStackTrace();
		}
	}

	private String blockArrow(Dependency dependency, String c) {
		final String generated = Strings.repeat(c, dependency.getParentDepth());
		return generated.isEmpty() ? c + c + c : generated;
	}

	private String blockBar(int length) {
		return Strings.repeat("=", length);
	}


	public static void debug(String... message) {
		if (isShowingDebug()) log(Level.WARNING, message);
	}

	public static void log(Level level, String... message) {
		Logger logger = getInstance().getLogger();
		for (String msgLine : message) logger.log(level, msgLine);
	}

}

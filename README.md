# DependencyLoader [![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0) [![GitHub release](https://img.shields.io/github/release/Sxtanna/dependency-loader.svg?style=flat-square)]()
Simple Maven Dependency Downloader for Spigot Plugins

### Dependency Loading
  * Specifically Entered in the Config
  * Loaded at runtime by other plugins
  
## In Config  
```yml
dependencies:
  kotlin-stdlib:
    version: 1.1.1
    groupId: org.jetbrains.kotlin
    artifactId: kotlin-stdlib
```

## By a Plugin
```java
public final class ExampleDepend extends JavaPlugin {

	// How Plugins will store Dependency IDs
	private static final Dependency HIKARI_CP = new Dependency("HikariCP", "2.6.1", "com.zaxxer", "HikariCP");

	/**
	 *
	 * The Dependency will most likely not exist when this instance is created, so the main class should never reference it
	 * Instead create other classes to handle it, ie. Database
	 *
	 */


	@Override
	public void onLoad() {
		// Plugins can either call load from onLoad and then have normal usage in the onEnable
		DLoader.getInstance().load(HIKARI_CP);
	}

	@Override
	public void onEnable() {
		// "Normal Usage"
		new Database();

		// Or Plugins could use this sort of logic in the onEnable alone, the instance of Database will be created after the dependency is loaded
		DLoader.getInstance().load(HIKARI_CP, Database::new);
	}

}
```

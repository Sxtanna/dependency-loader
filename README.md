# DependencyLoader [![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0) [![Current Release](https://img.shields.io/github/release/Sxtanna/dependency-loader.svg?style=flat-square)](https://github.com/Sxtanna/dependency-loader/releases/tag/1.0) [![Commits since release](https://img.shields.io/github/commits-since/Sxtanna/dependency-loader/1.0.svg?style=flat-square)](https://github.com/Sxtanna/dependency-loader/commits/master)
Simple Maven Dependency Downloader for Spigot Plugins

### Dependency Loading
  * Specifically Entered in the Config
  * Loaded at runtime by other plugins
  
## In Config  
```yml
dependencies:
  kotlin-runtime:
    version: 1.1.1
    group: org.jetbrains.kotlin
    artifact: kotlin-runtime
    always-update: true
  kotlin-eap:
    version: 1.1.1-eap-26
    group: org.jetbrains.kotlin
    artifact: kotlin-stdlib-jre8
    repository: https://dl.bintray.com/kotlin/kotlin-eap-1.1/
```
*Tags, 'always-update' and 'repository' are optional and explained in the default config*

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

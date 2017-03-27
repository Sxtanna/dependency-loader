# DependencyLoader [![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0) [![Current Release](https://img.shields.io/github/release/Sxtanna/dependency-loader.svg?style=flat-square)](https://github.com/Sxtanna/dependency-loader/releases/latest) [![Commits since release](https://img.shields.io/github/commits-since/Sxtanna/dependency-loader/v1.1.svg?style=flat-square)](https://github.com/Sxtanna/dependency-loader/commits/master) [![Maven Central](https://img.shields.io/maven-central/v/com.sxtanna/DependencyLoader.svg?style=flat-square)](http://repo1.maven.org/maven2/com/sxtanna/DependencyLoader/1.1/)
Simple Maven Dependency Downloader for Spigot Plugins

## How to get it!

### Maven
```xml
<dependency>
    <groupId>com.sxtanna</groupId>
    <artifactId>DependencyLoader</artifactId>
    <version>LATEST</version>
</dependency>
```


### Dependency Loading
  * Specifically Entered in the Config
  * Loaded at runtime by other plugins
  
#### In Config  
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

#### By a Plugin
```java
public final class ExampleDepend extends JavaPlugin {

	// How Plugins will store Dependency IDs
	private static final Dependency KOTLIN_STDLIB = new Dependency("Kotlin-EAP", "1.1.1-eap-26", "org.jetbrains.kotlin", "kotlin-stdlib-jre8");

	static  {
		KOTLIN_STDLIB.getOptions().setCustomRepository("https://dl.bintray.com/kotlin/kotlin-eap-1.1/");
	}

	/**
	 *
	 * The Dependency will most likely not exist when this instance is created, so the main class should never reference it
	 * Instead create other classes to handle it, ie. KotlinClassThing
	 *
	 */


	@Override
	public void onLoad() {
		// Plugins can either call load from onLoad and then have normal usage in the onEnable
		DLoader.getInstance().load(KOTLIN_STDLIB);
	}

	@Override
	public void onEnable() {
		// "Normal Usage", at this point the Dependency will be loaded and available
		new KotlinClassThing();

		// Or Plugins could use this sort of logic in the onEnable alone, the instance of Database will be created after the dependency is loaded
		DLoader.getInstance().load(KOTLIN_STDLIB, KotlinClassThing::new);
	}

}
```

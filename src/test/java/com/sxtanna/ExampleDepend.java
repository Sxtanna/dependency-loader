package com.sxtanna;

import com.sxtanna.base.Dependency;
import org.bukkit.plugin.java.JavaPlugin;

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

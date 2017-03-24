package com.sxtanna;

import com.sxtanna.base.Dependency;
import com.sxtanna.db.Database;
import org.bukkit.plugin.java.JavaPlugin;

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

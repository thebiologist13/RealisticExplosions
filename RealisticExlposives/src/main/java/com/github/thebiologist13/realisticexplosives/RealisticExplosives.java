package com.github.thebiologist13.realisticexplosives;

import com.github.thebiologist13.pluginutils.BetterPlugin;

public class RealisticExplosives extends BetterPlugin {

	@Override
	public void onDisable() {
		getBetterLogger().info("RealisticExplosions v" + getDescription().getVersion() + " has been disabled!");
	}
	
	@Override
	public void onEnable() {
		
		getBetterConfig().getConfig(); //Loads config
		getBetterLogger().setDebugMode(true); //TODO Turn off when ready
		
		getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
		
		getBetterLogger().info("RealisticExplosions v" + getDescription().getVersion() + " has been enabled!");
	}

}

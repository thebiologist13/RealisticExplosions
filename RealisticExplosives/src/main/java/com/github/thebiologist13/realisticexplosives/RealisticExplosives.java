package com.github.thebiologist13.realisticexplosives;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RealisticExplosives extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public FileConfiguration config;
	
	private File configFile;

	@Override
	public void onDisable() {
		log.info("RealisticExplosions v" + getDescription().getVersion() + " has been disabled!");
	}

	@Override
	public void onEnable() {

		config = getConfig(); //Loads config

		getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);

		log.info("RealisticExplosions v" + getDescription().getVersion() + " has been enabled!");
	}

	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

	public void reloadConfig() {

		if (configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");

			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				copy(getResource("config.yml"), configFile);
			}

		}

		config = YamlConfiguration.loadConfiguration(configFile);

		// Look for defaults in the jar
		InputStream defConfigStream = getResource("config.yml");
		if (defConfigStream != null) {
			FileConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.options().copyDefaults(true);
			config.setDefaults(defConfig);
		}

	}

	private void copy(InputStream in, File file) {

		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			log.severe("Could not copy config from jar!");
			e.printStackTrace();
		}

	}

}

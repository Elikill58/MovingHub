package com.elikill58.movinghub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MovingHub extends Plugin {

	private static ServerGroup ignored, redirect;
	
	private static MovingHub instance;
	public static MovingHub getInstance() {
		return instance;
	}

	private static Configuration config;
	public static Configuration getConfig() {
		return config;
	}
	
	public static ServerGroup getIgnored() {
		return ignored;
	}
	public static ServerGroup getRedirect() {
		return redirect;
	}
	
    @Override
    public void onEnable() {
    	instance = this;
    	
    	loadConfig();
    	
    	getProxy().getPluginManager().registerListener(this, new Listeners());
    }

	private void loadConfig() {
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadFile("config.yml"));
			ignored = new ServerGroup(config.getSection("groups.ignored"));
			redirect = new ServerGroup(config.getSection("groups.redirect"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File loadFile(String resource) {
		File folder = getInstance().getDataFolder();
		folder.mkdir();

		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = getInstance().getResourceAsStream(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}
	
	public static void debug(String msg) {
		if(config.getBoolean("debug", false)) {
			getInstance().getLogger().info(msg);
		}
	}
}

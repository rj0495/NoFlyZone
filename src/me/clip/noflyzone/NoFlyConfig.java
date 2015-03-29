package me.clip.noflyzone;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

public class NoFlyConfig {

	private NoFlyZone plugin;
	
	protected NoFlyConfig(NoFlyZone i) {
		plugin = i;
	}
	
	public void loadDefaultConfiguration() {
		FileConfiguration config = plugin.getConfig();
		config.options().header("NoFlyZone version: "+plugin.getDescription().getVersion()+" Main Configuration"+
				"\n" +
				"\nList regions below that will disable players from flying when they enter them" +
				"\n");
		config.addDefault("no_fly_worlds", Arrays.asList(new String[] {
				"world_nether", "world_the_end"
		}));
		
		if (!config.isConfigurationSection("no_fly_regions")) {
			config.addDefault("no_fly_regions.world", Arrays.asList(new String[] {
				"testflyregion"
			}));
		}
		if (!config.isConfigurationSection("no_fly_regions")) {
			config.addDefault("auto_fly_regions.world", Arrays.asList(new String[] {
					"autoflyregion"
			}));
		}
		
		config.addDefault("no_fly_message", "&cNo flying here!");
		config.addDefault("auto_fly_message", "&aYour flight has been toggled on!");
		config.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	protected int loadNoFlyWorlds() {
		
		FileConfiguration c = plugin.getConfig();
		
		if (!c.contains("no_fly_worlds") || !c.isList("no_fly_worlds")) {
			return 0;
		}
		
		NoFlyZone.noFlyWorlds = c.getStringList("no_fly_worlds");
		
			
		return NoFlyZone.noFlyWorlds.size();
	}
	
	protected int loadNoFlyRegions() {
		
		FileConfiguration c = plugin.getConfig();
		
		NoFlyZone.noFlyRegions = new HashMap<String, List<String>>();
		
		if (!c.contains("no_fly_regions") || !c.isConfigurationSection("no_fly_regions")) {
			return 0;
		}
		
		Set<String> keys = c.getConfigurationSection("no_fly_regions").getKeys(false);
		
		if (keys == null || keys.isEmpty()) {
			return 0;
		}
		
		int loaded = 0;
			
		for (String key : keys) {
			
			if (!c.isList("no_fly_regions."+key)) {
				continue;
			}
			
			List<String> worldRegions = c.getStringList("no_fly_regions."+key);
			
			if (worldRegions == null || worldRegions.isEmpty()) {
				continue;
			}
			
			loaded = loaded+worldRegions.size();
			
			NoFlyZone.noFlyRegions.put(key, worldRegions);	
		}
			
		return loaded;
	}
	
	protected int loadFlyRegions() {
		
		FileConfiguration c = plugin.getConfig();
		
		NoFlyZone.flyRegions = new HashMap<String, List<String>>();
		
		if (!c.contains("auto_fly_regions") || !c.isConfigurationSection("auto_fly_regions")) {
			return 0;
		}
		
		Set<String> keys = c.getConfigurationSection("auto_fly_regions").getKeys(false);
		
		if (keys == null || keys.isEmpty()) {
			return 0;
		}
		
		int loaded = 0;
			
		for (String key : keys) {
			
			if (!c.isList("auto_fly_regions."+key)) {
				continue;
			}
			
			List<String> worldRegions = c.getStringList("auto_fly_regions."+key);
			
			if (worldRegions == null || worldRegions.isEmpty()) {
				continue;
			}
			
			loaded = loaded+worldRegions.size();
			
			NoFlyZone.flyRegions.put(key, worldRegions);	
		}
			
		return loaded;
	}
	
	protected String noFlyMessage() {
		return plugin.getConfig().getString("no_fly_message");
	}
	
	protected String autoFlyMessage() {
		return plugin.getConfig().getString("auto_fly_message");
	}
}

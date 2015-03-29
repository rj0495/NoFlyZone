package me.clip.noflyzone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoFlyCommands implements CommandExecutor {
	
	NoFlyZone plugin;
	
	public NoFlyCommands(NoFlyZone i) {
		plugin = i;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String label,
			String[] args) {

		if (s instanceof Player) {
			Player p = (Player) s;
			
			if (!p.hasPermission("noflyzone.admin")) {
				plugin.sms(p, "&cYou don't have permission to do that!");
				return true;
			}
		}
		
		if (args.length == 0) {
			
			plugin.sms(s, "&8&m+----------------+");
			plugin.sms(s, "&cNo&fFly&cZone &f&o"+plugin.getDescription().getVersion());
			plugin.sms(s, "&7Created by: &f&oextended_clip");
			plugin.sms(s, "&8&m+----------------+");
			
		} else if (args.length != 0 && args[0].equalsIgnoreCase("help")) {
			
			plugin.sms(s, "&8&m+----------------+");
			plugin.sms(s, "&cNo&fFly&cZone &f&oHelp");
			plugin.sms(s, "&7/noflyzone - &fshow plugin version");
			plugin.sms(s, "&7/noflyzone reload - &freload config");
			plugin.sms(s, "&7/noflyzone list (world) - &flist all/world no fly regions");
			plugin.sms(s, "&7/noflyzone add <world> <region> - &fadd a no fly region");
			plugin.sms(s, "&7/noflyzone remove <world> <region> - &fremove a no fly region");
			plugin.sms(s, "&8&m+----------------+");
			return true;
			
		} else if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
			
			plugin.reloadConfig();
			plugin.saveConfig();
			NoFlyZone.noFlyMessage = plugin.cfg.noFlyMessage();
			NoFlyZone.autoFlyMessage = plugin.cfg.autoFlyMessage();
			plugin.cfg.loadNoFlyRegions();
			plugin.cfg.loadNoFlyWorlds();
			plugin.cfg.loadFlyRegions();
			plugin.sms(s, "&8&m+----------------+");
			plugin.sms(s, "&7Configuration successfully reloaded!");
			plugin.sms(s, "&8&m+----------------+");
			return true;
			
		} else if (args.length != 0 && args[0].equalsIgnoreCase("list")) {
			
			if (NoFlyZone.noFlyRegions == null || NoFlyZone.noFlyRegions.isEmpty()) {
					
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cNo regions loaded!");
				plugin.sms(s, "&8&m+----------------+");
				return true;
			}
			
			if (args.length == 1) {
			
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cNo&fFly&cZone &7worlds: &f"+NoFlyZone.noFlyWorlds.size());
				for (String world : NoFlyZone.noFlyRegions.keySet()) {
					
					List<String> regions = NoFlyZone.noFlyRegions.get(world);
					
					if (regions == null || regions.isEmpty()) {
						plugin.sms(s, "&a"+world+"&7: &cNo Regions loaded");
						continue;
					}
					
					plugin.sms(s, "&a"+world+"&7: &f"+regions.toString().replace("[", "").replace("]", "").replace(",", "&7,&f"));
					
				}
				
				plugin.sms(s, "&8&m+----------------+");
				return true;
				
			} else {
				
				String world = args[1];
				
				if (!NoFlyZone.noFlyRegions.containsKey(world) || NoFlyZone.noFlyRegions.get(world) == null || NoFlyZone.noFlyRegions.get(world).isEmpty()) {
					
					plugin.sms(s, "&8&m+----------------+");
					plugin.sms(s, "&cNo regions loaded for world&7: &f"+world);
					plugin.sms(s, "&8&m+----------------+");
					return true;
				}
				
				List<String> regions = NoFlyZone.noFlyRegions.get(world);
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&a"+world+" &cNo&fFly&cZone &7regions: &f"+regions.size());
				plugin.sms(s, regions.toString().replace("[", "").replace("]", "").replace(",", "&7,&f"));
				plugin.sms(s, "&8&m+----------------+");
				
				return true;
			}
			
		}  else if (args.length != 0 && args[0].equalsIgnoreCase("add")) {
			
			if (args.length != 3) {
				
				plugin.sms(s, "&cIncorrect usage! &7/nfz add <world> <region>");
				return true;
			}
			
			String world = args[1];
			
			String region = args[2];
			
			if (NoFlyZone.noFlyRegions == null || NoFlyZone.noFlyRegions.isEmpty()) {
				
				NoFlyZone.noFlyRegions = new HashMap<String, List<String>>();
				
				NoFlyZone.noFlyRegions.put(world, Arrays.asList(new String[] { region }));
				
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, region+" &ain world &f"+world+" &ahas been marked as a &cNo&fFly&cZone&a!");
				plugin.sms(s, "&8&m+----------------+");
				plugin.getConfig().set("no_fly_regions."+world, NoFlyZone.noFlyRegions.get(world));
				plugin.saveConfig();
				return true;
			}
						
			if (!NoFlyZone.noFlyRegions.containsKey(world) || NoFlyZone.noFlyRegions.get(world) == null) {
				
				NoFlyZone.noFlyRegions.put(world, Arrays.asList(new String[] { region }));
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, region+" &ain world &f"+world+" &ahas been marked as a &cNo&fFly&cZone&a!");
				plugin.sms(s, "&8&m+----------------+");
				plugin.getConfig().set("no_fly_regions."+world, NoFlyZone.noFlyRegions.get(world));
				plugin.saveConfig();
				return true;
			}
			
			List<String> regz = new ArrayList<String>();
			
			boolean contains = false;
			
			for (String inList : NoFlyZone.noFlyRegions.get(world)) {
				
				if (inList.equalsIgnoreCase(region)) {
					contains = true;
					break;
				} else {
					regz.add(inList);
				}
			}
			
			if (contains) {
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cRegion &f"+region+" &cin world &f"+world+" &cis already a &cNo&fFly&cZone&a!");
				plugin.sms(s, "&8&m+----------------+");
				return true;
			}

			regz.add(region);
			
			NoFlyZone.noFlyRegions.put(world, regz);
			plugin.getConfig().set("no_fly_regions."+world, regz);
			plugin.saveConfig();
			plugin.sms(s, "&8&m+----------------+");
			plugin.sms(s, region+" &ain world &f"+world+" &ahas been marked as a &cNo&fFly&cZone&a!");
			plugin.sms(s, "&8&m+----------------+");
			return true;
			
		}  else if (args.length != 0 && args[0].equalsIgnoreCase("remove")) {
			
			if (args.length != 3) {
				plugin.sms(s, "&cIncorrect usage! &7/nfz remove <world> <region>");
				return true;
			}
			
			if (NoFlyZone.noFlyRegions == null || NoFlyZone.noFlyRegions.isEmpty()) {
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cNo regions loaded to remove!");
				plugin.sms(s, "&8&m+----------------+");
				return true;
			}
			
			String world = args[1];
			
			if (!NoFlyZone.noFlyRegions.containsKey(world) || NoFlyZone.noFlyRegions.get(world) == null || NoFlyZone.noFlyRegions.get(world).isEmpty()) {
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cNo regions loaded for world &f"+world+" &cto remove!");
				plugin.sms(s, "&8&m+----------------+");
				return true;
			}
			
			List<String> temp = new ArrayList<String>();
			
			String region = args[2];
			
			boolean contains = false;
			
			for (String inList : NoFlyZone.noFlyRegions.get(world)) {
				
				if (inList.equalsIgnoreCase(region)) {
					contains = true;
					continue;
				} else {
					temp.add(inList);
				}
			}
			
			if (!contains) {
				plugin.sms(s, "&8&m+----------------+");
				plugin.sms(s, "&cNo region marked as a NoFlyZone in world &f"+world+" &cwith the name &f"+region+"&a!");
				plugin.sms(s, "&8&m+----------------+");
				return true;
			}
			
			
			if (temp.isEmpty()) {
				
				NoFlyZone.noFlyRegions.remove(world);
				plugin.getConfig().set("no_fly_regions."+world, null);
			} else {
				NoFlyZone.noFlyRegions.put(world, temp);
				plugin.getConfig().set("no_fly_regions."+world, temp);
			}
			
			plugin.saveConfig();
			plugin.sms(s, "&8&m+----------------+");
			plugin.sms(s, region+" &ain world &f"+world+" &ais no longer a &cNo&fFly&cZone&a!");
			plugin.sms(s, "&8&m+----------------+");
			return true;
			
		} else {
			
			plugin.sms(s, "&cIncorrect usage! Use /noflyzone help!");
		}
		return true;
	}
	


}

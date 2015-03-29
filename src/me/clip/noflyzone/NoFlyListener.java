package me.clip.noflyzone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoFlyListener implements Listener {
	
	NoFlyZone plugin;
	
	public NoFlyListener(NoFlyZone i) {
		plugin = i;
		Bukkit.getPluginManager().registerEvents(this, i);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if ((e.getFrom().getBlockX() != e.getTo().getBlockX())
			|| (e.getFrom().getBlockY() != e.getTo().getY())
			|| (e.getFrom().getBlockZ() != e.getTo().getBlockZ())) {
			
			Player p = e.getPlayer();
			
			String world = e.getTo().getWorld().getName();
				
			if (p.getAllowFlight()) {

				if (NoFlyZone.noFlyWorlds != null
						&& !NoFlyZone.noFlyWorlds.isEmpty()
						&& NoFlyZone.noFlyWorlds.contains(world)) {

					if (p.hasPermission("noflyzone.bypass")) {
						return;
					}
					
					p.setAllowFlight(false);
					p.setFlying(false);

					double oldY = e.getFrom().getY();

					Location movingTo = e.getTo().clone();

					movingTo.setY(oldY);

					e.setTo(movingTo);

					if (NoFlyZone.noFlyMessage != null
							&& !NoFlyZone.noFlyMessage.isEmpty()) {
						plugin.sms(p, NoFlyZone.noFlyMessage);
					}

					return;
				}

				if (NoFlyZone.noFlyRegions == null
						|| NoFlyZone.noFlyRegions.isEmpty()) {
					return;
				}

				if (!NoFlyZone.noFlyRegions.containsKey(world)
						|| NoFlyZone.noFlyRegions.get(world) == null
						|| NoFlyZone.noFlyRegions.get(world).isEmpty()) {
					return;
				}

				if (!p.isFlying()) {
					return;
				}

				if (NoFlyZone.getRegion(e.getTo()) == null) {
					return;
				}

				if (NoFlyZone.noFlyRegions.get(world).contains(
						NoFlyZone.getRegion(e.getTo()))) {

					if (p.hasPermission("noflyzone.bypass")) {
						return;
					}
					
					p.setAllowFlight(false);
					p.setFlying(false);

					double oldY = e.getFrom().getY();

					Location movingTo = e.getTo().clone();

					movingTo.setY(oldY);

					e.setTo(movingTo);

					if (NoFlyZone.noFlyMessage != null
							&& !NoFlyZone.noFlyMessage.isEmpty()) {
						plugin.sms(p, NoFlyZone.noFlyMessage);
					}

				}

			} else {

				String region = NoFlyZone.getRegion(e.getTo());
				
				if (region == null) {
					return;
				}
				
				
				if (NoFlyZone.flyRegions == null
						|| NoFlyZone.flyRegions.isEmpty()
						|| !NoFlyZone.flyRegions.containsKey(world)
						|| NoFlyZone.flyRegions.get(world) == null
						|| NoFlyZone.flyRegions.get(world).isEmpty()) {
					return;
				}
				
				if (!NoFlyZone.flyRegions.get(world).contains(region)) {
					return;
				}
				
				if (p.hasPermission("noflyzone.autofly.all") 
						|| p.hasPermission("noflyzone.autofly."+world+".all")
						|| p.hasPermission("noflyzone.autofly."+world+"."+region)) {
					p.setAllowFlight(true);
					
					if (NoFlyZone.autoFlyMessage != null
							&& !NoFlyZone.autoFlyMessage.isEmpty()) {
						plugin.sms(p, NoFlyZone.autoFlyMessage);
					}
				}
			}
		}	
	}
}

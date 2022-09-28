package com.elikill58.luckyuhc.luckyblocks.grenade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GrenadeManager implements Listener {

	public static int POWER_EXPLOSION = 8;
	private static final List<Location> GOOD_REPULSIVE_EXPLOSION = new ArrayList<>();
	
	public static void runGrenade(PlayerInteractEvent e, GrenadeType type) {
		Player p = e.getPlayer();
		Location explosionLoc = e.getClickedBlock() == null ? p.getEyeLocation() : e.getClickedBlock().getLocation();
		if(p.getEyeLocation().distance(p.getLocation()) < 20)
			return;
		if (type.equals(GrenadeType.REPULSIVE) || type.equals(GrenadeType.BAD_REPULSIVE)) {
			explosionLoc.add(0, 1, 0);
			p.getWorld().playEffect(explosionLoc, Effect.SMOKE, POWER_EXPLOSION);
			for (Entity et : p.getWorld().getEntities()) {
				if (et.getLocation().distance(explosionLoc) < POWER_EXPLOSION) {
					if(type.isGood())
						GOOD_REPULSIVE_EXPLOSION.add(explosionLoc);
					et.setVelocity(et.getVelocity().add(explosionLoc.toVector().multiply(-1)));
					/*Location explosionLoc = loc.subtract(et.getLocation()).add(0, 1, 0);
					if (!type.equals(GrenadeType.BAD_REPULSIVE))
						LAST_REPULSIVE_EXPLOSION.add(explosionLoc);
						et.setVelocity(et.getVelocity().subtract(explosionLoc.toVector()));
					Bukkit.getScheduler().runTaskLater(LuckyBlocks.INSTANCE, new BukkitRunnable() {
						@Override
						public void run() {
							if (LAST_REPULSIVE_EXPLOSION.contains(explosionLoc))
								LAST_REPULSIVE_EXPLOSION.remove(explosionLoc);
						}
					}, 10);*/
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!e.getCause().equals(DamageCause.BLOCK_EXPLOSION))
			return;
		Location loc = e.getEntity().getLocation();
		for (Location localLoc : GOOD_REPULSIVE_EXPLOSION)
			if (loc.distance(localLoc) < POWER_EXPLOSION * 1.1) {
				e.setCancelled(true);
				break;
			}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if (item != null) {
			GrenadeType.getGrenadeType(item).ifPresent((gt) -> {
				runGrenade(e, gt);
				if (item.getAmount() == 1)
					p.setItemInHand(null);
				else
					item.setAmount(item.getAmount() - 1);
			});
		}
	}
}

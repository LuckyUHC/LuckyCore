package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.block.BlockBreakEvent;

public class KillerRabbit extends BadLuckyBlock {

	@SuppressWarnings("deprecation")
	@Override
	public void run(BlockBreakEvent e) {
		Location loc = (Location) e.getPlayer().getEyeLocation();
		Rabbit entity = (Rabbit) loc.getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.RABBIT);
		entity.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
		entity.setMaxHealth(8);
	}

}

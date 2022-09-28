package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnSpider extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		Block b = e.getBlock();
		b.getLocation().add(0, 0, 1).getBlock().setType(Material.AIR);
		b.getLocation().add(0, 0, 2).getBlock().setType(Material.AIR);
		b.getLocation().add(0, 0, -1).getBlock().setType(Material.AIR);
		b.getLocation().add(0, 0, -2).getBlock().setType(Material.AIR);
		b.getWorld().spawnEntity(b.getLocation(), EntityType.SPIDER);
		
	}

}

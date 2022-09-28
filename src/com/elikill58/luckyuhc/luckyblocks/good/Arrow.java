package com.elikill58.luckyuhc.luckyblocks.good;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Arrow extends GoodLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.ARROW, new Random().nextInt(16) + 1));
	}
}

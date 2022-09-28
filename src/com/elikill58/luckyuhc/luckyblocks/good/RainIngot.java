package com.elikill58.luckyuhc.luckyblocks.good;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.elikill58.api.game.GameAPI;
import com.elikill58.luckyuhc.luckyblocks.LuckyBlocks;
import com.elikill58.luckyuhc.luckyblocks.Rain;

public class RainIngot extends GoodLuckyBlock {
	
	private ArrayList<ItemStack> items = new ArrayList<>();
	
	@Override
	public void run(BlockBreakEvent e) {
		LuckyBlocks.zoneRain(e, Material.AIR);
		items.clear();
		items.add(new ItemStack(Material.GOLDEN_APPLE));
		items.add(new ItemStack(Material.IRON_INGOT));
		items.add(new ItemStack(Material.GOLD_INGOT));
		items.add(new ItemStack(Material.DIAMOND));
		items.add(new ItemStack(Material.IRON_INGOT));
		items.add(new ItemStack(Material.IRON_INGOT));
		items.add(new ItemStack(Material.GOLD_INGOT));
		items.add(new ItemStack(Material.DIAMOND));
		new Rain(e.getBlock().getLocation().add(0, 2, 0), e.getBlock().getWorld(), 33, items).runTaskTimer(GameAPI.GAME_PROVIDER, 3, 3);
	}
}

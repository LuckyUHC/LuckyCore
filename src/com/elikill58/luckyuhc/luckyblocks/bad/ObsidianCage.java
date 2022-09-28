package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.luckyuhc.luckyblocks.LuckyBlocks;

public class ObsidianCage extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		LuckyBlocks.cageBadLuck(e.getPlayer(), Material.OBSIDIAN, Material.GLASS, Material.WATER);
	}

}

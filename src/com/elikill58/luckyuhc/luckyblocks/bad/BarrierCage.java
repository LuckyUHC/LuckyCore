package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.luckyuhc.luckyblocks.LuckyBlocks;

public class BarrierCage extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		LuckyBlocks.cageBadLuck(e.getPlayer(), Material.BARRIER, Material.BARRIER, Material.WATER);
	}
}

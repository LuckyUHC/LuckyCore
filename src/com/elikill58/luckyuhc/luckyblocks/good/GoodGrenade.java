package com.elikill58.luckyuhc.luckyblocks.good;

import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.luckyuhc.luckyblocks.LuckyBlocks;
import com.elikill58.luckyuhc.luckyblocks.grenade.GrenadeType;

public class GoodGrenade extends GoodLuckyBlock {
	
	@Override
	public void run(BlockBreakEvent e) {
		if(LuckyBlocks.runGrenade)
			LuckyBlocks.runRandomLuckyBlock(e, 0);
		e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), GrenadeType.getRandomGrenade(true).getItem());
	}
	
}

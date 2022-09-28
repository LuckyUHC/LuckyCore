package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.luckyuhc.luckyblocks.LuckyBlocks;
import com.elikill58.luckyuhc.luckyblocks.grenade.GrenadeType;

public class BadGrenade extends BadLuckyBlock {
	
	@Override
	public void run(BlockBreakEvent e) {
		if(LuckyBlocks.runGrenade)
			LuckyBlocks.runRandomLuckyBlock(e, 100);
		e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), GrenadeType.getRandomGrenade(false).getItem());
	}
	
}

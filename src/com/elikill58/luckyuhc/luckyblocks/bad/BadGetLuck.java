package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.api.game.GameAPI;

public class BadGetLuck extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		GameAPI.broadcast("luckyblock.break.bad.get_luck", "%name%", e.getPlayer().getName());
	}

}

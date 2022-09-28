package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NauseaEffect extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 1));
	}
}

package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SlownessEffect extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8, 1));
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8, 1));
	}
}

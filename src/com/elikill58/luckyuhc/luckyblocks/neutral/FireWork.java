package com.elikill58.luckyuhc.luckyblocks.neutral;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireWork extends NeutralLuckyBlock {

	@Override
	public void run(BlockBreakEvent e){
		Random r = new Random();
		int nbFW = r.nextInt(8);
		int rt = r.nextInt(4) + 1;
		int rp = r.nextInt(2) + 1;
		nbFW++;
		ItemStack fw = new ItemStack(Material.FIREWORK_ROCKET, nbFW);
		FireworkMeta fwm = (FireworkMeta) fw.getItemMeta();
		Type type = Type.BALL;
		if (rt == 1)
			type = Type.BALL;
		if (rt == 2)
			type = Type.BALL_LARGE;
		if (rt == 3)
			type = Type.BURST;
		if (rt == 4)
			type = Type.CREEPER;
		if (rt == 5)
			type = Type.STAR;
		fwm.addEffect(FireworkEffect.builder().flicker(r.nextBoolean()).withColor(getRandomColor())
				.withFade(getRandomColor()).with(type).trail(r.nextBoolean()).build());
		fwm.setPower(rp);
		fw.setItemMeta(fwm);
		e.getBlock().getWorld().dropItemNaturally(e.getPlayer().getLocation(), fw);
	}

	private Color getRandomColor() {
		int r = new Random().nextInt(17);
		r++;
		switch (r) {
		case 1:
			return Color.AQUA;
		case 2:
			return Color.BLACK;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.FUCHSIA;
		case 5:
			return Color.GRAY;
		case 6:
			return Color.GREEN;
		case 7:
			return Color.LIME;
		case 8:
			return Color.MAROON;
		case 9:
			return Color.NAVY;
		case 10:
			return Color.OLIVE;
		case 11:
			return Color.ORANGE;
		case 12:
			return Color.PURPLE;
		case 13:
			return Color.RED;
		case 14:
			return Color.SILVER;
		case 15:
			return Color.TEAL;
		case 16:
			return Color.WHITE;
		case 17:
			return Color.YELLOW;
		default:
			return Color.BLUE;
		}
	}
}

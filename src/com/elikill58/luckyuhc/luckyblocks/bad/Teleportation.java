package com.elikill58.luckyuhc.luckyblocks.bad;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public class Teleportation extends BadLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		Location loc = e.getPlayer().getLocation();
		loc.add(0, 20, 0);
		while(!loc.getBlock().getType().equals(Material.AIR)){
			loc.add(0, 1, 0);
		}
		e.getPlayer().sendMessage(ChatColor.YELLOW + "Woosh");
		e.getPlayer().teleport(loc);
	}

}

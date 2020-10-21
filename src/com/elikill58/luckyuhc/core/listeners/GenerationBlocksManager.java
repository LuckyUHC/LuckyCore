package com.elikill58.luckyuhc.core.listeners;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class GenerationBlocksManager implements Listener {

	private Random r = new Random();
	private int relia = 100;
	
	@EventHandler
	public void onGen(ChunkLoadEvent e) {
		if(!e.isNewChunk()) return;
		// entre 40 et 80
		Chunk c = e.getChunk();
		relia = 100;
		for(int y = 5; y < 80; y++) {
			for(int x = (c.getX() * 16) + 1; x < ((c.getX() + 1) * 16) - 1; x++) {
				for(int z = (c.getZ() * 16) + 1; z < ((c.getZ() + 1) * 16) - 1; z++) {
					if(relia <= 0)
						return;
					Block b = c.getWorld().getBlockAt(x, y, z);
					tryGenCane(b);
					//tryGenCane(addRandomMove(b.getLocation().clone()).getBlock());
				}
			}
		}
	}
	
	public Location addRandomMove(Location loc) {
		if(r.nextBoolean() && loc.getChunk().equals(loc.clone().add(1, 0, 0).getChunk()))
			return loc.add(1, 0, 0);
		if(r.nextBoolean() && loc.getChunk().equals(loc.clone().add(0, 0, 1).getChunk()))
			return loc.add(1, 0, 0);
		if(r.nextBoolean() && loc.getChunk().equals(loc.clone().add(-1, 0, 0).getChunk()))
			return loc.add(1, 0, 0);
		if(r.nextBoolean() && loc.getChunk().equals(loc.clone().add(0, 0, -1).getChunk()))
			return loc.add(1, 0, 0);
		return loc;
	}
	
	private void tryGenCane(Block b) {
		if(!b.getLocation().clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR))
			return;
		
		Location loc = b.getLocation().clone();
		if(r.nextInt(100) <= relia) {
			loc.add(0, 1, 0).getBlock().setType(Material.PRISMARINE);
			relia -= 10;
			if(r.nextInt(100) <= relia) {
				loc.add(0, 0, 1).getBlock().setType(Material.PRISMARINE);
				relia -= 20;
				if(r.nextInt(100) <= relia) {
					loc.add(1, 0, 0).getBlock().setType(Material.PRISMARINE);
					relia -= 30;
				}
			}
		}
	}
}

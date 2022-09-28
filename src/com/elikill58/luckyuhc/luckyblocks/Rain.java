package com.elikill58.luckyuhc.luckyblocks;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Rain extends BukkitRunnable {
	
	private Location loc;
	private World w;
	private int nb;
	private ArrayList<ItemStack> items = new ArrayList<>();
	
	public Rain(Location loc, World w, int nb, ArrayList<ItemStack> items, ItemStack... item){
		this.loc = loc;
		this.w = w;
		this.nb = nb;
		this.items = items;
		for(ItemStack it : item)
			this.items.add(it);
	}
	
	@Override
	public void run() {
		w.dropItemNaturally(loc, items.get(new Random().nextInt(items.size())));
    	
    	nb--;
    	if(nb == 0)
            this.cancel();
	}

}

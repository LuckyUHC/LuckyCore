package com.elikill58.luckyuhc.core.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class PickManager implements Listener {
	
	public static final List<Material> REMOVE = new ArrayList<>();
	
	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
		if(e.getItem() != null)
			if(REMOVE.contains(e.getItem().getItemStack().getType()))
				e.getItem().remove();
	}
	
}

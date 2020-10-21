package com.elikill58.luckyuhc.core.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.LuckyUtils;

public class CraftManager implements Listener {

	private static final HashMap<Material, Craft> CRAFT = new HashMap<>();

	public static void addCraft(Material m, Craft c) {
		CRAFT.put(m, c);
	}
	
	public static void removeCraft(Material m) {
		if(CRAFT.containsKey(m))
			CRAFT.remove(m);
	}
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		Material m = e.getRecipe().getResult().getType();
		if (CRAFT.containsKey(m))
			e.getInventory().setResult(CRAFT.get(m).toItem());
	}

	@EventHandler
	public void onOpenEnchanting(InventoryOpenEvent e) {
		if (e.getInventory() instanceof EnchantingInventory) {
			if(!LuckyCore.properties.enchantEnabled){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.GREEN + "Enchantement désactivé.");
			} else
				e.getInventory().setItem(1, new ItemStack(Material.LAPIS_LAZULI, 64));
		}
	}

	@EventHandler
	public void onClickEnchanting(InventoryClickEvent e) {
		if ((e.getClickedInventory() instanceof EnchantingInventory) && e.getSlot() == 1)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void inventoryClickEvent(EnchantItemEvent e) {
		if((e.getInventory() instanceof EnchantingInventory) && LuckyCore.properties.enchantEnabled)
			e.getInventory().setItem(1, new ItemStack(Material.LAPIS_LAZULI, 64));
	}

	@EventHandler
	public void onCloseEnchanting(InventoryCloseEvent e) {
		if (e.getInventory() instanceof EnchantingInventory)
			e.getInventory().setItem(1, null);
	}

	public static class Craft {

		private Material m;
		private int amount;
		private byte b = -1;
		private String name;

		public Craft(Material m, int amount) {
			this.m = m;
			this.amount = amount;
		}

		public Craft(Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.b = b;
		}

		public Craft(String name, Material m, int amount) {
			this.m = m;
			this.amount = amount;
			this.name = name;
		}

		public Craft(String name, Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.name = name;
			this.b = b;
		}

		@SuppressWarnings("deprecation")
		public ItemStack toItem() {
			if(m == null)
				return null;
			if (b != -1)
				if (name != null)
					return LuckyUtils.createItem(m, name, amount, b);
				else
					return new ItemStack(m, amount, b);
			else if (name != null)
				return LuckyUtils.createItem(m, name, amount);
			else
				return new ItemStack(m, amount);
		}
	}

}

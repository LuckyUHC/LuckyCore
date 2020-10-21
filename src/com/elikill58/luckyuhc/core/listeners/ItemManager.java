package com.elikill58.luckyuhc.core.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.elikill58.luckyuhc.core.LuckyCore;

@SuppressWarnings("deprecation")
public class ItemManager implements Listener {
	
	@EventHandler
	public void onPlayerPickUpItem(PlayerPickupItemEvent e){
		if(LuckyCore.properties.autoManageInventory)
			startEditInventory(e.getPlayer(), e.getItem().getItemStack().getType());
	}
	
	public void startEditInventory(Player p, Material m) {
		editInventory(p);
		deleteUselessItem(p, m);
	}

	private void editInventory(Player p) {
		Inventory inv = p.getInventory();
		ItemStack[] it = inv.getContents();
		HashMap<Material, Integer> ma = new HashMap<>();

		for (Material mato : getItemAtConvertToBlock())
			ma.put(mato, 0);

		for (int i = 0; i < it.length; i++) {
			if (it[i] != null) {
				Material m = it[i].getType();
				if (getItemAtConvertToBlock().contains(m))
					ma.put(m, ma.get(m) + it[i].getAmount());
			}
		}

		ma.forEach((m, i) -> {
			if (i > 64 || hasBlockToIngot(inv, m)) {
				for (ItemStack is : it)
					if (is != null)
						if (is.getType().equals(m))
							inv.removeItem(is);
				int nb = i / 9;
				int nbo = i - (nb * 9);
				if (nb > 0 && nbo > 0) {
					inv.addItem(new ItemStack(m, nbo));
					inv.addItem(new ItemStack(getBlockFromIngot(m), nb));
				}
			}
		});
	}

	private ArrayList<Material> getItemAtConvertToBlock() {
		ArrayList<Material> is = new ArrayList<>();
		is.add(Material.DIAMOND);
		is.add(Material.IRON_INGOT);
		is.add(Material.GOLD_INGOT);
		return is;
	}

	private Material getBlockFromIngot(Material m) {
		switch (m) {
		case IRON_INGOT:
			return Material.IRON_BLOCK;
		case GOLD_INGOT:
			return Material.GOLD_BLOCK;
		case DIAMOND:
			return Material.DIAMOND_BLOCK;
		default:
			return null;
		}
	}

	private boolean hasBlockToIngot(Inventory inv, Material m) {
		for (ItemStack it : inv.getContents())
			if (it != null)
				if (it.getType().equals(getBlockFromIngot(m)))
					return true;
		return false;
	}

	private void deleteUselessItem(Player p, Material m) {
		if (!getItemToDeleteOver64().contains(m))
			return;
		Inventory inv = p.getInventory();
		int nb = 0;
		for (ItemStack is : inv.getContents())
			if (is != null)
				if (is.getType().equals(m))
					nb = nb + is.getAmount();
		if (nb > 64) {
			boolean isDestroy = false;
			for (ItemStack is : inv.getContents())
				if (is != null && !is.getType().equals(m)) {
					if(!isDestroy) {
						isDestroy = true;
						if(is.getAmount() < 64)
							is.setAmount(64);
					} else {
						inv.removeItem(is);
					}
				}
			//inv.addItem(new ItemStack(m, 64));
		}
	}

	private ArrayList<Material> getItemToDeleteOver64() {
		ArrayList<Material> m = new ArrayList<>();
		m.add(Material.TORCH);
		for(Material all : Material.values()) {
			String name = all.name();
			if(name.startsWith("LEGACY_")) // don't use legacy items
				continue;
			if(name.contains("COOKED_") || name.contains("RAW_"))
				m.add(all);
			else if(name.contains("MUTTON") || name.contains("PORK") || name.contains("FISH"))
				m.add(all);
		}
		/*m.add(Material.COOKED_BEEF);
		m.add(Material.RAW_BEEF);
		m.add(Material.COOKED_CHICKEN);
		m.add(Material.RAW_CHICKEN);
		m.add(Material.COOKED_FISH);
		m.add(Material.COOKED_FISH);
		m.add(Material.COOKED_MUTTON);
		m.add(Material.MUTTON);
		m.add(Material.COOKED_RABBIT);
		m.add(Material.RABBIT);
		m.add(Material.CARROT);
		m.add(Material.CARROT_ITEM);
		m.add(Material.WORKBENCH);
		m.add(Material.GRILLED_PORK);
		m.add(Material.PORK);*/
		return m;
	}
}

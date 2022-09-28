package com.elikill58.luckyuhc.luckyblocks.good;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.elikill58.luckyuhc.core.generator.Generator;

public class LittleHouseGen extends GoodLuckyBlock {

	@Override
	public void run(BlockBreakEvent e) {
		Player p = e.getPlayer();
		ArrayList<Material> list = new ArrayList<>();
		list.add(Material.DIAMOND_BLOCK);
		list.add(Material.IRON_BLOCK);
		list.add(Material.DIAMOND_ORE);
		list.add(Material.IRON_ORE);
		list.add(Material.TNT);
		list.add(Material.EMERALD_ORE);
		list.add(Material.AIR);
		Block b = e.getBlock();
		int fX = b.getX(), fY = b.getY(), fZ = b.getZ();
		Generator.getGenerator().spawnSchematic(b.getLocation(), "little_house.schematic");
		Location lc = p.getLocation().clone();
		lc.getBlock().setType(Material.AIR);
		lc.add(0, 1, 0).getBlock().setType(Material.AIR);
		for (int y = (fY - 1); y != (fY + 8); y++)
			for (int x = fX; x != (fX + 8); x++)
				for (int z = (fZ - 3); z != (fZ + 6); z++) {
					Block b2 = b.getWorld().getBlockAt(x, y, z);
					if (b2.getType().equals(Material.END_STONE))
						b2.setType(list.get(new Random().nextInt(list.size())));
					else if (b2.getType().equals(Material.CHEST) && new Location(b2.getWorld(), x, y - 1, z).getBlock().getType().equals(Material.MELON)) {
						Chest c = (Chest) b2.getState();
						c.getInventory().setContents(getRandomStuff(c.getInventory().getContents().length));
						c.update();
					}
				}
	}

	public ItemStack[] getRandomStuff(int size) {
		Random r = new Random();
		ItemStack[] items = new ItemStack[size];
		int i = 10, amount = 0, slot = 0;
		while (i > 0) {
			if (Math.random() * 100 > 10) {
				slot = r.nextInt(size);
				while (items[slot] != null) {
					slot++;
					if (slot >= size)
						slot = 0;
				}
				items[slot] = new ItemStack(Material.DIAMOND, amount = r.nextInt(2) + 1);
				i -= (amount * 3);
			}
			if (Math.random() * 100 > 20) {
				slot = r.nextInt(size);
				while (items[slot] != null) {
					slot++;
					if (slot >= size)
						slot = 0;
				}
				items[slot] = new ItemStack(Material.ANVIL, amount = r.nextInt(1) + 1);
				i -= amount;
			}
			if (Math.random() * 100 > 50) {
				slot = r.nextInt(size);
				while (items[slot] != null) {
					slot++;
					if (slot >= size)
						slot = 0;
				}
				items[slot] = new ItemStack(Material.GOLDEN_APPLE, amount = r.nextInt(7) + 1);
				i -= amount * 2;
			}
			if (Math.random() * 100 > 20) {
				slot = r.nextInt(size);
				while (items[slot] != null) {
					slot++;
					if (slot >= size)
						slot = 0;
				}
				items[slot] = new ItemStack(Material.ENCHANTING_TABLE, amount = r.nextInt(2) + 1);
				i -= amount;
			}
			if (Math.random() * 100 > 18) {
				slot = r.nextInt(size);
				while (items[slot] != null) {
					slot++;
					if (slot >= size)
						slot = 0;
				}
				items[slot] = getRandomStuff();
				i -= amount + 1;
			}
		}
		return items;
	}

	public ItemStack getRandomStuff() {
		ItemStack item = null;
		switch (new Random().nextInt(14)) {
		case 0:
			item = new ItemStack(Material.GOLDEN_CHESTPLATE);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			break;
		case 1:
			item = new ItemStack(Material.IRON_CHESTPLATE);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			break;
		case 2:
			item = new ItemStack(Material.DIAMOND_CHESTPLATE);
			break;
		case 3:
			item = new ItemStack(Material.GOLDEN_PICKAXE);
			item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			break;
		case 4:
			item = new ItemStack(Material.IRON_BOOTS);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			break;
		case 5:
			item = new ItemStack(Material.IRON_BOOTS);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
			break;
		case 6:
			item = new ItemStack(Material.DIAMOND_BOOTS);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			break;
		case 7:
			item = new ItemStack(Material.IRON_PICKAXE);
			item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
			break;
		case 8:
			item = new ItemStack(Material.IRON_SWORD);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			break;
		case 9:
			item = new ItemStack(Material.DIAMOND_HELMET);
			break;
		case 10:
			item = new ItemStack(Material.IRON_PICKAXE);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			break;
		case 11:
			item = new ItemStack(Material.DIAMOND_LEGGINGS);
			break;
		case 12:
			item = new ItemStack(Material.GOLDEN_LEGGINGS);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			break;
		case 13:
			item = new ItemStack(Material.DIAMOND_SWORD);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1);
			break;
		}
		return item;
	}
}

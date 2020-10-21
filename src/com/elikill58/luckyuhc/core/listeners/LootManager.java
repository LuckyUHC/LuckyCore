package com.elikill58.luckyuhc.core.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.LuckyUtils;

public class LootManager implements Listener {

	private static HashMap<EntityType, HashMap<Loot, Integer>> LOOT = new HashMap<>();
	private static HashMap<EntityType, List<Material>> NO_LOOT = new HashMap<>();

	public static void addLoot(EntityType e, Loot l, int luck) {
		HashMap<Loot, Integer> futurLoot = LOOT.containsKey(e) ? LOOT.get(e) : new HashMap<>();
		futurLoot.put(l, luck);
		LOOT.put(e, futurLoot);
	}

	public static void removeLoot(EntityType e, Material m) {
		List<Material> futurLoot = NO_LOOT.containsKey(e) ? NO_LOOT.get(e) : new ArrayList<>();
		futurLoot.add(m);
		NO_LOOT.put(e, futurLoot);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getClickedBlock() == null)
			return;
		if(e.getClickedBlock().getType().equals(Material.OBSIDIAN))
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10, 2));
	}
	
	@EventHandler
	public void onPlayerHitMob(EntityDamageByEntityEvent e) {
		Entity et = e.getEntity();
		if (!(e.getDamager() instanceof Player))
			return;
		Player p = (Player) e.getDamager();
		if (et instanceof Rabbit) {
			if (!((Rabbit) et).getRabbitType().equals(Rabbit.Type.THE_KILLER_BUNNY)) {
				e.setDamage(1000);
				p.giveExp(LuckyCore.properties.xpPassiveMob);
			}
		} else if ((et instanceof Bat || et instanceof Pig || et instanceof Sheep || et instanceof Cow
				|| et instanceof Chicken || et instanceof Squid || et instanceof MushroomCow || et instanceof Ocelot)
				&& LuckyCore.properties.autoDeathOfPassiveMob) {
			e.setDamage(1000);
			p.giveExp(LuckyCore.properties.xpPassiveMob);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity et = e.getEntity();

		ArrayList<ItemStack> nodrops = new ArrayList<ItemStack>();
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (LOOT.containsKey(et.getType())) {
			HashMap<Loot, Integer> hash = LOOT.get(et.getType());
			Random r = new Random();
			hash.forEach((loot, i) -> {
				if (i <= r.nextInt(100))
					drops.add(loot.toItem());
			});
			if (LuckyCore.properties.clearEntityDrop.get(et.getType()))
				e.getDrops().clear();
		}
		if (NO_LOOT.containsKey(et.getType()))
			for (ItemStack item : e.getDrops())
				if (NO_LOOT.get(et.getType()).contains(item.getType()))
					nodrops.add(item);
		e.getDrops().removeAll(nodrops);
		e.getDrops().addAll(drops);
	}

	public static class Loot {

		private Material m;
		private int amount;
		private byte b = -1;
		private String name;

		public Loot(Material m, int amount) {
			this.m = m;
			this.amount = amount;
		}

		public Loot(Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.b = b;
		}

		public Loot(String name, Material m, int amount) {
			this.m = m;
			this.amount = amount;
			this.name = name;
		}

		public Loot(String name, Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.name = name;
			this.b = b;
		}

		public ItemStack toItem() {
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

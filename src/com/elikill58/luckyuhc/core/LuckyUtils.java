package com.elikill58.luckyuhc.core;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.elikill58.luckyuhc.core.listeners.OthersEvents;

import fr.zonefun.api.spigot.APIUtils;
import fr.zonefun.api.spigot.builders.ItemStackBuilder;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class LuckyUtils {
	
	public static ItemStack createItem(Material m, String name, int quantite, String... lore){
		return new ItemStackBuilder(m, quantite).displayName(name).lore(lore).build();
	}

	public static ItemStack createItem(Material m, String name){
		return new ItemStackBuilder(m).displayName(name).build();
	}

	public static ItemStack createItem(Material m, String name, int quantite, byte byt, String... lore){
		return new ItemStackBuilder(m, quantite, byt).displayName(name).lore(lore).build();
	}
	
	public static ItemStack createItem(Material m, String name, int quantite, Enchantment enchant, int lvl, String... lore){
		return new ItemStackBuilder(m, quantite).displayName(name).lore(lore).unsafeEnchant(enchant, lvl).build();
	}

	public static ItemStack createSkull(String name, int amount, String owner, String... lore) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
		skullmeta.setDisplayName(APIUtils.applyColorCodes(name));
		skullmeta.setOwner(owner);
		List<String> lorel = new ArrayList<>();
		for(String s : lore)
			lorel.add(APIUtils.applyColorCodes(s));
		skullmeta.setLore(lorel);
		skull.setItemMeta(skullmeta);
		return skull;
	}

	public static String toTime(int i){
        int min = i / 60, sec = i - (min * 60);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        return nf.format(min) + ":" + nf.format(sec);
    }

	public static List<Player> getSurvivor(){
		List<Player> list = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getGameMode().equals(GameMode.SURVIVAL))
				list.add(p);
		return list;
	}
	
	public static List<Player> getSpectator(){
		List<Player> list = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.getGameMode().equals(GameMode.SPECTATOR))
				list.add(p);
		return list;
	}
	
	public static void respawnInstant(final Player player) {
		OthersEvents.CONTENT.put(player.getName() + "-content", player.getInventory().getContents());
		OthersEvents.CONTENT.put(player.getName() + "-armorcontent", player.getInventory().getArmorContents());
        Bukkit.getScheduler().runTaskLater(LuckyCore.INSTANCE, () -> {
            PacketPlayInClientCommand paquet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
            ((CraftPlayer) player).getHandle().playerConnection.a(paquet);
            player.setGameMode(GameMode.SPECTATOR);
            if (LuckyUtils.getSpectator().size() > LuckyCore.properties.maxSpecs) {
            	player.sendMessage("Nombre maximum de joueur atteint !");
                APIUtils.tpToServer(player, "Lobby");
            }
        }, 5L);
    }
	
	public static boolean deleteWorld(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteWorld(file);
				} else {
					file.delete();
				}
			}
			return path.delete();
		}
		return path.delete();
	}

	public static void genLocAround(Location loc) {
		gen(loc);
		int[] x = new int[] { 0, 0, -16, -16, 0, 0, 0, 0, 16, 16, 16, 16, 0, 0, 0, 0, -16, 0, 0, 0, -16, -16, 0, 0 },
				z = new int[] { 16, 16, 0, 0, -16, -16, -16, -16, 0, 0, 0, 0, 16, 16, 16, 16, 0, -16, -16, -16, 0, 0,
						16, 16 };
		for (int i = 0; i < x.length; i++) {
			loc.add(x[i], 0, z[i]);
			gen(loc);
		}
	}

	private static void gen(Location loc) {
		loc.getChunk().load(true);
		loc.getChunk().unload(false);
	}
	
	public static void genereAround(Location loc, Material m) {
		loc.clone().subtract(0, 1, 0).getBlock().setType(m);
		loc.clone().add(0, 2, 0).getBlock().setType(m);

		loc.clone().add(1, 0, 0).getBlock().setType(m);
		loc.clone().add(0, 0, 1).getBlock().setType(m);
		loc.clone().add(-1, 0, 0).getBlock().setType(m);
		loc.clone().add(0, 0, -1).getBlock().setType(m);

		loc.clone().add(1, 1, 0).getBlock().setType(m);
		loc.clone().add(0, 1, 1).getBlock().setType(m);
		loc.clone().add(-1, 1, 0).getBlock().setType(m);
		loc.clone().add(0, 1, -1).getBlock().setType(m);
	}
}

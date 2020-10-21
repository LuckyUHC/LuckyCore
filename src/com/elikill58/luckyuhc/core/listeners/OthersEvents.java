package com.elikill58.luckyuhc.core.listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.phases.LobbyPhase;

import fr.zonefun.api.spigot.SpigotPlayerData;
import fr.zonefun.api.universal.Rank;
import fr.zonefun.gameapi.GameAPI;

public class OthersEvents implements Listener {

	public static final HashMap<String, ItemStack[]> CONTENT = new HashMap<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (e.getFrom().getY() < 120 && GameAPI.ACTIVE_PHASE.name.equalsIgnoreCase("lobby") && !p.getGameMode().equals(GameMode.CREATIVE))
			p.teleport(LuckyCore.properties.lobby.get(new Random().nextInt(LuckyCore.properties.lobby.size())));
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(GameAPI.ACTIVE_PHASE.name.equalsIgnoreCase("lobby"))
			e.setCancelled(true);
	}

	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e) {
		String cmd = e.getMessage();
		Player p = e.getPlayer();
		SpigotPlayerData pi = SpigotPlayerData.getSpigotPlayerData(p);
		if (!pi.hasPower(Rank.CELEBRITE))
			return;
		if(cmd.startsWith("/forcestart")) {
			e.setCancelled(true);
			if(GameAPI.ACTIVE_PHASE.name.equals("lobby") && !LobbyPhase.timer.isRunning()) {
				LobbyPhase.timer.start();
				p.sendMessage(LuckyCore.game.prefix() + ChatColor.GREEN + " Démarrage forcé.");
			} else {
				p.sendMessage(LuckyCore.game.prefix() + ChatColor.RED + " Déjà démarré.");
			}
		} else if (!cmd.startsWith("/revive"))
			return;
		e.setCancelled(true);
		String[] arg = e.getMessage().split(" ");
		String nameToRevive = p.getName();
		if (arg.length > 1)
			if (Bukkit.getPlayer(arg[1]) != null)
				nameToRevive = arg[1];
		Player revive = Bukkit.getPlayer(nameToRevive);
		if (revive.getGameMode().equals(GameMode.SURVIVAL)) {
			p.sendMessage(nameToRevive + " est déjà en vie !");
		} else {
			revive.setGameMode(GameMode.SURVIVAL);
			PlayerInventory inv = revive.getInventory();
			if (CONTENT.containsKey(nameToRevive + "-content"))
				inv.setContents(CONTENT.get(nameToRevive + "-content"));
			if (CONTENT.containsKey(nameToRevive + "-armorcontent"))
				inv.setArmorContents(CONTENT.get(nameToRevive + "-armorcontent"));
		}
	}

	@EventHandler
	public void onEntity(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player))
			return;
		if(LuckyCore.properties.defaultKnockback)
			return;
		e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(0.85));
	}
}

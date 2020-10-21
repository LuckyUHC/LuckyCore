package com.elikill58.luckyuhc.core.phases;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import com.elikill58.luckyuhc.core.LuckyCore;

import fr.zonefun.api.spigot.SpigotPlayerData;
import fr.zonefun.gameapi.GameAPI;
import fr.zonefun.gameapi.PhaseEvent;
import fr.zonefun.gameapi.PhaseTimer;

public class LobbyPhase {

	public static PhaseTimer timer = PhaseTimer.builder()
			.phase(LuckyCore.game.startingPhase())
			.duration(30)
			.actionbar(true)
			.actionbarRange(60, 1, "starting.start_in")
			.actionbarReminder(1, "starting.start_in_one")
			.actionbarReminder(0, "starting.start")
			.build();

	@PhaseEvent.Annotation(PhaseEvent.JOIN)
	public static void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		
		Player player = event.getPlayer();
		SpigotPlayerData playerData = SpigotPlayerData.getSpigotPlayerData(player);
		GameAPI.broadcast("join_message",
                "%grade%", playerData.getDisplayRank(),
                "%playername%", player.getName(),
                "%playercount%", String.valueOf(Bukkit.getOnlinePlayers().size()),
                "%max%", String.valueOf(LuckyCore.properties.maxPlayers));
		player.setDisplayName(player.getName());
		player.setGameMode(GameMode.ADVENTURE);
		player.teleport(LuckyCore.properties.lobby.get(new Random().nextInt(LuckyCore.properties.lobby.size())));
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setExp(0);
		player.setTotalExperience(0);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setPlayerListName(player.getName());
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

	@PhaseEvent.Annotation(PhaseEvent.LEAVE)
	public static void onLeft(PlayerQuitEvent event) {
		event.setQuitMessage("");
		
		Player player = event.getPlayer();
		SpigotPlayerData playerData = SpigotPlayerData.getSpigotPlayerData(player);
        GameAPI.broadcast("leave_message",
                "%grade%", playerData.getDisplayRank(),
                "%playername%", player.getName(),
                "%playercount%", String.valueOf(Bukkit.getOnlinePlayers().size() - 1),
                "%max%", String.valueOf(LuckyCore.properties.maxPlayers));
        
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed((float) 0.2);
        player.getInventory().setArmorContents(null);
		for (PotionEffect pf : player.getActivePotionEffects())
			player.removePotionEffect(pf.getType());
		if (Bukkit.getOnlinePlayers().size() - 1 < LuckyCore.properties.minPlayers && timer.isRunning())
			timer.cancel();
	}
	
	@PhaseEvent.Annotation(PhaseEvent.END)
	public static void onEnd() {
		timer.cancel();
	}
}
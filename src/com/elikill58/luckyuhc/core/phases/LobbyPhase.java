package com.elikill58.luckyuhc.core.phases;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import com.elikill58.api.PlayerData;
import com.elikill58.api.game.GameAPI;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.scoreboard.ObjectiveSign;
import com.elikill58.luckyuhc.core.LuckyCore;

@SuppressWarnings("deprecated")
public class LobbyPhase extends Phase {

	public static PhaseTimer timer = PhaseTimer.builder()
			.phase(LuckyCore.game.startingPhase())
			.duration(30)
			.actionbar(true)
			.actionbarRange(60, 1, "starting.start_in")
			.actionbarReminder(1, "starting.start_in_one")
			.actionbarReminder(0, "starting.start")
			.build();
	
	public LobbyPhase() {
		super("lobby", "Lobby", true, true);
	}

	@Override
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		
		Player player = event.getPlayer();
		PlayerData playerData = PlayerData.getPlayerData(player);
		GameAPI.broadcast("join_message",
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

	@Override
	public void onLeft(PlayerQuitEvent event) {
		event.setQuitMessage("");
		
		Player player = event.getPlayer();
		PlayerData playerData = PlayerData.getPlayerData(player);
        GameAPI.broadcast("leave_message",
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

	@Override
	public void onEnd() {
		timer.cancel();
	}

	@Override
	public void setScoreboardLines(Player p, ObjectiveSign sign) {
		
	}
}
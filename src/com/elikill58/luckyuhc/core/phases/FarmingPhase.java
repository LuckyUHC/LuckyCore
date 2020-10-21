package com.elikill58.luckyuhc.core.phases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elikill58.api.game.GameAPI;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.game.phase.PhaseTimer;
import com.elikill58.api.scoreboard.ObjectiveSign;
import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.LuckyUtils;

public class FarmingPhase extends Phase {

	public static PhaseTimer timer = PhaseTimer.builder()
			.phase(LuckyCore.properties.fightPhase())
			.duration(LuckyCore.properties.timeBeforeTp)
			.actionbar(true)
			.actionbarRange(99999, 60, ChatColor.GRAY + "Téléportation dans %mins% mins et %secs% secondes")
			.actionbarRange(60, 1, ChatColor.GRAY + "Téléporation dans %secs% secondes")
			.actionbarReminder(0, ChatColor.GRAY + "Téléportation ...")
			.build();
	private static int task;
	
	public FarmingPhase() {
		super("farming", "Farming", false, true);
	}

	@Override
	public void onStart() {
		final List<Location> locs = new ArrayList<>();
		for(Location temp : LuckyCore.FARMING_LOCS)
			locs.add(temp);
		Random randomtp = new Random();
		final List<Player> toTp = new ArrayList<>();
		for (Player pp : Bukkit.getOnlinePlayers())
			toTp.add(pp);
		GameAPI.broadcast("starting.tp_each_3");
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(LuckyCore.INSTANCE, new Runnable() {
			@Override
			public void run() {
				if(toTp.size() == 0) {
					Bukkit.getScheduler().cancelTask(task);
					timer.start();
					LuckyCore.deletelobby();
					for(Location loc : LuckyCore.FARMING_LOCS)
						LuckyUtils.genereAround(loc, Material.AIR);
					task = Bukkit.getScheduler().scheduleSyncRepeatingTask(LuckyCore.INSTANCE, new Runnable() {
						@Override
						public void run() {
							if (GameAPI.ACTIVE_PHASE.display.equalsIgnoreCase("fight"))
								Bukkit.getScheduler().cancelTask(task);
							for (Player p : Bukkit.getOnlinePlayers())
								p.setPlayerListName(p.getName() + " " + ChatColor.YELLOW + ((int) p.getHealth()));
						}
					}, 0, 20);
				} else {
					Player p = toTp.get(0);
					Location randomlocation = locs.get(randomtp.nextInt(locs.size()));
					LuckyUtils.genereAround(randomlocation, Material.GLASS);
					p.teleport(randomlocation);
					locs.remove(randomlocation);
					toTp.remove(p);
				}
			}
		}, 0, 60);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage(null);
		p.sendMessage(ChatColor.RED + "La partie a déjà commencé.");
        p.setMaxHealth(20);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setAllowFlight(false);
		p.setGameMode(GameMode.SPECTATOR);
	}

	@Override
	public void onLeft(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		GameAPI.broadcast("game.player_leave", "%name%", p.getName());
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(null);
		LuckyCore.manageDeathMessage(e);
		LuckyUtils.respawnInstant(p);
	}
	
	@Override
	public void setScoreboardLines(Player p, ObjectiveSign sign) {
		
	}
}

package com.elikill58.luckyuhc.core.phases;

import java.text.NumberFormat;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elikill58.api.ActionBar;
import com.elikill58.api.Messages;
import com.elikill58.api.game.GameAPI;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.scoreboard.ObjectiveSign;
import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.LuckyUtils;

public class FightPhase extends Phase {

	public static int task, timeBeforeBorderMove = 30;
	public static final NumberFormat nf = NumberFormat.getInstance();

	public FightPhase() {
		super("fight", "Fight", false, false);
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(null);
		LuckyCore.manageDeathMessage(e);
		LuckyUtils.respawnInstant(p);
	}

	@Override
	public void onStart() {
		nf.setMinimumIntegerDigits(2);
		Bukkit.getScheduler().runTaskLater(LuckyCore.INSTANCE, new Runnable() {
			@Override
			public void run() {
				GameAPI.ACTIVE_GAME.properties.pvp(true);
				WorldBorder wb = LuckyCore.getWorld().getWorldBorder();
				wb.setSize(400);
				task = Bukkit.getScheduler().scheduleSyncRepeatingTask(LuckyCore.INSTANCE, new Runnable() {
					public void run() {
						if (timeBeforeBorderMove == 0) {
							if (LuckyCore.properties.timeBorder == 0) {
								GameAPI.broadcast("border.end_move");
								Bukkit.getScheduler().cancelTask(task);
							} else {
								LuckyCore.properties.timeBorder--;
								int min = LuckyCore.properties.timeBorder / 60;
								int sec = LuckyCore.properties.timeBorder - (min * 60);
								ActionBar.sendToAll(Messages.getMessage("border.actionbar.end_in", "%min%", nf.format(min), "%sec%",
										nf.format(sec)));
							}
						} else {
							timeBeforeBorderMove--;
							if(timeBeforeBorderMove == 0) {
								LuckyCore.properties.disableAllDamages(false);
								wb.setSize(200, LuckyCore.properties.timeBorder);
							}
						}
					}
				}, 0, 20);
			}
		}, 20 * 60);
	}

	@Override
	public void onLeft(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		List<Player> survivor = LuckyUtils.getSurvivor();
		if (survivor.contains(p))
			survivor.remove(p);
		if (survivor.size() < 2)
			LuckyCore.end(survivor);
	}
	
	@Override
	public void setScoreboardLines(Player p, ObjectiveSign sign) {
		
	}
}

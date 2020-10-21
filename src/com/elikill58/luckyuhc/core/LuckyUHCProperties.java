package com.elikill58.luckyuhc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.elikill58.api.game.GameProperties;
import com.elikill58.api.game.phase.Phase;

public class LuckyUHCProperties extends GameProperties {
	
	public boolean useBooster = true, enchantEnabled = true, autoDeathOfPassiveMob = true,
			autoManageInventory = false, mobDropHead = false, playerDropHead = true, appleGold = true, winCoins = true, defaultKnockback = true;
	public HashMap<EntityType, Boolean> clearEntityDrop = new HashMap<>();
	public List<Location> lobby = new ArrayList<>();
	public int xpPassiveMob = 6, lvlBiblio = 0, luck = 50, appleRate = 10, timeBorder = 5 * 60, timeBeforeTp = 10 * 60;
	
	public LuckyUHCProperties xpPassiveMob(int i) {
		xpPassiveMob = i;
		return this;
	}
	
	public LuckyUHCProperties useBooster(boolean b) {
		useBooster = b;
		return this;
	}
	
	public LuckyUHCProperties enchantEnabled(boolean b) {
		enchantEnabled = b;
		return this;
	}
	
	public LuckyUHCProperties autoDeathPassiveMob(boolean b) {
		autoDeathOfPassiveMob = true;
		return this;
	}

	public LuckyUHCProperties lobby(Location loc) {
		lobby.add(loc);
		return this;
	}
	
	public LuckyUHCProperties lobby(Location loc, int place) {
		lobby.add(loc);
		return this;
	}
	
	public LuckyUHCProperties clearEntityDrop(EntityType type, boolean b) {
		clearEntityDrop.put(type, b);
		return this;
	}
	
	public LuckyUHCProperties autoManageInventory(boolean b) {
		autoManageInventory = b;
		return this;
	}

	public LuckyUHCProperties levelBiblio(int i){
		this.lvlBiblio = i;
		return this;
	}

	public LuckyUHCProperties luck(int i){
		this.luck = i;
		return this;
	}

	public LuckyUHCProperties appleRate(int i){
		this.appleRate = i;
		return this;
	}
	
	public LuckyUHCProperties mobDropHead(boolean b){
		this.mobDropHead = b;
		return this;
	}

	public LuckyUHCProperties playerDropHead(boolean b){
		this.playerDropHead = b;
		return this;
	}
	
	public LuckyUHCProperties appleGold(boolean b){
		this.appleGold = b;
		return this;
	}

	public LuckyUHCProperties timeBorder(int i){
		this.timeBorder = i;
		return this;
	}

	public LuckyUHCProperties timeBeforeTp(int i){
		this.timeBeforeTp = i;
		return this;
	}

	public Phase fightPhase() {
		return LuckyCore.FIGHT;
	}
	
	public LuckyUHCProperties fightPhase(Phase phase) {
		LuckyCore.FIGHT = phase;
		return this;
	}
	
	public LuckyUHCProperties winCoins(boolean b) {
		this.winCoins = b;
		return this;
	}
}

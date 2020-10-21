package com.elikill58.luckyuhc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.elikill58.api.game.GameProperties;
import com.elikill58.api.game.phase.Phase;

public class GameValue extends GameProperties {
	
	public boolean useBooster = true, enchantEnabled = true, autoDeathOfPassiveMob = true,
			autoManageInventory = false, mobDropHead = false, playerDropHead = true, appleGold = true, winCoins = true, defaultKnockback = true;
	public HashMap<EntityType, Boolean> clearEntityDrop = new HashMap<>();
	public List<Location> lobby = new ArrayList<>();
	public int xpPassiveMob = 6, lvlBiblio = 0, luck = 50, appleRate = 10, timeBorder = 5 * 60, timeBeforeTp = 10 * 60;
	
	public GameValue xpPassiveMob(int i) {
		xpPassiveMob = i;
		return this;
	}
	
	public GameValue useBooster(boolean b) {
		useBooster = b;
		return this;
	}
	
	public GameValue enchantEnabled(boolean b) {
		enchantEnabled = b;
		return this;
	}
	
	public GameValue autoDeathPassiveMob(boolean b) {
		autoDeathOfPassiveMob = true;
		return this;
	}

	public GameValue lobby(Location loc) {
		lobby.add(loc);
		return this;
	}
	
	public GameValue lobby(Location loc, int place) {
		lobby.add(loc);
		return this;
	}
	
	public GameValue clearEntityDrop(EntityType type, boolean b) {
		clearEntityDrop.put(type, b);
		return this;
	}
	
	public GameValue autoManageInventory(boolean b) {
		autoManageInventory = b;
		return this;
	}

	public GameValue levelBiblio(int i){
		this.lvlBiblio = i;
		return this;
	}

	public GameValue luck(int i){
		this.luck = i;
		return this;
	}

	public GameValue appleRate(int i){
		this.appleRate = i;
		return this;
	}
	
	public GameValue mobDropHead(boolean b){
		this.mobDropHead = b;
		return this;
	}

	public GameValue playerDropHead(boolean b){
		this.playerDropHead = b;
		return this;
	}
	
	public GameValue appleGold(boolean b){
		this.appleGold = b;
		return this;
	}

	public GameValue timeBorder(int i){
		this.timeBorder = i;
		return this;
	}

	public GameValue timeBeforeTp(int i){
		this.timeBeforeTp = i;
		return this;
	}

	public Phase fightPhase() {
		return LuckyCore.FIGHT;
	}
	
	public GameValue fightPhase(Phase phase) {
		LuckyCore.FIGHT = phase;
		return this;
	}
	
	public GameValue winCoins(boolean b) {
		this.winCoins = b;
		return this;
	}
}

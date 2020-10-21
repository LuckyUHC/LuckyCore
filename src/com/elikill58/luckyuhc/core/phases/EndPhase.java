package com.elikill58.luckyuhc.core.phases;

import org.bukkit.entity.Player;

import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.scoreboard.ObjectiveSign;

public class EndPhase extends Phase {
	
	public EndPhase() {
		super("end", "End", false, false);
	}
	
	@Override
	public void setScoreboardLines(Player p, ObjectiveSign sign) {
		
	}
}

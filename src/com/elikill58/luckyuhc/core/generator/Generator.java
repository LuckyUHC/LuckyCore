package com.elikill58.luckyuhc.core.generator;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.elikill58.api.Version;
import com.elikill58.api.game.GameAPI;
import com.elikill58.luckyuhc.core.LuckyCore;

public interface Generator {
	
	public void spawnSchematic(Location loc, String filename);
	
	public static Generator getGenerator() {
		if(LuckyCore.generator != null)
			return LuckyCore.generator;
		Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		Logger log = GameAPI.GAME_PROVIDER.getLogger();
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			if(worldEdit == null) {
				log.severe("Cannot load LuckyUHC. When the server is on 1.13 or upper, WorldEdit is required to paste schematic.");
				log.severe("We are working on removing this dependancy.");
			} else {
				log.info("Loaded support of WorldEdit 7.");
				LuckyCore.generator = new WorldEdit7Generator();
			}
		} else {
			if(worldEdit == null) {
				log.info("Loaded support of Spigot schematic.");
				LuckyCore.generator = new Spigot1_8Generator();
			} else {
				log.info("Loaded support of WorldEdit 6.");
				LuckyCore.generator = new WorldEdit6Generator();
			}
		}
		return LuckyCore.generator;
	}
}

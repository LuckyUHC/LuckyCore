package com.elikill58.luckyuhc.core.generator;

import java.io.File;

import org.bukkit.Location;

import com.elikill58.api.game.GameAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;

public class WorldEdit6Generator implements Generator {

	@Override
	public void spawnSchematic(Location loc, String filename) {
		try {
			File f = new File(GameAPI.GAME_PROVIDER.getDataFolder() + "/hub.schematic");
			EditSession es = EditSession.class.getConstructor(Class.forName("com.sk89q.worldedit.LocalWorld"), int.class)
					.newInstance(new BukkitWorld(loc.getWorld()), 999999999);
			Class<?> cuboidClipboardClass = Class.forName("com.sk89q.worldedit.CuboidClipboard");
			Object cc = cuboidClipboardClass.getDeclaredMethod("loadSchematic", f.getClass()).invoke(null, f);
			Class<?> vectorClass = Class.forName("com.sk89q.worldedit.Vector");
			Object vector = vectorClass.getConstructor(int.class, int.class, int.class).newInstance(0, 140, 0);
			
			cc.getClass().getDeclaredMethod("paste", es.getClass(), vectorClass, boolean.class).invoke(cc, es, vector, false);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}

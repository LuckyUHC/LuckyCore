package com.elikill58.luckyuhc.core.generator;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Location;

import com.elikill58.api.game.GameAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

public class WorldEdit7Generator implements Generator {

	@Override
	public void spawnSchematic(Location loc, String filename) {
		try { // Pasting Operation
				// We need to adapt our world into a format that worldedit accepts. This looks
				// like this:
				// Ensure it is using com.sk89q... otherwise we'll just be adapting a world into
				// the same world.
			File file = new File(GameAPI.GAME_PROVIDER.getDataFolder(), filename);
			ClipboardReader reader = ClipboardFormats.findByFile(file).getReader(new FileInputStream(file));
		    Clipboard clipboard = reader.read();
			
			com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(loc.getWorld());

			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);

			// Saves our operation and builds the paste - ready to be completed.
			Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
					.to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ())).ignoreAirBlocks(true).build();

			Operations.complete(operation);
			editSession.flushSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

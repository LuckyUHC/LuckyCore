package com.elikill58.luckyuhc.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.elikill58.api.Version;
import com.elikill58.api.game.GameAPI;
import com.elikill58.api.utils.PacketUtils;

@SuppressWarnings("deprecation")
public class Generator {

	public static void spawnSchematic(Location loc, String filename) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13))
			spawnSchematic1_13(loc, filename);
		else
			spawnSchematic1_8(loc, filename);
	}
	
	public static void spawnSchematic1_8(Location loc, String filename) {
        try {
            FileInputStream fis = new FileInputStream(new File(GameAPI.GAME_PROVIDER.getDataFolder(), filename));
            Object nbtData = PacketUtils.getNmsClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, fis);
            Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            short width = ((short) getShort.invoke(nbtData, "Width"));
            short height = ((short) getShort.invoke(nbtData, "Height"));
            short length = ((short) getShort.invoke(nbtData, "Length"));

            byte[] blocks = ((byte[]) getByteArray.invoke(nbtData, "Blocks"));
            //byte[] data = ((byte[]) getByteArray.invoke(nbtData, "Data"));

            fis.close();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int z = 0; z < length; z++) {
                        int index = y * width * length + z * width + x;
                        int b = blocks[index] & 0xFF;//make the block unsigned,
                        // so that blocks with an id over 127, like quartz and emerald, can be pasted
                        Material m = null;//Material.getMaterial(b);
                        for(Material all : Material.values())
                        	if(all.getId() == b)
                        		m = all;
                        //Y - 10 -> height of structure bottom to center (Y)
                        if (m != Material.AIR) {
                            //The 10 in the following line can be changed to adjust Y alignment with the ground
                            Block block = new Location(loc.getWorld(), loc.getBlockX() - ((int) (width / 2)) + x, loc.getBlockY() - 10 + y, loc.getBlockZ() - ((int) (length / 2)) + z).getBlock();
                            block.setType(m, true);
                            //block.setData(data[index]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void spawnSchematic1_13(Location loc, String filename) {
		// TODO fix for 1.13
		spawnSchematic1_8(loc, filename);
	}
}

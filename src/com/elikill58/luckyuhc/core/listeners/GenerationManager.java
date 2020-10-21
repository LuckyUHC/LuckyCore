package com.elikill58.luckyuhc.core.listeners;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;

import com.elikill58.api.game.GameAPI;
import com.elikill58.luckyuhc.core.LuckyCore;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

@SuppressWarnings("deprecation")
public class GenerationManager implements Listener {

	public static final HashMap<Material, Integer> INT_ADDED = new HashMap<>();
		
	@EventHandler
	public void onWorldInit(WorldInitEvent e) {
		e.getWorld().getPopulators().add(new OrePopulator());

		// e.getWorld().getPopulators().add(new LobbyPopulator());
	}

	public static class OrePopulator extends BlockPopulator {

		private static Generation[] GENERATIONS = new Generation[] { new Generation(55, Material.PRISMARINE, 400, 22),
				new Generation(20, Material.DIAMOND_ORE, 45, 6), new Generation(55, Material.COAL_ORE, 80, 20),
				new Generation(25, Material.REDSTONE_ORE, 45), new Generation(40, Material.IRON_ORE, 60, 15),
				new Generation(25, Material.QUARTZ, 45), new Generation(25, Material.EMERALD_ORE, 40, 6),
				new Generation(25, Material.OBSIDIAN, 60, 4) };

		@Override
		public void populate(World world, Random random, Chunk source) {

			for (Generation gen : GENERATIONS) {
				// for (int i = 0; i < type.length; i++) {
				makeOres(source, random, random.nextInt(16), random.nextInt(gen.getMaxHeight()), random.nextInt(16),
						gen.getAmount(), gen.getMaterial());
				// }
			}
		}

		private void makeOres(Chunk source, Random random, int originX, int originY, int originZ, int amount,
				Material type) {
			for (int i = 0; i < amount; i++) {
				int x = originX + random.nextInt(amount / 2) - amount / 4;
				int y = originY + random.nextInt(amount / 4) - amount / 8;
				int z = originZ + random.nextInt(amount / 2) - amount / 4;
				x &= 0xf;
				z &= 0xf;
				if (y > 127 || y < 0) {
					continue;
				}
				Block b = source.getBlock(x, y, z);
				switch (type) {
				case DIAMOND_ORE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT) {
						b.setType(type, false);
						b.getLocation().add(1, 0, 0).getBlock().setType(type, false);
						b.getLocation().add(1, 0, 1).getBlock().setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting Diamond");
					}
					break;
				case COAL_ORE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT
							|| b.getType() == Material.GRAVEL) {
						b.setType(type, false);
						b.getLocation().add(1, 0, 0).getBlock().setType(type, false);
						b.getLocation().add(1, 0, 1).getBlock().setType(type, false);
						b.getLocation().add(0, 0, 1).getBlock().setType(type, false);
						b.getLocation().add(0, -1, 0).getBlock().setType(type, false);
						b.getLocation().add(0, -1, 1).getBlock().setType(type, false);
						b.getLocation().add(1, -1, 0).getBlock().setType(type, false);
						if(random.nextBoolean())
							b.getLocation().add(1, -1, 1).getBlock().setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting Coal");
					}
					break;
				case PRISMARINE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT || b.getType() == Material.GRAVEL
							|| b.getType() == Material.WATER) {
						b.setType(type, false);
						if (random.nextBoolean())
							b.getLocation().add(1, 0, 0).getBlock().setType(type, false);
						if (random.nextBoolean())
							b.getLocation().add(0, 0, 1).getBlock().setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting Prismarine");
					}
					break;
				case REDSTONE_ORE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT) {
						b.setType(type, false);
						b.getLocation().add(1, 0, 0).getBlock().setType(type, false);
						b.getLocation().add(1, 0, 1).getBlock().setType(type, false);
						if (random.nextBoolean())
							b.getLocation().add(0, 0, 1).getBlock().setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting redstone");
					}
					break;
				case IRON_ORE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT
							|| b.getType() == Material.GRAVEL) {
						b.setType(type, false);
						b.getLocation().add(1, 0, 0).getBlock().setType(type, false);
						b.getLocation().add(1, 0, 1).getBlock().setType(type, false);
						b.getLocation().add(0, 0, 1).getBlock().setType(type, false);
						b.getLocation().add(0, 1, 0).getBlock().setType(type, false);
						b.getLocation().add(0, 1, 1).getBlock().setType(type, false);
						b.getLocation().add(1, 1, 0).getBlock().setType(type, false);
						if (random.nextBoolean())
							b.getLocation().add(1, 1, 1).getBlock().setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting iron");
					}
					break;
				case NETHER_QUARTZ_ORE:
					if (b.getType() == Material.STONE || b.getType() == Material.DIRT) {
						b.setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting quartz");
					}
					break;
				case OBSIDIAN:
					if (b.getType() == Material.STONE) {
						b.setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting Obisidian");
					}
					break;
				default:
					if (b.getType() == Material.STONE) {
						b.setType(type, false);
						//pl.getLogger().info("[GENERATOR] setting default " + b.getType());
						continue;
					}
				}
				if(INT_ADDED.containsKey(type))
					INT_ADDED.put(type, INT_ADDED.get(type) + 1);
				else INT_ADDED.put(type, 1);
			}
		}
	}

	public static class LobbyPopulator extends BlockPopulator {

		public String SchematicName = "Lobby.schematic";

		public LuckyCore pl = LuckyCore.INSTANCE;

		@Override
		public void populate(World world, Random random, Chunk chunk) {

			if (chunk.getX() == 0 && chunk.getZ() == 0) {
				try {
					EditSession es = new EditSession(new BukkitWorld(world), 999999999);
					CuboidClipboard cc = CuboidClipboard
							.loadSchematic(new File(GameAPI.GAME_PROVIDER.getDataFolder() + "/hub.schematic"));
					cc.paste(es, new Vector(0, 140, 0), false);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
				/*try {
					InputStream is = pl.getClass().getClassLoader().getResourceAsStream(SchematicName);
					SchematicsManager man = new SchematicsManager();
					man.loadGzipedSchematic(is);
					int wd = man.getWidth();
					int hg = man.getHeight();
					int lg = man.getLength();
					int starty = 140;
					int endy = starty + hg;

					for (int x = 0; x < wd; x++) {
						for (int z = 0; z < lg; z++) {
							int realX = x + chunk.getX() * 16;
							int realZ = z + chunk.getZ() * 16;
							for (int y = starty; y <= endy && y < 256; y++) {
								int rely = y - starty;
								int id = man.getBlockIdAt(x, rely, z);
								byte data = man.getMetadataAt(x, rely, z);
								if (id == -103)
									world.getBlockAt(realX, y, realZ).setTypeIdAndData(153, data, true);
								if (id > -1 && world.getBlockAt(realX, y, realZ) != null)
									world.getBlockAt(realX, y, realZ).setTypeIdAndData(id, data, true);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Un probleme est survenu, la schematic n'est pas load");
				}*/
			}
		}
	}

	public static class Generation {

		protected final int amount, maxHeight, minAmount;
		protected final Material m;

		public Generation(int amount, Material m, int maxHeight) {
			this(amount, m, maxHeight, 5);
		}
		
		public Generation(int amount, Material m, int maxHeight, int minAmount) {
			this.amount = amount;
			this.minAmount = minAmount;
			this.m = m;
			this.maxHeight = maxHeight;
		}

		public int getAmount() {
			return amount;
		}

		public Material getMaterial() {
			return m;
		}

		public int getMaxHeight() {
			return maxHeight;
		}
		
		public int getMinAmount() {
			return minAmount;
		}
	}
}

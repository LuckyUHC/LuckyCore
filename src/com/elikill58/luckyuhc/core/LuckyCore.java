package com.elikill58.luckyuhc.core;

import static com.elikill58.api.game.GameAPI.GAME_PROVIDER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.elikill58.api.Messages;
import com.elikill58.api.PlayerData;
import com.elikill58.api.game.Game;
import com.elikill58.api.game.GameAPI;
import com.elikill58.api.game.phase.Phase;
import com.elikill58.api.utils.PacketUtils;
import com.elikill58.api.utils.Utils;
import com.elikill58.luckyblocks.LuckyBlocks;
import com.elikill58.luckyuhc.core.generator.Generator;
import com.elikill58.luckyuhc.core.listeners.CraftManager;
import com.elikill58.luckyuhc.core.listeners.DropsManager;
import com.elikill58.luckyuhc.core.listeners.GenerationBlocksManager;
import com.elikill58.luckyuhc.core.listeners.GenerationManager;
import com.elikill58.luckyuhc.core.listeners.ItemManager;
import com.elikill58.luckyuhc.core.listeners.LootManager;
import com.elikill58.luckyuhc.core.listeners.OthersEvents;
import com.elikill58.luckyuhc.core.listeners.PickManager;
import com.google.common.io.ByteStreams;

@SuppressWarnings("deprecation")
public class LuckyCore {

	public static Generator generator;
	public static LuckyUHCProperties properties = new LuckyUHCProperties();
	public static HashMap<Player, Integer> FOUND = new HashMap<>();
	public static final List<Location> FARMING_LOCS = new ArrayList<>(), FIGHT_LOCS = new ArrayList<>();

	private static BukkitTask loadingTask = null;
	
	private static int getIdOfBiomeBase(String name) throws Exception {
		Object biomeBaseEnum = getBiomeBase(name);
		return biomeBaseEnum.getClass().getDeclaredField("id").getInt(biomeBaseEnum);
	}
	
	private static Object getBiomeBase(String name) throws Exception {
		Class<?> biomeBaseClass = PacketUtils.getNmsClass("BiomeBase");
		return biomeBaseClass.getDeclaredField(name).get(biomeBaseClass);
	}
	
	public static void onLoad() {
		try {
			File worldContainer = Bukkit.getServer().getWorldContainer();
			LuckyUtils.deleteWorld(new File(worldContainer, "world"));
			LuckyUtils.deleteWorld(new File(worldContainer, "world_nether"));
			LuckyUtils.deleteWorld(new File(worldContainer, "world_the_end"));
			
			Class<?> biomeBaseClass = PacketUtils.getNmsClass("BiomeBase");
			Field biomesField = biomeBaseClass.getDeclaredField("biomes");

			biomesField.setAccessible(true);
			Object[] biomes = (Object[]) biomesField.get(null);
			biomes[getIdOfBiomeBase("DEEP_OCEAN")] = getBiomeBase("PLAINS");
			biomes[getIdOfBiomeBase("OCEAN")] = getBiomeBase("FOREST");
			biomes[getIdOfBiomeBase("ICE_MOUNTAINS")] = getBiomeBase("JUNGLE");
			biomes[getIdOfBiomeBase("ICE_PLAINS")] = getBiomeBase("PLAINS");
			biomesField.setAccessible(true);
			biomesField.set(null, biomes);
			GAME_PROVIDER.getLogger().info("Chunk modifié avec succés.");
		} catch (Exception ignore) {}
	}
	
	@SuppressWarnings("unchecked")
	public static void onEnable() {
		((Game<LuckyUHCProperties>) GameAPI.ACTIVE_GAME).properties = properties;
		properties.maxPlayers(20).maxSpecs(20);
		GAME_PROVIDER.getDataFolder().mkdirs();
		try (InputStream in = GAME_PROVIDER.getResource("Lobby.schematic");
				OutputStream out = new FileOutputStream(new File(GAME_PROVIDER.getDataFolder(), "hub.schematic"))) {
			ByteStreams.copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadingTask = Bukkit.getScheduler().runTaskTimer(GAME_PROVIDER, new Runnable() {
			@Override
			public void run() {
				World w = LuckyCore.getWorld();
				if(w != null) {
					Bukkit.getScheduler().runTaskLater(GAME_PROVIDER, new Runnable() {
						@Override
						public void run() {
							Generator.getGenerator().spawnSchematic(new Location(w, 0, 140, 0), "/hub.schematic");
							/*try {
								EditSession es = new EditSession(new BukkitWorld(w), 999999999);
								CuboidClipboard cc = CuboidClipboard
										.loadSchematic(new File(GAME_PROVIDER.getDataFolder(), "hub.schematic"));
								cc.paste(es, new Vector(0, 140, 0), false);
							} catch (Exception exc) {
								exc.printStackTrace();
							}*/
						}
					}, 20);
					w.setGameRuleValue("naturalRegeneration", "false");
					w.setGameRuleValue("doDaylightCycle", "false");
					w.getWorldBorder().setSize(2000);
					LuckyCore.loadLocations();
					LuckyUtils.genLocAround(new Location(w, 0, 100, 0));
					/*GenerationManager.INT_ADDED.forEach((m, i) -> {
						GAME_PROVIDER.getLogger().info("[GENERATOR] setting > " + i + " " + m.name());
					});*/
					loadingTask.cancel();
				}
			}
		}, 1, 1);
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GenerationManager(), GAME_PROVIDER);
		pm.registerEvents(new GenerationBlocksManager(), GAME_PROVIDER);
		pm.registerEvents(new CraftManager(), GAME_PROVIDER);
		pm.registerEvents(new LootManager(), GAME_PROVIDER);
		pm.registerEvents(new ItemManager(), GAME_PROVIDER);
		pm.registerEvents(new OthersEvents(), GAME_PROVIDER);
		pm.registerEvents(new DropsManager(), GAME_PROVIDER);
		pm.registerEvents(new PickManager(), GAME_PROVIDER);
		
		LuckyBlocks.init();
	}

	public static void loadLocations() {
		FileConfiguration config = GAME_PROVIDER.getConfig();
		if (config.get("spawns.farming") != null) {
			ConfigurationSection cs = config.getConfigurationSection("spawns.farming");
			for (String key : cs.getKeys(false))
				FARMING_LOCS.add(
						new Location(getWorld(), cs.getDouble(key + ".x"), cs.getDouble(key + ".y"), cs.getDouble(key + ".z")));
		}
		if (config.get("spawns.fight") != null) {
			ConfigurationSection cs = config.getConfigurationSection("spawns.fight");
			for (String key : cs.getKeys(false))
				FIGHT_LOCS.add(
						new Location(getWorld(), cs.getDouble(key + ".x"), cs.getDouble(key + ".y"), cs.getDouble(key + ".z")));
		}
	}

	/*public static void deleteWorld(World w) {
		Bukkit.unloadWorld(Bukkit.getWorld(WORLD_NAME), false);
		new File(WORLD_NAME + "\\session.lock").deleteOnExit();
		Utils.deleteWorld(new File(WORLD_NAME));
	}*/

	public static void configure(Game<?> gameClass, Phase lobbyPhase) {
		gameClass.properties.canDrop(true).canModifyInventories(true)
				.disableNaturalSpawning(false).disableFallDamages(true).pvp(false);
	}

	public static void end(List<Player> winner) {
		if (winner.size() == 0) {
			Bukkit.broadcastMessage("Il n'y a aucun vainqueur. There isn't any winner.");
			return;
		}
		String winnersName = winner.get(0).getName();
		for (Player p : winner)
			if (!p.getName().equalsIgnoreCase(winnersName))
				winnersName += ", " + p.getName();
		if (properties.winCoins) {
			for (Player p : winner)
				PlayerData.getPlayerData(p).addCoins(20);
		}
		for (Player p : winner) {
			Messages.sendMessage(p, "game.end.split", "%prefix%", GameAPI.ACTIVE_GAME.prefix());
			p.sendMessage("");
			if (!properties.winCoins)
				Messages.sendMessage(p, "game.end.earn_off");
			Messages.sendMessage(p, "game.end.win_by", "%name%", winnersName);
			p.sendMessage("");
			Messages.sendMessage(p, "game.end.split", "%prefix%", GameAPI.ACTIVE_GAME.prefix());
		}
		Bukkit.getScheduler().runTaskLater(GAME_PROVIDER, new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers())
					Utils.tpToServer(p, "LOBBY");
				Bukkit.shutdown();
			}
		}, 100);
	}

	public static int getLuckyblockFound(Player p) {
		if (FOUND.containsKey(p))
			return FOUND.get(p);
		else
			return 0;
	}

	public static void addLuckyblockFound(Player p) {
		if (FOUND.containsKey(p))
			FOUND.put(p, FOUND.get(p) + 1);
		else
			FOUND.put(p, 1);
	}

	public static void deletelobby() {
		Location lobbyloc = new Location(getWorld(), 0, 130, 0);
		Location lobbyloc2 = new Location(getWorld(), 40, 250, 40);

		int minX = Math.min(lobbyloc.getBlockX(), lobbyloc2.getBlockX());
		int minY = Math.min(lobbyloc.getBlockY(), lobbyloc2.getBlockY());
		int minZ = Math.min(lobbyloc.getBlockZ(), lobbyloc2.getBlockZ());

		int maxX = Math.max(lobbyloc.getBlockX(), lobbyloc2.getBlockX());
		int maxY = Math.max(lobbyloc.getBlockY(), lobbyloc2.getBlockY());
		int maxZ = Math.max(lobbyloc.getBlockZ(), lobbyloc2.getBlockZ());

		for (int x = minX; x <= maxX; x++)
			for (int y = minY; y <= maxY; y++)
				for (int z = minZ; z <= maxZ; z++)
					getWorld().getBlockAt(x, y, z).setType(Material.AIR);
	}

	/*public static void pregenerate() {
		if(world != null)
			return;
		World w = Bukkit.getWorld(WORLD_NAME);
		if(w != null) {
			Utils.deleteWorld(world);
		}
		List<File> list = new ArrayList<>();
		for(File f : new File(new File("").getAbsolutePath() + "/").listFiles()) {
			if(!f.isDirectory())
				continue;
			if(f.getName().startsWith("world-") && f.getName() != WORLD_NAME)
				list.add(f);
		}
		if(list.size() == 0) {
			throw new IllegalArgumentException("No generated world found. Please generate 1000x1000 world's and paste in here: " + new File("").getPath() + ". Name us 'world-XX'");
		}
		File chooseWorld = list.get(new Random().nextInt(list.size() - 1));
		try {
			INSTANCE.getLogger().info("Well deleted: " + (new File(WORLD_NAME).delete()));
			Files.copy(chooseWorld.toPath(), new File(WORLD_NAME).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		world = Bukkit.getWorld(WORLD_NAME);
		if(world == null)
			world = Bukkit.createWorld(WorldCreator.name(WORLD_NAME));
		world.getWorldBorder().setSize(2000);
		GameAPI.startPhase(lobbyPhase);
	}*/
	
	public static World getWorld() {
		return Bukkit.getWorlds().get(0);
	}
	
	public static void manageDeathMessage(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(e.getEntityType().equals(EntityType.PLAYER) && p.getKiller() != null)
			GameAPI.broadcast("death.kill_by", "%death%", p.getName(), "%killer%", p.getKiller().getName());
		else {
			switch (e.getEntityType()) {
			case ARROW:
			case CREEPER:
			case ENDERMAN:
			case FALLING_BLOCK:
			case GIANT:
			case IRON_GOLEM:
			case LIGHTNING:
			case PRIMED_TNT:
			case SKELETON:
			case SLIME:
			case SPIDER:
			case WITCH:
			case WITHER:
			case WITHER_SKULL:
			case WOLF:
			case ZOMBIE:
				GameAPI.broadcast("death.message." + e.getEntityType().name().toLowerCase(), "%death%", p.getName());
				break;
			default:
				GameAPI.broadcast("death.kill_by", "%death%", p.getName(), "%killer%", "death.no_killer_name");
				break;
			}
		}
	}
}

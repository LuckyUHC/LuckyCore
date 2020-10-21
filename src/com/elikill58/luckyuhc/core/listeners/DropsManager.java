package com.elikill58.luckyuhc.core.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.elikill58.luckyblocks.LuckyBlocks;
import com.elikill58.luckyuhc.core.LuckyCore;
import com.elikill58.luckyuhc.core.LuckyUtils;
import com.elikill58.luckyuhc.core.phases.FarmingPhase;

import fr.zonefun.gameapi.GameAPI;

public class DropsManager implements Listener {

	private static final HashMap<Material, Drops> DROPS = new HashMap<>();
	public static final List<Location> LOC_BIBLIO = new ArrayList<>();
	public static boolean isFirst = false;
	
	public static void addDrop(Material m, Drops drop) {
		DROPS.put(m, drop);
	}
	
	public static void removeDrop(Material m) {
		if(DROPS.containsKey(m))
			DROPS.remove(m);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		Material m = b.getType();
		if(GameAPI.ACTIVE_PHASE.equals(LuckyCore.game.startingPhase()) && !FarmingPhase.timer.isRunning()) {
			e.setCancelled(true);
			return;
		}
		if(DROPS.containsKey(m))
			DROPS.get(m).applyEffect(e);
		if (m.equals(Material.PRISMARINE)) {
            if (isFirst) {
                GameAPI.broadcast("luckyblock.break.isfirst", "%name%", e.getPlayer().getName());
                isFirst = false;
            }
            e.setCancelled(true);
            b.setType(Material.AIR);
			LuckyCore.addLuckyblockFound(e.getPlayer());
			LuckyBlocks.runRandomLuckyBlock(e, LuckyCore.properties.luck);
		} else if (m.equals(Material.LOG) || m.equals(Material.LOG_2)) {
			logDetele(e);
		} else if(m.equals(Material.BOOKSHELF)) {
            if(DropsManager.LOC_BIBLIO.contains(b.getLocation())) {
                e.setCancelled(true);
                b.setType(Material.AIR);
                DropsManager.LOC_BIBLIO.remove(b.getLocation());
            }
		}
	}
	
	private void logDetele(BlockBreakEvent e) {
        e.getPlayer().giveExp(10);
        List<Block> blist = new ArrayList<>();
        checkLeaves(e.getBlock());
        blist.add(e.getBlock());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < blist.size(); i++) {
                    Block b = blist.get(i);
                    if (b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
                        for (ItemStack item : b.getDrops())
                            b.getWorld().dropItemNaturally(b.getLocation(), item);

                        b.setType(Material.AIR);
                        checkLeaves(b);
                    }
                    for (BlockFace face : BlockFace.values()) {
                        if (b.getRelative(face).getType() == Material.LOG
                                || b.getRelative(face).getType() == Material.LOG_2) {
                            blist.add(b.getRelative(face));
                        }
                    }

                    blist.remove(b);

                    if (blist.size() == 0) {
                        cancel();
                    }
                    for (Entity et : e.getPlayer().getWorld().getEntities()) {
                        if (et instanceof Item) {
                            Item it = (Item) et;
                            if (it.getItemStack().getType() == Material.SAPLING
                                    || it.getItemStack().getType() == Material.APPLE) {
                                it.setItemStack(new ItemStack(Material.GOLDEN_APPLE));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(LuckyCore.INSTANCE, 0, 1);
    }

    @SuppressWarnings("deprecation")
    private void breakLeaf(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        byte data = block.getData();

        if ((data & 4) == 4) {
            return;
        }

        byte range = 4;
        byte max = 32;
        int[] blocks = new int[max * max * max];
        int off = range + 1;
        int mul = max * max;
        int div = max / 2;

        if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
            int offX;
            int offY;
            int offZ;
            int type;

            for (offX = -range; offX <= range; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        Material mat = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
                        if ((mat == Material.LEAVES || mat == Material.LEAVES_2))
                            type = Material.LEAVES.getId();
                        else if ((mat == Material.LOG || mat == Material.LOG_2))
                            type = Material.LOG.getId();
                        blocks[(offX + div) * mul + (offY + div) * max + offZ
                                + div] = ((mat == Material.LOG || mat == Material.LOG_2) ? 0
                                : ((mat == Material.LEAVES || mat == Material.LEAVES_2) ? -2 : -1));
                    }
                }
            }

            for (offX = 1; offX <= 4; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        for (type = -range; type <= range; type++) {
                            if (blocks[(offY + div) * mul + (offZ + div) * max + type + div] == offX - 1) {
                                if (blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] == -2)
                                    blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] = offX;
                                if (blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] == -2)
                                    blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] = offX;
                            }
                        }
                    }
                }
            }
        }

        if (blocks[div * mul + div * max + div] < 0) {
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(new LeavesDecayEvent(block));
            if (event.isCancelled())
                return;

            block.breakNaturally();

            if (10 > new Random().nextInt(100)) {
                world.playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES.getId());
            }
        }
    }

    public boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY >= 0 && minY < world.getMaxHeight()) {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;

            for (int x = minX; x <= maxX; x++)
                for (int z = minZ; z <= maxZ; z++)
                    if (!world.isChunkLoaded(x, z))
                        return false;
            return true;
        }

        return false;
    }

    private void checkLeaves(Block block) {
        Location loc = block.getLocation();
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final int range = 4;
        final int off = range + 1;

        if (!validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
            return;

        Bukkit.getServer().getScheduler().runTask(LuckyCore.INSTANCE, new Runnable() {
            public void run() {
                for (int offX = -range; offX <= range; offX++)
                    for (int offY = -range; offY <= range; offY++)
                        for (int offZ = -range; offZ <= range; offZ++)
                            if ((world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES
                                    || world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES_2))
                                breakLeaf(world, x + offX, y + offY, z + offZ);
            }
        });
    }
    
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		Material m = b.getType();
		Player p = e.getPlayer();
		if (m.equals(Material.ENCHANTMENT_TABLE)) {
			LOC_BIBLIO.add(b.getLocation().clone());
			int nb = LuckyCore.properties.lvlBiblio;
			for (int i = 0; i != nb; i++)
				setBiblio(b.getLocation().clone().add(0, i, 0));
			Location ll = p.getLocation().clone().add(0, nb, 0);
			ll.getBlock().setType(Material.AIR);
			ll.add(0, 1, 0).getBlock().setType(Material.AIR);
			p.teleport(p.getLocation().clone().add(0, nb, 0));
		}
	}
	
	public void setBiblio(Location loc) {
		Location l = loc;
		l.add(0, 0, 2).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, -1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, -1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, -1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, -1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(-1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(-1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(-1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(-1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, 1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, 1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, 1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(0, 0, 1).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
		l.add(1, 0, 0).getBlock().setType(Material.BOOKSHELF);
		LOC_BIBLIO.add(l.clone());
	}
	
	public static class Drops {

		private Material m = Material.AIR;
		private int amount, xp = 1;
		private byte b = -1;
		private String name;
		private boolean cancel = true;

		public Drops(Material m, int amount) {
			this.m = m;
			this.amount = amount;
		}

		public Drops(Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.b = b;
		}

		public Drops(String name, Material m, int amount) {
			this.m = m;
			this.amount = amount;
			this.name = name;
		}

		public Drops(String name, Material m, int amount, byte b) {
			this.m = m;
			this.amount = amount;
			this.name = name;
			this.b = b;
		}

		public Drops setCancelled(boolean b) {
			cancel = b;
			return this;
		}
		
		public Drops setExp(int xp) {
			this.xp = xp;
			return this;
		}
		
		public void applyEffect(BlockBreakEvent e) {
			if(cancel) {
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
			Player p = e.getPlayer();
			p.giveExp(xp);
			if(m != Material.AIR) {
				ItemStack item = null;
				if (b != -1)
					if (name != null)
						item = LuckyUtils.createItem(m, name, amount, b);
					else
						item = new ItemStack(m, amount, b);
				else if (name != null)
					item = LuckyUtils.createItem(m, name, amount);
				else
					item = new ItemStack(m, amount);
				p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
			}
		}
	}
}

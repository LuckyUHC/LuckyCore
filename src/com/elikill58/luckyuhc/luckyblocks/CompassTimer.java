package com.elikill58.luckyuhc.luckyblocks;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CompassTimer extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getItemInHand().getType().equals(Material.COMPASS)) {
				ItemStack it = p.getItemInHand();
				ItemMeta im = it.getItemMeta();
				Player cible = getNearestPlayer(p);
				if (cible != null) {
					DecimalFormat df = new DecimalFormat("0.0");
					double dist = getDistance(p, cible);
					String arrow = getArrow(p, cible);
					im.setDisplayName(
							ChatColor.YELLOW + df.format(dist) + " blocks, " + arrow + " : " + cible.getName());
				} else
					im.setDisplayName("Il n'y a pas assez de joueur");
				it.setItemMeta(im);
				p.setItemInHand(it);
			}
		}
	}

	private Player getNearestPlayer(Player p) {
		Player retu = null;
		double distNear = 4000;
		Location location = p.getLocation();
		for (Player cible : Bukkit.getOnlinePlayers()) {
			if (cible == p)
				continue;
			if (cible.getGameMode().equals(GameMode.SURVIVAL)) {
				Location location2 = cible.getLocation();
				double dist = location.distance(location2);
				if (dist < distNear) {
					retu = cible;
					distNear = dist;
				}
			} else
				continue;
		}

		return retu;
	}

	private double getDistance(Player p1, Player p2) {
		double dist = p2.getLocation().distance(p1.getLocation());
		return dist;
	}

	private double getAngle(Player p1, Player p2) {

		Vector a = p1.getLocation().toVector().subtract(p2.getLocation().toVector()).normalize();
		Vector b = p1.getEyeLocation().getDirection();
		double angle = Math.acos(a.dot(b));
		angle = Math.toDegrees(angle);
		return angle;
	}

	private String getArrow(Player p1, Player p2) {
		double angle = getAngle(p1, p2);
		if (0 <= angle && angle < 22.5)
			return "\u2193"; // bas
		else if (22.5 <= angle && angle < 67.5)
			return "\u2198"; // bas droite
		else if (67.5 <= angle && angle < 112.5)
			return "\u2192"; // droite
		else if (112.5 <= angle && angle < 157.5)
			return "\u2196"; // haut droite
		else if (157.5 <= angle && angle < 202.5)
			return "\u2191"; // haut
		else if (202.5 <= angle && angle < 247.5)
			return "\u2199"; // bas gauche
		else if (247.5 <= angle && angle < 292.5)
			return "\u2190"; // gauche
		else if (292.5 <= angle && angle < 337.5)
			return "\u2197"; // haut gauche
		else if (337.5 <= angle && angle < 360.0)
			return "\u2193"; // bas
		else
			return "?";
	}
}

package com.clashwars.cwclasses.utils;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CustomEffects {

	static Random rand = new Random();
	
	public static void Cloud(Location location, int radius) {
		if (radius > 10) {
			radius = 10;
		}

		World w = location.getWorld();
		int cx = location.getBlockX();
		int cy = location.getBlockY();
		int cz = location.getBlockZ();

		Block b;
		for (int x = cx - radius; x <= cx + radius; x++) {
			for (int z = cz - radius; z <= cz + radius; z++) {
				if (inRange(x, z, cx, cz, radius)) {
					b = w.getBlockAt(x, cy, z);
					if (isPathable(b.getType())) {
						smoke(w, b, radius);
					} else {
						b = b.getRelative(0, -1, 0);
						if (isPathable(b.getType())) {
							smoke(w, b, radius);
						} else {
							b = b.getRelative(0, 2, 0);
							if (isPathable(b.getType())) {
								smoke(w, b, radius);
							}
						}
					}
				}
			}
		}
	}
	
	public static void BigSmoke(Location loc) {
		World world = loc.getWorld();
		int lx = loc.getBlockX();
		int ly = loc.getBlockY();
		int lz = loc.getBlockZ();
		Location location;

		for (int x = lx - 1; x <= lx + 1; x++) {
			for (int y = ly; y <= ly + 1; y++) {
				for (int z = lz - 1; z <= lz + 1; z++) {
					for (int i = 0; i <= 8; i += 2) {
						location = new Location(world, x, y, z);
						world.playEffect(location, Effect.SMOKE, i);
					}
				}
			}
		}
	}
	
	
	public static void Splash(Location location, String param) {
		int pot = 0;
		if (param != null && !param.isEmpty()) {
			try {
				pot = Integer.parseInt(param);
			} catch (NumberFormatException e) {
			}
		}
		location.getWorld().playEffect(location, Effect.POTION_BREAK, pot);
	}
	
	public static void playSignal(Location loc) {
		loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 0);
	}

	public static void playExplosion(Location loc) {
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 1F, false, false);
	}

	public static void playLightning(Location loc) {
		loc.getWorld().strikeLightningEffect(loc);
	}
	
	
	
	
	
	
	
	private  static void smoke(World w, Block b, int r) {
		Location loc = b.getLocation();
		if (r <= 5) {
			for (int i = 0; i <= 8; i += 2) {
				w.playEffect(loc, Effect.SMOKE, i);
			}
		} else if (r <= 8) {
			w.playEffect(loc, Effect.SMOKE, rand.nextInt(9));
			w.playEffect(loc, Effect.SMOKE, rand.nextInt(9));
		} else {
			w.playEffect(loc, Effect.SMOKE, rand.nextInt(9));
		}
	}
	
	
	private static boolean inRange(int x1, int z1, int x2, int z2, int r) {
		return sq(x1 - x2) + sq(z1 - z2) < sq(r);
	}
	
	private static int sq(int v) {
		return v * v;
	}
	
	private static boolean isPathable(Material material) {
		return material == Material.AIR || material == Material.SAPLING || material == Material.WATER || material == Material.STATIONARY_WATER
				|| material == Material.POWERED_RAIL || material == Material.DETECTOR_RAIL || material == Material.LONG_GRASS
				|| material == Material.DEAD_BUSH || material == Material.YELLOW_FLOWER || material == Material.RED_ROSE
				|| material == Material.BROWN_MUSHROOM || material == Material.RED_MUSHROOM || material == Material.TORCH
				|| material == Material.FIRE || material == Material.REDSTONE_WIRE || material == Material.CROPS || material == Material.SIGN_POST
				|| material == Material.LADDER || material == Material.RAILS || material == Material.WALL_SIGN || material == Material.LEVER
				|| material == Material.STONE_PLATE || material == Material.WOOD_PLATE || material == Material.REDSTONE_TORCH_OFF
				|| material == Material.REDSTONE_TORCH_ON || material == Material.STONE_BUTTON || material == Material.SNOW
				|| material == Material.SUGAR_CANE_BLOCK || material == Material.VINE || material == Material.WATER_LILY
				|| material == Material.NETHER_STALK;
	}
}

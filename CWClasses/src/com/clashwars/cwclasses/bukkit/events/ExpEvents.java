package com.clashwars.cwclasses.bukkit.events;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.Util;

public class ExpEvents implements Listener {
	
	private CWClasses cwc;
	
	public ExpEvents(CWClasses cwc) {
		this.cwc = cwc;
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (cwc.spawnerMobs.contains(event.getEntity().getUniqueId())) {
			return;
		}
		Player damaged = null;
		if (event.getEntity() instanceof Player) {
			damaged = (Player)event.getEntity();
		}
		
		Player damager = null;
		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
			
			//Hit a player or mob with sword [Warrior]
			Material mat = damager.getItemInHand().getType();
			if (mat == Material.DIAMOND_SWORD || mat == Material.IRON_SWORD || mat == Material.STONE_SWORD || mat == Material.WOOD_SWORD || mat == Material.GOLD_SWORD) {
				if (damaged == null) {
					double xp = (double)Math.min(Math.max(event.getDamage() / 14, 0.1), 1.5);
					cwc.getPlayerManager().addExp(damager, xp, ClassType.WARRIOR);
				} else {
					double xp = (double)Math.min(Math.max(event.getDamage() / 4, 1.0), 5.0);
					cwc.getPlayerManager().addExp(damager, xp, ClassType.WARRIOR);
				}
			}
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			ProjectileSource projSrc = proj.getShooter();
			if (projSrc instanceof Player) {
				damager = (Player) projSrc;
				
				//Shoot a player or mob with bow [Archer]
				if (damaged == null) {
					double xp = (double)Math.min(Math.max(event.getDamage() / 8, 0.5), 2.5);
					cwc.getPlayerManager().addExp(damager, xp, ClassType.ARCHER);
				} else {
					double xp = (double)Math.min(Math.max(event.getDamage() / 2.5, 1.0), 8.0);
					cwc.getPlayerManager().addExp(damager, xp, ClassType.ARCHER);
				}
			}
		}
		
		//Blocking damage [Guardian]
		if (damaged != null) {
			if (damaged.isBlocking() && Util.canPvP(damaged)) {
				//XP values are doubled cuz this is called twice for some reason.
				if (damager == null) {
					double xp = (double)Math.min(Math.max(event.getDamage() / 40, 0.1), 0.5);
					cwc.getPlayerManager().addExp(damaged, xp, ClassType.GUARDIAN);
				} else {
					double xp = (double)Math.min(Math.max(event.getDamage() / 8, 0.5), 2.5);
					cwc.getPlayerManager().addExp(damaged, xp, ClassType.GUARDIAN);
				}
			}
		}
	}
	
	@EventHandler
	public void death(EntityDeathEvent event) {
		if (cwc.spawnerMobs.contains(event.getEntity().getUniqueId())) {
			cwc.spawnerMobs.remove(event.getEntity().getUniqueId());
			return;
		}
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		//Kill a player or mob. [Warrior]
		LivingEntity entity = (LivingEntity)event.getEntity();
		if (entity != null && entity.getKiller() != null && entity.getKiller() instanceof Player) {
			Player killer = (Player)entity.getKiller();
			if (entity instanceof Player) {
				cwc.getPlayerManager().addExp(killer, 25, ClassType.WARRIOR);
			} else {
				double xp = 1.0;
				switch (entity.getType()) {
					case CHICKEN:
						xp = 0.5;
						break;
					case PIG:
					case COW:
					case MUSHROOM_COW:
					case SHEEP:
						xp = 1.0;
						break;
					case WOLF:
					case OCELOT:
					case SQUID:
					case BAT:
					case ZOMBIE:
					case SILVERFISH:
					case CAVE_SPIDER:
					case HORSE:
					case MAGMA_CUBE:
					case SLIME:
						xp = 3.0;
						break;
					case SKELETON:
					case SPIDER:
					case CREEPER:
					case BLAZE:
					case ENDERMAN:
					case WITCH:
					case VILLAGER:
						xp = 4.0;
						break;
					case GHAST:
						xp = 5.0;
						break;
					case WITHER:
						xp = 300;
						break;
					case ENDER_DRAGON:
						xp = 200;
						break;
					default:
						break;
				}
				cwc.getPlayerManager().addExp(killer, xp, ClassType.WARRIOR);
			}
		}
	}
}

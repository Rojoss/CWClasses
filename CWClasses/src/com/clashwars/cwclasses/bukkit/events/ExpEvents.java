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
					//TODO: Check for spawner mob.
					cwc.getPlayerManager().addExp(damager, 1, ClassType.WARRIOR);
				} else {
					cwc.getPlayerManager().addExp(damager, 5, ClassType.WARRIOR);
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
					//TODO: Check for spawner mob.
					cwc.getPlayerManager().addExp(damager, 1, ClassType.ARCHER);
				} else {
					cwc.getPlayerManager().addExp(damager, 5, ClassType.ARCHER);
				}
			}
		}
		
		//Blocking damage [Guardian]
		if (damaged != null) {
			if (damaged.isBlocking()) {
				if (damager == null) {
					cwc.getPlayerManager().addExp(damaged, 1, ClassType.GUARDIAN);
				} else {
					cwc.getPlayerManager().addExp(damaged, 5, ClassType.GUARDIAN);
				}
			}
		}
	}
	
	@EventHandler
	public void death(EntityDeathEvent event) {
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
				//TODO: Different values for different mobs.
				//TODO: Check for spawner mobs.
				cwc.getPlayerManager().addExp(killer, 3, ClassType.WARRIOR);
			}
		}
	}
}

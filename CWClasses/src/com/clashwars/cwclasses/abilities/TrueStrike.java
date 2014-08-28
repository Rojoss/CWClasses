package com.clashwars.cwclasses.abilities;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.utils.Util;

public class TrueStrike implements AbilityClass,Listener {

	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	
	public TrueStrike() {
		scales.put("chance", new Scalable(0, 50, 1, 20));
	}
	
	
	@Override
	public AbilityType getType() {
		return AbilityType.TRUESTRIKE;
	}
	@Override
	public String getDescription(int level) {
		return "&7Arrows have &a" + scales.get("chance").getValueAtLevel(level) + "% &7chance to deal 50% extra damage.";
	}
	@Override
	public boolean isPassive() {
		return true;
	}
	@Override
	public HashMap<String, Scalable> getScales() {
		return scales;
	}
	
	
	@EventHandler
	public void ArrowHit(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntity() instanceof LivingEntity) {
			return;
		}
		
		LivingEntity hit = (LivingEntity)event.getEntity();
		if (event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			ProjectileSource projSrc = proj.getShooter();
			if (projSrc instanceof Player) {
				CWPlayer cwp = CWClasses.instance.getPlayerManager().getOrCreatePlayer(((Player) projSrc).getUniqueId());
				
				if (cwp.getActiveClass() == getType().getClassType() && cwp.getExpClass().getLevel() >= getLevel()) {
					int percentage = scales.get("chance").getValueAtLevel(cwp.getExpClass().getLevel());
					if (Util.checkChance(percentage)) {
						hit.setHealth(hit.getHealth() - (event.getDamage() / 100 * 50));
						if (hit instanceof Player) {
							((Player) hit).sendMessage(Util.integrateColor("&cHit by a " + getType().getColor() + getType().getName()) + " &carrow.");
						}
						cwp.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "! &7Arrow dealth 50% more damage!"));
						//TODO: Sounds
						//TODO: Particles
					}
				}
			}
		}
	}
}

package com.clashwars.cwclasses.abilities;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.ParticleEffect;
import com.clashwars.cwclasses.utils.Util;

public class TrueStrike extends AbilityClass {

	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	
	public TrueStrike() {
		scales.put("chance", new Scalable(1, 50, 1, 20));
	}
	
	

	public AbilityType getType() {
		return AbilityType.TRUESTRIKE;
	}

	public String getDescription(int level) {
		return "&7Arrows have &a" + scales.get("chance").getValueAtLevel(level) + "% &7chance to deal 50% extra damage.";
	}

	public boolean isPassive() {
		return true;
	}

	public HashMap<String, Scalable> getScales() {
		return scales;
	}
	
	
	@EventHandler
	public void ArrowHit(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		LivingEntity hit = (LivingEntity)event.getEntity();
		if (event.getDamager() instanceof Arrow) {
			Projectile proj = (Projectile) event.getDamager();
			ProjectileSource projSrc = proj.getShooter();
			if (projSrc instanceof Player) {
				CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(((Player) projSrc).getUniqueId());
				
				if (cwp.getActiveClass() == getType().getClassType() && cwp.getLevel() >= getLevel()) {
					int percentage = scales.get("chance").getValueAtLevel(cwp.getLevel());
					if (Util.checkChance(percentage)) {
						event.setDamage(event.getDamage() + (event.getDamage() / 100 * 50));
						if (hit instanceof Player) {
							((Player) hit).sendMessage(Util.integrateColor("&cHit by a " + getType().getColor() + getType().getName() + " &carrow."));
						}
						cwp.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "! &7Arrow dealth 50% more damage!"));
						cwc.getPlayerManager().addExp(cwp, 5.0, ClassType.ARCHER);
						cwp.getPlayer().playSound(cwp.getPlayer().getLocation(), Sound.ORB_PICKUP, 0.8f, 1.3f);
						ParticleEffect.MAGIC_CRIT.display(hit.getLocation(), 1.0f, 1.5f, 1.0f, 0.01f, 30);
					}
				}
			}
		}
	}
}

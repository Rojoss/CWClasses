package com.clashwars.cwclasses.abilities;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.utils.ParticleEffect;
import com.clashwars.cwclasses.utils.Util;

public class Beserk extends AbilityClass {
	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	
	public Beserk() {
		scales.put("duration", new Scalable(0, 100, 40, 200));
	}
	

	public AbilityType getType() {
		return AbilityType.BESERK;
	}

	public String getDescription(int level) {
		return "&7When killing a player you will get strength &81 &7for &a" + (double)Math.round(scales.get("duration").getValueAtLevel(level) / 20) + " &7seconds.";
	}

	public boolean isPassive() {
		return true;
	}

	public HashMap<String, Scalable> getScales() {
		return scales;
	}
	
	
	@EventHandler
	public void kill(PlayerDeathEvent event) {
		if (event.getEntity() == null || event.getEntity().getKiller() == null) {
			return;
		}
		Player killer = event.getEntity().getKiller();
		CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(killer.getUniqueId());
		
		if (cwp.getActiveClass() == getType().getClassType() && cwp.getLevel() >= getLevel()) {
			int ticks = scales.get("duration").getValueAtLevel(cwp.getLevel());
			killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, ticks, 0), true);
			killer.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "! &7Strength for &a" + (double)Math.round(ticks/20) + " &7seconds!"));
			killer.playSound(killer.getLocation(), Sound.GHAST_DEATH, 0.5f, 0f);
			ParticleEffect.displayIconCrack(cwp.getPlayer().getLocation().add(0,1,0), 372, 0.8f, 1.5f, 0.8f, 0.01f, 50);
			ParticleEffect.RED_DUST.display(cwp.getPlayer().getLocation(), 0.4f, 1.0f, 0.4f, 0.0f, 125);
		}
	}
}

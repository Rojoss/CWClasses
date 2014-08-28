package com.clashwars.cwclasses.abilities;

import java.util.HashMap;

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
import com.clashwars.cwclasses.utils.Util;

public class Beserk implements AbilityClass {
	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	
	@Override
	public void init() {
		scales.put("duration", new Scalable(0, 100, 40, 200));
	}
	
	
	@Override
	public AbilityType getType() {
		return AbilityType.BESERK;
	}
	@Override
	public String getDescription(int level) {
		return "&7When killing a player you will get strength &81 &7for &a" + (double)Math.round(scales.get("duration").getValueAtLevel(level) / 20) + " seconds &7.";
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
	public void kill(PlayerDeathEvent event) {
		if (event.getEntity() == null || event.getEntity().getKiller() == null) {
			return;
		}
		Player killer = event.getEntity().getKiller();
		CWPlayer cwp = CWClasses.instance.getPlayerManager().getOrCreatePlayer(killer.getUniqueId());
		
		if (cwp.getActiveClass() == getType().getClassType() && cwp.getExpClass().getLevel() >= getLevel()) {
			int ticks = scales.get("duration").getValueAtLevel(cwp.getExpClass().getLevel());
			killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, ticks, 1), true);
			killer.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "! &7Strength for &a" + (double)Math.round(ticks/20) + " &7seconds!"));
			//TODO: Sounds
			//TODO: Particles
		}
	}
}

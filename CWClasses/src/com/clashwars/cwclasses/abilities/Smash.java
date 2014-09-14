package com.clashwars.cwclasses.abilities;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
import com.clashwars.cwclasses.utils.CustomEffects;
import com.clashwars.cwclasses.utils.Util;

public class Smash implements AbilityClass {

	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	int radius = 4;
	int yScale = 5;
	
	public Smash() {
		scales.put("force", new Scalable(0, 100, 10, 25));
	}
	
	
	@Override
	public AbilityType getType() {
		return AbilityType.SMASH;
	}
	@Override
	public String getDescription(int level) {
		return "&7When using smash all entities within 4 blocks will be knocked backwards with a force of &a" + scales.get("force").getValueAtLevel(level) + "&7.";
	}
	@Override
	public HashMap<String, Scalable> getScales() {
		return scales;
	}
	
	
	@EventHandler
	public void smash(PlayerInteractEvent event) {
		if (event.isCancelled() || event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null) {
			return;
		}
		if (event.getPlayer().isSneaking() && event.getPlayer().isBlocking()) {
			CWPlayer cwp = CWClasses.instance.getPlayerManager().getOrCreatePlayer(event.getPlayer().getUniqueId());
			if (!Util.canPvP(cwp.getPlayer())) {
				cwp.sendMessage(Util.formatMsg(getType().getColor() + getType().getName() + " &ccan't be used here!"));
				return;
			}
			if (cwp.getActiveClass() == getType().getClassType() && cwp.getLevel() >= getLevel()) {
				//Check cooldown.
				Cooldown cd = cwp.getCDM().getCooldown("smash");
				if (cd != null && cd.onCooldown()) {
					long timeLeft = cwp.getCDM().getCooldown("smash").getTimeLeft();
        			event.getPlayer().sendMessage(Util.formatMsg(getType().getColor() + getType().getName() + " &cis on cooldown! " + Util.getMinSecStr(timeLeft, ChatColor.GRAY, ChatColor.DARK_GRAY)));
        			return;
				}
				
				//Try and push back all nearby entities.
				Vector other,cent,v;
				cent = event.getPlayer().getLocation().toVector();
				float force = scales.get("force").getValueAtLevel(cwp.getLevel()) / 10;
				
				int pushedBack = 0;
				List<Entity> entities = event.getPlayer().getNearbyEntities(radius, radius, radius);
				for (Entity entity : entities) {
					if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.PAINTING || entity.getType() == EntityType.ENDER_CRYSTAL || entity.getType() == EntityType.LEASH_HITCH) {
						continue;
					}
					other = entity.getLocation().toVector();
					v = other.subtract(cent).normalize().multiply(force);
					v.setY(v.getY() + force / yScale);
					entity.setVelocity(v);
					
					pushedBack++;
					if (entity instanceof Player) {
						((Player)entity).playSound(entity.getLocation(), Sound.BAT_TAKEOFF, 0.2f, 0f);
						((Player)entity).sendMessage(Util.integrateColor("&cYou got smashed by " + cwp.getName() + "!"));
					}
				}
				
				//Only do it if it actually pushed back entities.
				if (pushedBack <= 0) {
					event.getPlayer().sendMessage(Util.formatMsg("&cNo entities nearby!"));
					return;
				}
				event.getClickedBlock().getWorld().playSound(event.getPlayer().getLocation(), Sound.ZOMBIE_METAL, 1.0f, 0f);
				event.getClickedBlock().getWorld().playSound(event.getPlayer().getLocation(), Sound.ZOMBIE_WOODBREAK, 0.1f, 0f);
				CustomEffects.Cloud(event.getPlayer().getLocation().add(0d, -1d, 0d), 2);
				cwp.getCDM().createCooldown("smash", 30000);
				CWClasses.instance.getPlayerManager().addExp(cwp, 10.0, ClassType.GUARDIAN);
				cwp.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "! &7Pushed back nearby entities with &a" + scales.get("force").getValueAtLevel(cwp.getLevel()) + " &7force!"));
			}
		}
	}
}

package com.clashwars.cwclasses.events;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.CWPlayer.ClassLevelupEvent;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
import com.clashwars.cwclasses.utils.ParticleEffect;
import com.clashwars.cwclasses.utils.Util;

public class MainEvents implements Listener {
	private CWClasses	cwc;

	public MainEvents(CWClasses cwc) {
		this.cwc = cwc;
	}
	
	@EventHandler
	public void npcInteract(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
        if (event.getRightClicked() instanceof HumanEntity) {
        	HumanEntity npc = (HumanEntity) event.getRightClicked();
        	ClassType ct = ClassType.fromString(Util.stripAllColour(npc.getName()).toLowerCase());
        	if (ct != null) {
        		CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(p.getUniqueId());
        		Cooldown cd = cwp.getCDM().getCooldown("ClassSwitch");
        		if (cd != null && cd.onCooldown()) {
        			long timeLeft = cwp.getCDM().getCooldown("ClassSwitch").getTimeLeft();
        			p.sendMessage(Util.formatMsg("&cYou can't switch yet. &8Cooldown: &7" + Util.getMinSecStr(timeLeft, ChatColor.GRAY, ChatColor.DARK_GRAY)));
        			return;
        		}
        		cwp.setActiveClass(ct);
        		cwp.getCDM().createCooldown("ClassSwitch", 1800000);
        		p.sendMessage(Util.formatMsg("&6Your new active class is now " + ct.getColor() + ct.getName() + "&6!"));
            }
        }
	}

	@EventHandler
	public void levelUp(ClassLevelupEvent event) {
		CWPlayer cwp = event.getCWPlayer();
		cwp.sendMessage(Util.formatMsg("&2&lLevel up!\n&aYou are now a level &2&l" + cwp.getLevel() + " &r" + cwp.getActiveClass().getColor() + cwp.getActiveClass().getName() + "&a!"));
		cwp.getPlayer().playSound(cwp.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
		ParticleEffect.FIREWORKS_SPARK.display(cwp.getPlayer().getLocation(), 0.5f, 1.5f, 0.5f, 0.0001f, 50);
		
		//TODO: Check if player unlocked a new ability and if so send message.
		
		cwp.saveExp();
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(event.getPlayer().getUniqueId());
		cwp.saveExp();
	}
	
	@EventHandler
	public void kick(PlayerKickEvent event) {
		CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(event.getPlayer().getUniqueId());
		cwp.saveExp();
	}
	
	
	//Store mobs spawned from spawners.
	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.SPAWNER) {
			cwc.spawnerMobs.add(event.getEntity().getUniqueId());
		}
	}
}

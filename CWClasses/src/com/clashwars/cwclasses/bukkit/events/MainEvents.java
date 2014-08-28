package com.clashwars.cwclasses.bukkit.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.ClassExp.ClassLevelupEvent;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
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
        		cwp.getCDM().createCooldown("ClassSwitch", 3599000);
        		p.sendMessage("&6Your new active class is now " + ct.getColor() + ct.getName() + "&6!");
            }
        }
	}

	@EventHandler
	public void levelUp(ClassLevelupEvent event) {
		CWPlayer cwp = event.getCWPlayer();
		cwp.sendMessage(Util.formatMsg("&2&lLevel up!\n&aYou are now a level &2&l" + cwp.getExpClass().getLevel() + " &r" + cwp.getActiveClass().getColor() + cwp.getActiveClass().getName()) + "&a!");
		//TODO: Particles
		//TODO: Sound
		//TODO: Check if player unlocked a new ability and if so send message.
		
		//TODO: Save data.
	}
}

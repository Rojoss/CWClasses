package com.clashwars.cwclasses.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
import com.clashwars.cwclasses.utils.Util;

public class PickPocket implements AbilityClass {
	HashMap<String, Scalable> scales = new HashMap<String, Scalable>();
	
	@Override
	public void init() {
		scales.put("chance", new Scalable(0, 100, 1, 15));
	}
	
	
	@Override
	public AbilityType getType() {
		return AbilityType.PICKPOCKET;
	}
	@Override
	public String getDescription(int level) {
		return "&7When pickpocketing you will steal a random item from the other player his inventory. You have &a" + scales.get("chance").getValueAtLevel(level) + "% &7chance to succeed. If you fail you will get poisoned.";
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
	public void pickpocket(PlayerInteractEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getRightClicked() == null || !(event.getRightClicked() instanceof Player) || event.getPlayer().getItemInHand().getType() != Material.SHEARS) {
			return;
		}
		Player player = event.getPlayer();
		Player target = (Player)event.getRightClicked();
		CWPlayer cwp = CWClasses.instance.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
		if (cwp.getActiveClass() == getType().getClassType() && cwp.getExpClass().getLevel() >= getLevel()) {
			//Check cooldown
			Cooldown cd = cwp.getCDM().getCooldown("pickpocket");
			if (cd != null && cd.onCooldown()) {
				long timeLeft = cwp.getCDM().getCooldown("pickpocket").getTimeLeft();
    			event.getPlayer().sendMessage(Util.formatMsg(getType().getColor() + getType().getName() + " &cis on cooldown! " + Util.getMinSecStr(timeLeft, ChatColor.GRAY, ChatColor.DARK_GRAY)));
    			return;
			}
			cwp.getCDM().createCooldown("smash", 120000);
			
			//Check chance
			int percentage = scales.get("chance").getValueAtLevel(cwp.getExpClass().getLevel());
			if (Util.checkChance(percentage)) {
				Inventory inv = target.getInventory();
				//Get a list of all slots that have items.
				List<Integer> filledSlots = new ArrayList<Integer>();
				for (int i = 9; i < 36; i++) {
					if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
						return;
					}
					filledSlots.add(i);
				}
				//Return if no items are found.
				if (filledSlots.size() < 1) {
					player.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + " &4failed! &cNo items to steal!"));
					target.sendMessage(Util.integrateColor("&4" + player.getDisplayName() + " &ctried to pickpocket you!"));
					return;
				}
				//Take a random item and give it to the player.
				int slotNr = Util.random(0, filledSlots.size());
				ItemStack loot = inv.getItem(slotNr);
				inv.setItem(slotNr, new ItemStack(Material.AIR));
				player.getInventory().addItem(loot);
				player.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + "ed &6from &5" + target.getDisplayName() + "&6! You stole &5" + loot.getAmount() + " " + loot.getType().toString().toLowerCase().replace("_", " ") + "&6!"));
				target.sendMessage(Util.integrateColor("&cYou have been pickpocketed by &4" + player.getDisplayName() + "&c. &cYou lost &4" + loot.getAmount() + " " + loot.getType().toString().toLowerCase().replace("_", " ") + "&c!"));
			} else {
				//Failed so add poison.
				player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
				player.sendMessage(Util.integrateColor(getType().getColor() + getType().getName() + " &4failed!"));
				target.sendMessage(Util.integrateColor("&4" + player.getDisplayName() + " &ctried to pickpocket you!"));
			}
		}
	}
}

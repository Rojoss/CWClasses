package com.clashwars.cwclasses.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.abilities.internal.Scalable;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
import com.clashwars.cwclasses.utils.Util;

public class Commands {
	private CWClasses			cwc;

	public Commands(CWClasses cwc) {
		this.cwc = cwc;
	}

	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		/*  /class [class] [player]  */
		if (label.equalsIgnoreCase("class")) {
			Player player = null;

			//Console check.
			if (args.length < 2) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Util.formatMsg("&cSpecify a player to use this on the console."));
					return true;
				}
				player = ((Player) sender);
			}
			
			//Get the player if specified
			if (args.length >= 2) {
				if (!sender.hasPermission("cwclasses.class.other") && !sender.isOp()) {
					sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
					return true;
				}
				player = cwc.getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(Util.formatMsg("&cInvalid player."));
					return true;
				}
			}
			CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
			
			//Get the class if specified
			ClassType ct = cwp.getActiveClass();
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("list")) {
					sender.sendMessage(Util.integrateColor("&8===== &4&lClass List &8====="));
					String prefix;
					for (ClassType c : ClassType.values()) {
						if (cwp.getActiveClass() == c) {
							prefix = "&a&l>> ";
						} else {
							prefix = "&8&l- ";
						}
						sender.sendMessage(Util.integrateColor(prefix + c.getColor() + "&l" + c.getName() + " &8[&6Lvl&7: &5" + cwp.getLevel(c) + "&8, &6Exp&7: &5" + cwp.getExp(c) + "&8]" ));
					}
					return true;
				}
				ct = ClassType.fromString(args[0]);
				if (ct == null) {
					sender.sendMessage(Util.formatMsg("&cInvalid class name."));
					return true;
				}
			}
			
			//No class found for player and no class specified.
			if (ct == null) {
				sender.sendMessage(Util.integrateColor("&8===== &4&lClass Info &8====="));
				sender.sendMessage(Util.integrateColor("&cYou have no active class!"));
				sender.sendMessage(Util.integrateColor("&cGo to spawn and interact with one of the class npc's."));
				return true;
			}
			
			//Display info
			double exp = cwp.getExp(ct);
			int lvl = cwp.getLevel(ct);
			
			int xpNeeded = cwp.getExpDiffNextLvl(ct);
			int xpProgress = cwp.getExpProgress(ct);
			
			sender.sendMessage(Util.integrateColor("&8===== &4&lClass Info &7[" + ct.getColor() + ct.getName() + "&7] &8====="));
			if (cwp.getActiveClass() == ct) {
				sender.sendMessage(Util.integrateColor("&6Status&8: &a&lActive"));
			} else {
				sender.sendMessage(Util.integrateColor("&6Status&8: &4&lInactive"));
			}
			sender.sendMessage(Util.integrateColor("&6Level&8: &5" + lvl));
			sender.sendMessage(Util.integrateColor("&6Total Exp&8: &5" + exp));
			sender.sendMessage(Util.integrateColor("&6Exp&8: &a" + xpProgress + "&7/&2" + xpNeeded));
			sender.sendMessage(Util.integrateColor("&6Level progress&8: &5" + Math.round(Util.getPercentage(xpProgress, xpNeeded)) + "%"));
			return true;
		}
		
		/*  /abilities [class] [player]  */
		if (label.equalsIgnoreCase("abilities")) {
			Player player = null;
			
			//Console check.
			if (args.length < 2) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Util.formatMsg("&cSpecify a player to use this on the console."));
					return true;
				}
				player = ((Player) sender);
			}
			
			//Get the player if specified
			if (args.length >= 2) {
				player = cwc.getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(Util.formatMsg("&cInvalid player."));
					return true;
				}
			}
			CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
			
			//Get the class if specified
			ClassType ct = cwp.getActiveClass();
			if (args.length >= 1) {
				ct = ClassType.fromString(args[0]);
				if (ct == null) {
					sender.sendMessage(Util.formatMsg("&cInvalid class name."));
					return true;
				}
			}
			if (ct == null) {
				sender.sendMessage(Util.integrateColor("&8===== &4&lClass Info &8====="));
				sender.sendMessage(Util.integrateColor("&cYou have no active class!"));
				sender.sendMessage(Util.integrateColor("&cGo to spawn and interact with one of the class npc's."));
				return true;
			}
			sender.sendMessage(Util.integrateColor("&8===== &4&lAbilities Info &7[" + ct.getColor() + ct.getName() + "&7] &8====="));
			int lvl = cwp.getLevel(ct);
			String status = "";
			String type = "";
			HashMap<String, Scalable> scales;
			for (String abilityName : ct.getAbilities()) {
				AbilityType at = AbilityType.fromString(abilityName);
				if (lvl >= at.getAbilitClass().getLevel()) {
					status = " &8[&aUnlocked&8]";
				} else {
					status = " &8[&4Locked &7Lvl needed: &c" + at.getAbilitClass().getLevel() + "&8]";
				}
				sender.sendMessage(Util.integrateColor(at.getColor() + "&l" + at.getName() + status));
				sender.sendMessage(Util.integrateColor("&6Description&8: &7" + at.getAbilitClass().getDescription(lvl)));
				if (at.getAbilitClass().isPassive()) {
					type = "&ePassive";
				} else {
					type = "&aActive";
				}
				if (at.getAbilitClass().getActivationInfo().isEmpty()) {
					sender.sendMessage(Util.integrateColor("&6Type&8: &5" + type));
				} else {
					sender.sendMessage(Util.integrateColor("&6Type&8: &5" + type + " &8(&7" + at.getAbilitClass().getActivationInfo() + "&8)"));
				}
				
				scales = at.getAbilitClass().getScales();
				if (scales != null) {
					for (String key : scales.keySet()) {
						sender.sendMessage(Util.integrateColor("&6" + Util.capitalize(key) + " scaleable&8: &7Lvl&6" + scales.get(key).getMinLevel() + "&8: &a" 
								+ scales.get(key).getMinValue() + " &7Lvl&6" + scales.get(key).getMaxLevel() + "&8: &a" + scales.get(key).getMaxValue()));
					}
				}
				sender.sendMessage(Util.integrateColor("&8≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣≣"));
			}
			return true;
		}
		
		
		/*  /switch [className]  */
		if (label.equalsIgnoreCase("switch")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Util.formatMsg("&cThis is a player only command."));
				return true;
			}

			Player player = (Player) sender;
			CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
			
			if (args.length == 0) {
				if (cwp.getActiveClass() == null) {
					sender.sendMessage(Util.formatMsg("&cYou have no active class."));
				} else {
					sender.sendMessage(Util.formatMsg("&6Active class&8: &4" + cwp.getActiveClass().getColor() + cwp.getActiveClass().getName()));
				}
				String classNames = "";
				for (ClassType c : ClassType.values()) {
					classNames += c.getColor() + c.getName();
				}
				sender.sendMessage(Util.formatMsg("&7Command usage&8: &5/switch &8[" + classNames + "&8]"));
				return true;
			}
			
			ClassType ct = ClassType.fromString(args[0]);
	    	if (ct != null) {
	    		Cooldown cd = cwp.getCDM().getCooldown("ClassSwitch");
	    		if (cd != null && cd.onCooldown()) {
	    			long timeLeft = cwp.getCDM().getCooldown("ClassSwitch").getTimeLeft();
	    			sender.sendMessage(Util.formatMsg("&cYou can't switch yet. &8Cooldown: &7" + Util.getMinSecStr(timeLeft, ChatColor.GRAY, ChatColor.DARK_GRAY)));
	    			return true;
	    		}
	    		cwp.setActiveClass(ct);
	    		cwp.getCDM().createCooldown("ClassSwitch", 1800000);
	    		sender.sendMessage(Util.formatMsg("&6Your new active class is now " + ct.getColor() + ct.getName() + "&6!"));
	        } else {
	        	String classNames = "";
	        	for (ClassType c : ClassType.values()) {
					classNames += c.getColor() + c.getName();
				}
	        	sender.sendMessage(Util.formatMsg("&cInvalid class name."));
	        	sender.sendMessage(Util.formatMsg("&7Command usage&8: &5/switch &8[" + classNames + "&8]"));
	        }
			return true;
		}
		
		
		/*  /classes  */
		if (label.equalsIgnoreCase("classes")) {
			if (args.length > 0) {
				/*  /classes reload  */
				if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("cwclasses.classes.reload") && !sender.isOp()) {
						sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
						return true;
					}
					cwc.getMainConfig().load();
					sender.sendMessage(Util.formatMsg("&6Reloaded."));
					return true;
				}
				
				
				/*  /classes givexp|takexp|setxp (amt) [player] [class]  */
				if (args[0].equalsIgnoreCase("givexp") || args[0].equalsIgnoreCase("takexp") || args[0].equalsIgnoreCase("setxp")) {
					if (!sender.hasPermission("cwclasses.classes.xp") && !sender.isOp()) {
						sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
						return true;
					}
					if (args.length < 2) {
						sender.sendMessage(Util.formatMsg("&cInvalid usage. &7/classes " + args[0] + " (amt) [player] [class]"));
						return true;
					}
					
					Player player = null;

					//Console check.
					if (args.length < 3) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(Util.formatMsg("&cSpecify a player to use this on the console."));
							return true;
						}
						player = ((Player) sender);
					}
					
					//Get the player if specified
					if (args.length >= 3) {
						if (!sender.hasPermission("cwclasses.class.other") && !sender.isOp()) {
							sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
							return true;
						}
						player = cwc.getServer().getPlayer(args[2]);
						if (player == null) {
							sender.sendMessage(Util.formatMsg("&cInvalid player."));
							return true;
						}
					}
					CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
					
					//Get the class if specified
					ClassType ct = cwp.getActiveClass();
					if (args.length >= 4) {
						ct = ClassType.fromString(args[3]);
						if (ct == null) {
							sender.sendMessage(Util.formatMsg("&cInvalid class name."));
							return true;
						}
					}
					if (ct == null) {
						sender.sendMessage(Util.formatMsg("&cNo class active. Specify class name as last arg to set xp for inactive classes."));
						return true;
					}
					
					//Validate and get amount.
					int amt = Util.getInt(args[1]);
					if (amt < 0) {
						sender.sendMessage(Util.formatMsg("&cInvalid amount. Must be a number and possitive."));
						return true;
					}
					
					if (args[0].equalsIgnoreCase("givexp")) {
						cwp.incrementExp(ct, amt);
						sender.sendMessage(Util.formatMsg("&6Given &a" + amt + " "  + ct.getColor() + ct.getName() + " &6exp to &5" + cwp.getName() + "&6. &8[&7Total: &a" + cwp.getExp(ct) + "&8]"));
						cwp.sendMessage(Util.formatMsg("&6You received &a" + amt + " " + ct.getColor() + ct.getName() + " &6exp! &8[&7Total: &a" + cwp.getExp(ct) + "&8]"));
					} else if (args[0].equalsIgnoreCase("takexp")) {
						cwp.decrementExp(ct, amt);
						sender.sendMessage(Util.formatMsg("&6Taken &a" + amt + " "  + ct.getColor() + ct.getName() + " &6exp from &5" + cwp.getName() + "&6. &8[&7Total: &a" + cwp.getExp(ct) + "&8]"));
						cwp.sendMessage(Util.formatMsg("&a" + amt + " " + ct.getColor() + ct.getName() + " &6exp was taken from you. &8[&7Total: &a" + cwp.getExp(ct) + "&8]"));
					} else if (args[0].equalsIgnoreCase("setxp")) {
						cwp.setExp(ct, amt);
						sender.sendMessage(Util.formatMsg("&6Set &5" + cwp.getName() + "'s " + ct.getColor() + ct.getName() + " &6exp at &a" + amt + "&6."));
						cwp.sendMessage(Util.formatMsg("&6Your &a" + ct.getColor() + ct.getName() + " &6exp has been set to &a" + amt + "&6."));
					}
					return true;
				}
				
				
				
				/*  /classes reset [player]  */
				if (args[0].equalsIgnoreCase("reset")) {
					if (!sender.hasPermission("cwclasses.classes.reset") && !sender.isOp()) {
						sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
						return true;
					}
					Player player = null;

					//Console check.
					if (args.length < 2) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(Util.formatMsg("&cSpecify a player to use this on the console."));
							return true;
						}
						player = ((Player) sender);
					}
					
					//Get the player if specified
					if (args.length >= 2) {
						player = cwc.getServer().getPlayer(args[1]);
						if (player == null) {
							sender.sendMessage(Util.formatMsg("&cInvalid player."));
							return true;
						}
					}
					CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
					cwp.getCDM().removeCooldowns();
					sender.sendMessage(Util.formatMsg("&6All cooldowns from &5" + cwp.getName() + " &6have been removed!"));
					cwp.sendMessage(Util.formatMsg("&6All your cooldowns have been removed/reset!"));
					return true;
				}
			}
			
			sender.sendMessage(Util.integrateColor("&8===== &4CWClasses command help &8====="));
			sender.sendMessage(Util.integrateColor("&6/class list [player] &8- &5List classes and stats."));
			sender.sendMessage(Util.integrateColor("&6/class [class] [player] &8- &5Display class stats."));
			sender.sendMessage(Util.integrateColor("&6/abilities [class] [player] &8- &5Display ability stats."));
			sender.sendMessage(Util.integrateColor("&6/switch [class] &8- &5Switch to another class."));
			sender.sendMessage(Util.integrateColor("&6/classes reload &8- &5Reload the plugin"));
			sender.sendMessage(Util.integrateColor("&6/classes givexp (amt) [player] [class] &8- &5Give class xp."));
			sender.sendMessage(Util.integrateColor("&6/classes takexp (amt) [player] [class] &8- &5Take class xp."));
			sender.sendMessage(Util.integrateColor("&6/classes setxp (amt) [player] [class] &8- &5Set class xp."));
			sender.sendMessage(Util.integrateColor("&6/classes reset [player] &8- &5Reset cooldowns."));
			return true;
		}
		
		return false;
	}
}

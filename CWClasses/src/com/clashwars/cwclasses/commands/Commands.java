package com.clashwars.cwclasses.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.CWPlayer;
import com.clashwars.cwclasses.ClassExp;
import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager.Cooldown;
import com.clashwars.cwclasses.utils.Util;

public class Commands {
	private CWClasses			cwc;

	public Commands(CWClasses cwc) {
		this.cwc = cwc;
	}

	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		/*  /class [player]  */
		if (label.equalsIgnoreCase("class")) {
			UUID uuid = null;

			if (args.length < 2) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Util.formatMsg("&cSpecify a player to use this on the console."));
					return true;
				}

				uuid = ((Player) sender).getUniqueId();
			} else {
				if (!sender.hasPermission("cwclasses.class.other") && !sender.isOp()) {
					sender.sendMessage(Util.formatMsg("&cInsufficient permissions."));
					return true;
				}

				Player player = cwc.getServer().getPlayer(args[1]);
				uuid = (player != null ? player.getUniqueId() : UUID.fromString(args[1]));
			}

			if (uuid == null) {
				sender.sendMessage(Util.formatMsg("&cInvalid player."));
				return true;
			}
			
			CWPlayer cwp = cwc.getPlayerManager().getOrCreatePlayer(uuid);
			ClassType ac = cwp.getActiveClass();
			if (ac == null || ac == ClassType.UNKNOWN) {
				sender.sendMessage(Util.integrateColor("&8===== &&4&lClass Info &8====="));
				sender.sendMessage(Util.integrateColor("&cYou have no active class!"));
				sender.sendMessage(Util.integrateColor("&cGo to spawn and interact with one of the class npc's."));
				return true;
			}
			
			ClassExp cxp = cwp.getExpClass();
			sender.sendMessage(Util.integrateColor("&8===== &&4&lClass Info &7[" + ac.getColor() + ac.getName() + "&7] &8====="));
			sender.sendMessage(Util.integrateColor("&6Level&8: &5" + cxp.getLevel()));
			sender.sendMessage(Util.integrateColor("&6Total Exp&8: &5" + cxp.getExp()));
			sender.sendMessage(Util.integrateColor("&6Exp&8: &a" + cxp.getLevelProgression() + "&7/&2" + cxp.calculateExpForLevel(cxp.getLevel())));
			sender.sendMessage(Util.integrateColor("&6Level progress&8: &5" + cxp.getLevelPercentage() + "%"));
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
				if (cwp.getActiveClass() == null || cwp.getActiveClass() == ClassType.UNKNOWN) {
					sender.sendMessage(Util.formatMsg("&cYou have no active class."));
				} else {
					sender.sendMessage(Util.formatMsg("&6Active class&8: &4" + cwp.getActiveClass().getColor() + cwp.getActiveClass().getName()));
				}
				String classNames = "";
				for (ClassType c : ClassType.values()) {
					if (c != ClassType.UNKNOWN) {
						classNames += c.getColor() + c.getName();
					}
				}
				sender.sendMessage(Util.formatMsg("&7Command usage&8: &5/switch &8[" + classNames + "&8]"));
				return true;
			}
			
			ClassType ct = ClassType.fromString(args[0]);
	    	if (ct != null && ct != ClassType.UNKNOWN) {
	    		Cooldown cd = cwp.getCDM().getCooldown("ClassSwitch");
	    		if (cd != null && cd.onCooldown()) {
	    			long timeLeft = cwp.getCDM().getCooldown("ClassSwitch").getTimeLeft();
	    			sender.sendMessage(Util.formatMsg("&cYou can't switch yet. &8Cooldown: &7" + Util.getMinSecStr(timeLeft, ChatColor.GRAY, ChatColor.DARK_GRAY)));
	    			return true;
	    		}
	    		cwp.setActiveClass(ct);
	    		cwp.getCDM().createCooldown("ClassSwitch", 3599000);
	    		sender.sendMessage(Util.formatMsg("&6Your new active class is now " + ct.getColor() + ct.getName() + "&6!"));
	        }
			return true;
		}
		
		
		/*  /switch [className]  */
		if (label.equalsIgnoreCase("classes")) {
			cwc.getMainConfig().load();
			sender.sendMessage(Util.formatMsg("&6Reloaded."));
			return true;
		}
		
		return false;
	}
	
	/*
	@Command(permissions = { "cwclasses.classes.givexp" }, aliases = { "classes" }, secondaryAliases = { "givexp", "giveexp", "giveexperience", "givexperience", "gxp" })
	public boolean classes_giveXP(CommandSender sender, String label, String argument, String... args) {
		if (args.length < 1) {
			sender.sendMessage(pf + DARK_RED + "Usage: /classes givexp <xp> [class] [player]");
			return true;
		}

		double exp;
		try {
			exp = Double.parseDouble(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(pf + DARK_RED + "Invalid experience points.");
			return true;
		}

		String[] modified = args.clone();
		modified[0] = Double.toString(-exp);
		if (exp < 0) {
			return classes_takeXP(sender, label, argument, modified);
		}

		UUID uuid = null;
		if (args.length < 3) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(pf + DARK_RED + "Must either specify a player or run as a player.");
				return true;
			}

			uuid = ((Player) sender).getUniqueId();
		} else {
			Player player = cwc.getServer().getPlayer(args[2]);
			uuid = (player != null ? player.getUniqueId() : UUID.fromString(args[2]));
		}

		if (uuid == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid player.");
			return true;
		}
		/*
		ClassedPlayer cp = cwc.getPlayerManager().getOrCreatePlayer(uuid);
		Class clazz = (args.length < 2 ? cp.getActiveClass() : Class.getClasses().get(args[1]));

		if (clazz == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid class.");
			return true;
		}

		cp.incrementExp(clazz, exp);
		sender.sendMessage(pf + "Successfully gave " + exp + " experience.");
		
		return true;
	}
	

	@Command(permissions = { "cwclasses.classes.takexp" }, aliases = { "classes" }, secondaryAliases = { "takexp", "takeexp", "takeexperience", "takexperience", "txp" })
	public boolean classes_takeXP(CommandSender sender, String label, String argument, String... args) {
		if (args.length < 1) {
			sender.sendMessage(pf + DARK_RED + "Usage: /classes takexp <xp> [class] [player]");
			return true;
		}

		double exp;
		try {
			exp = Double.parseDouble(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(pf + DARK_RED + "Invalid experience points.");
			return true;
		}

		String[] modified = args.clone();
		modified[0] = Double.toString(-exp);
		if (exp < 0) {
			return classes_giveXP(sender, label, argument, modified);
		}

		UUID uuid = null;
		if (args.length < 3) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(pf + DARK_RED + "Must either specify a player or run as a player.");
				return true;
			}

			uuid = ((Player) sender).getUniqueId();
		} else {
			Player player = cwc.getServer().getPlayer(args[2]);
			uuid = (player != null ? player.getUniqueId() : UUID.fromString(args[2]));
		}

		if (uuid == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid player.");
			return true;
		}
		/*
		ClassedPlayer cp = cwc.getPlayerManager().getOrCreatePlayer(uuid);
		Class clazz = (args.length < 2 ? cp.getActiveClass() : Class.getClasses().get(args[1]));

		if (clazz == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid class.");
			return true;
		}

		cp.decrementExp(clazz, exp);
		sender.sendMessage(pf + "Successfully took " + exp + " experience.");
		return true;
	}


	@Command(permissions = { "cwclasses.classes.reload" }, aliases = { "classes" }, secondaryAliases = { "reload" })
	public boolean classes_reload(CommandSender sender, String label, String argument, String... args) {
		cwc.getMainConfig().load();
		sender.sendMessage(pf + "Reloaded.");
		return true;
	}
	*/

	/* End of commands */
	/*
	public void populateCommands() {
		commands.clear();

		for (Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(Command.class) && method.getReturnType().equals(boolean.class)) {
				commands.add(method);
			}
		}
	}

	public boolean executeCommand(CommandSender sender, String lbl, String... args) {
		try {
			for (Method method : commands) {
				Command command = method.getAnnotation(Command.class);
				String[] permissions = command.permissions();
				String[] aliases = command.aliases();
				String[] saliases = command.secondaryAliases();

				for (String alias : aliases) {
					if (alias.equalsIgnoreCase(lbl)) {
						if ((saliases == null || saliases.length <= 0)) {
							check: if (!sender.isOp() && permissions != null && permissions.length > 0) {
								for (String p : permissions) {
									if (sender.hasPermission(p)) {
										break check;
									}
								}

								sender.sendMessage(pf + RED + "Insufficient permissions.");
								return true;
							}
							try {
								return (Boolean) method.invoke(this, sender, lbl, null, args);
							} catch (InvocationTargetException e) {
								continue;
							}
						}

						if (args.length <= 0) {
							continue;
						}

						for (String salias : saliases) {
							if (salias.equalsIgnoreCase(args[0])) {
								check: if (!sender.isOp() && permissions != null && permissions.length > 0) {
									for (String p : permissions) {
										if (sender.hasPermission(p)) {
											break check;
										}
									}

									sender.sendMessage(pf + RED + "Insufficient permissions.");
									return true;
								}

								try {
									return (Boolean) method.invoke(this, sender, lbl, args[0], Util.trimFirst(args));
								} catch (InvocationTargetException e) {
									continue;
								}
							}
						}
					}
				}
			}
			/*sender.sendMessage(DARK_GRAY + "===== " + DARK_RED + "CWScripts command menu" + DARK_GRAY + " =====");
			sender.sendMessage(GRAY + "You can use the command with 1 parameter to get more info.");
			sender.sendMessage(GRAY + "For example " + DARK_GRAY + "/cws var" + GRAY + " gives info about var management cmds.");
			sender.sendMessage(GOLD + "/cws var <action> [object.]<var> [value]" + DARK_GRAY + " - " + GRAY + "Var management");
			sender.sendMessage(GOLD + "/cws loc <type> [object.]<var>" + DARK_GRAY + " - " + GRAY + "Create a location var.");
			sender.sendMessage(GOLD + "/cws del <type> [object.][var]" + DARK_GRAY + " - " + GRAY + "Delete a var/object.");
			sender.sendMessage(GOLD + "/cws run <file:script> [player]" + DARK_GRAY + " - " + GRAY + "Force run a script.");
			sender.sendMessage(GOLD + "/cws reload <type> [args]" + DARK_GRAY + " - " + GRAY + "Reload scripts and configs.");
			sender.sendMessage(GOLD + "/cws save" + DARK_GRAY + " - " + GRAY + "Save scripts and configs.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
		
	}*/
}

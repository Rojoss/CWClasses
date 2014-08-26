package com.clashwars.cwclasses.commands;

import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.clashwars.cwclasses.CWClasses;
import com.clashwars.cwclasses.utils.Util;

public class Commands {
	private CWClasses			cwc;
	private final List<Method>	commands	= new ArrayList<Method>();
	private final String		pf			= DARK_GRAY + "[" + DARK_RED + "CW" + DARK_GRAY + "] " + GOLD;

	public Commands(CWClasses cwc) {
		this.cwc = cwc;
	}

	/* Start of commands */

	@SuppressWarnings("deprecation")
	@Command(permissions = {}, aliases = { "class" })
	public boolean skill(CommandSender sender, String label, String argument, String... args) {
		UUID uuid = null;

		if (args.length < 2) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(pf + DARK_RED + "Must either specify a player or run as a player.");
				return true;
			}

			uuid = ((Player) sender).getUniqueId();
		} else {
			if (!sender.hasPermission("cwclasses.class.other") && !sender.isOp()) {
				sender.sendMessage(pf + DARK_RED + "Insufficient permissions.");
				return true;
			}

			Player player = cwc.getServer().getPlayer(args[1]);
			uuid = (player != null ? player.getUniqueId() : UUID.fromString(args[1]));
		}

		if (uuid == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid player.");
			return true;
		}

		/*ClassedPlayer cp = cwc.getPlayerManager().getOrCreatePlayer(uuid);
		Class clazz = (args.length < 1 ? cp.getActiveClass() : Class.getClasses().get(args[0]));

		if (clazz == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid class.");
			return true;
		}

		Levelable level = cp.getLevels().get(clazz);

		if (level == null) {
			sender.sendMessage(pf + DARK_RED + "This player has not yet used this class.");
			return true;
		}

		sender.sendMessage(pf + "Class: " + DARK_RED + clazz.getName());
		sender.sendMessage(pf + "Level: " + DARK_RED + level.getLevel());
		sender.sendMessage(pf + "Total experience: " + DARK_RED + level.getExperience());
		sender.sendMessage(pf + "Level experience: " + DARK_RED + level.getLevelProgression());
		sender.sendMessage(pf + "Leveling percentage: " + DARK_RED + level.getLevelPercentage() + "%");
		sender.sendMessage(pf + "Experience until level: " + DARK_RED + level.getExperienceToNextLevel());
		*/
		return true;
	}

	@Command(permissions = {}, aliases = { "switch" })
	public boolean switchCommand(CommandSender sender, String label, String argument, String... args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(pf + DARK_RED + "Player-only command.");
			return true;
		}

		Player player = (Player) sender;
		/*
		ClassedPlayer cp = cwc.getPlayerManager().getOrCreatePlayer(player.getUniqueId());
		Class active = cp.getActiveClass();

		if (args.length == 0) {
			sender.sendMessage(pf + "Active class: " + DARK_RED + (active == null ? "NONE" : active.getName()));
			return true;
		}

		Class newclass = Class.getClasses().get(args[0]);
		if (newclass == null) {
			sender.sendMessage(pf + DARK_RED + "Invalid class.");
			return true;
		}

		cp.switchTo(newclass);
		sender.sendMessage(pf + "Class successfully switched to: " + DARK_RED + newclass.getName());
		*/
		return true;
	}

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
		*/
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
		*/
		return true;
	}

	@Command(permissions = { "cwclasses.classes.reload" }, aliases = { "classes" }, secondaryAliases = { "reload" })
	public boolean classes_reload(CommandSender sender, String label, String argument, String... args) {
		cwc.getMainConfig().load();
		sender.sendMessage(pf + "Reloaded.");
		return true;
	}

	/* End of commands */
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

							return (Boolean) method.invoke(this, sender, lbl, null, args);
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

								return (Boolean) method.invoke(this, sender, lbl, args[0], Util.trimFirst(args));
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
			sender.sendMessage(GOLD + "/cws save" + DARK_GRAY + " - " + GRAY + "Save scripts and configs.");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}

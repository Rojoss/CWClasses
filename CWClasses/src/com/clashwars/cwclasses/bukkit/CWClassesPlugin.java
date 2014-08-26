package com.clashwars.cwclasses.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.clashwars.cwclasses.CWClasses;

public class CWClassesPlugin extends JavaPlugin {
	private CWClasses	cwc;

	@Override
	public void onDisable() {
		cwc.onDisable();
	}

	@Override
	public void onEnable() {
		cwc = new CWClasses(this);
		cwc.onEnable();
	}

	public CWClasses getInstance() {
		return cwc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cwc.onCommand(sender, cmd, label, args);
	}
}

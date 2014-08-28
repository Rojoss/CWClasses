package com.clashwars.cwclasses;

import java.sql.Connection;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.bukkit.CWClassesPlugin;
import com.clashwars.cwclasses.bukkit.events.ExpEvents;
import com.clashwars.cwclasses.bukkit.events.MainEvents;
import com.clashwars.cwclasses.commands.Commands;
import com.clashwars.cwclasses.config.Config;
import com.clashwars.cwclasses.config.PluginConfig;
import com.clashwars.cwclasses.sql.MySql;
import com.clashwars.cwclasses.sql.SqlInfo;

public class CWClasses {
	private CWClassesPlugin		cwc;

	private PlayerManager		playerManager;
	private Commands			cmds;

	private Config				cfg;
	private PluginConfig		mainCfg;
	
	private MySql sql = null;
	private Connection c = null;

	private final Logger		log	= Logger.getLogger("Minecraft");

	public static CWClasses		instance;


	public CWClasses(CWClassesPlugin cwc) {
		this.cwc = cwc;
	}

	public void onDisable() {
		instance = null;
		mainCfg.save();
		
		//TODO: save class data of all online CWPlayers.
		getServer().getScheduler().cancelTasks(getPlugin());

		log("Disabled.");
	}

	public void onEnable() {
		instance = this;

		cfg = new Config();
		
		mainCfg = new PluginConfig(cfg);
		mainCfg.init();
		mainCfg.load();

		SqlInfo sqli = cfg.getSqlInfo();
		sql = new MySql(this, sqli.getAddress(), sqli.getPort(), sqli.getDb(), sqli.getUser(), sqli.getPass());
		if (sql == null) {
			log("Can't conntact to database!");
			getPlugin().getPluginLoader().disablePlugin(getPlugin());
			return;
		}
		c = sql.openConnection();
		if (c == null) {
			log("Can't conntact to database!");
			getPlugin().getPluginLoader().disablePlugin(getPlugin());
			return;
		}
		//TODO: create table...
		//sql.createTable("players", true, playerStructure);

		playerManager = new PlayerManager(this);
		playerManager.populate();

		cmds = new Commands(this);
		
		//getServer().getScheduler().runTaskTimerAsynchronously(getPlugin(), (sus = new SqlUpdateSchedule(this)), 0, 15L);
		registerEvents();
		log("Successfully enabled.");
	}
	
	private void registerEvents() {
		PluginManager pm = getPlugin().getServer().getPluginManager();
		pm.registerEvents(new MainEvents(this), getPlugin());
		pm.registerEvents(new ExpEvents(this), getPlugin());
		for (AbilityType ability : AbilityType.values()) {
			if (ability.getAbilitClass() != null)
				pm.registerEvents(ability.getAbilitClass(), getPlugin());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cmds.onCommand(sender, cmd, label, args);
	}

	public void log(Object msg) {
		log.info("[CWClasses " + getPlugin().getDescription().getVersion() + "]: " + msg.toString());
	}

	public CWClassesPlugin getPlugin() {
		return cwc;
	}

	public Server getServer() {
		return getPlugin().getServer();
	}

	public Config getConfig() {
		return cfg;
	}
	
	public PluginConfig getMainConfig() {
		return mainCfg;
	}
	
	public Connection getSql() {
		return c;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}

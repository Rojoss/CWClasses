package com.clashwars.cwclasses;

import com.clashwars.cwclasses.abilities.internal.AbilityType;
import com.clashwars.cwclasses.bukkit.CWClassesPlugin;
import com.clashwars.cwclasses.bukkit.events.ExpEvents;
import com.clashwars.cwclasses.bukkit.events.MainEvents;
import com.clashwars.cwclasses.commands.Commands;
import com.clashwars.cwclasses.config.Config;
import com.clashwars.cwclasses.config.PluginConfig;
import com.clashwars.cwclasses.sql.MySql;
import com.clashwars.cwclasses.sql.SqlInfo;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class CWClasses {
    private CWClassesPlugin cwc;

    private PlayerManager playerManager;
    private Commands cmds;

    private Config cfg;
    private PluginConfig mainCfg;

    private MySql sql = null;
    private Connection c = null;

    private final Logger log = Logger.getLogger("Minecraft");

    public Set<UUID> spawnerMobs = new HashSet<UUID>();

    public static CWClasses instance;


    public CWClasses(CWClassesPlugin cwc) {
        this.cwc = cwc;
    }

    @Deprecated
    public void onDisable() {
        instance = null;
        mainCfg.save();

        for (Player p : getServer().getOnlinePlayers()) {
            CWPlayer cwp = getPlayerManager().getOrCreatePlayer(p.getUniqueId());
            cwp.saveExp(cwp.expToString());
        }

        try {
            c.close();
            sql.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
        c = sql.openConnection();
        if (c == null) {
            log("Can't conntact to database!");
            getPlugin().getPluginLoader().disablePlugin(getPlugin());
            return;
        }

        playerManager = new PlayerManager(this);
        playerManager.populate();

        cmds = new Commands(this);

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
        try {
            if (c == null || c.isClosed()) {
                c = sql.openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}

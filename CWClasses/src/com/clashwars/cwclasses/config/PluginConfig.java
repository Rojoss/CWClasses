package com.clashwars.cwclasses.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.clashwars.cwclasses.sql.SqlInfo;

public class PluginConfig extends Config {
	private YamlConfiguration	cfgFile;
	private Config 				cfg;
	private ConfigUtil			cu;
	private final File			dir		= new File("plugins/CWClasses/");
	private final File			file	= new File(dir + "/CWClasses.yml");

	public PluginConfig(Config cfg) {
		this.cfg = cfg;
	}
	
	public void init() {
		try {
			dir.mkdirs();
			file.createNewFile();
			cfgFile = new YamlConfiguration();
			cu = new ConfigUtil(cfgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			cfgFile.load(file);
			
			//Class.getClasses().clear();

			String address = cu.getString("sql.address", "localhost");
			String port = cu.getString("sql.port", "3306");
			String user = cu.getString("sql.username", "root");
			String password = cu.getString("sql.password", "");
			String database = cu.getString("sql.database", "cwclasses");
			cfg.setSqlInfo(new SqlInfo(address, port, user, password, database));
			
			/*
			if (!cfgFile.isConfigurationSection("classes")) {
				cfgFile.createSection("classes");
			}

			for (String c : cfgFile.getConfigurationSection("classes").getKeys(false)) {
				Class clazz = new Class(c);

				if (!cfgFile.isConfigurationSection("classes." + c + ".permissions")) {
					cfgFile.createSection("classes." + c + ".permissions");
				}

				for (String p : cfg.getConfigurationSection("classes." + c + ".permissions").getKeys(false)) {
					List<String> perms = cu.getStringList("classes." + c + ".permissions." + p);
					clazz.getPermissions().put(Integer.parseInt(p), perms.toArray(new String[perms.size()]));
				}

				Class.getClasses().put(c, clazz);
			}
			*/

			cfgFile.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			cfgFile.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

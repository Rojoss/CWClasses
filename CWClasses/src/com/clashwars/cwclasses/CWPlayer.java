package com.clashwars.cwclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager;
import com.clashwars.cwclasses.utils.Util;

public class CWPlayer {
	private CWClasses	cwc;
	private UUID		uuid;
	
	private ClassType activeClass;
	private HashMap<String, Double> exp = new HashMap<String, Double>();
	private ClassExp	cExp;
	private CooldownManager cdm;

	
	public CWPlayer(CWClasses cwc, UUID uid) {
		this.cwc = cwc;
		this.uuid = uid;
		cExp = new ClassExp(this);
		cdm = new CooldownManager();
	}

	public CWClasses getPlugin() {
		return cwc;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	
	//Custom methods.
	public CooldownManager getCDM() {
		return cdm;
	}
	
	public ClassType getActiveClass() {
		return activeClass;
	}
	
	//Set a new active class.
	public void setActiveClass(ClassType classType) {
		if (activeClass == null || activeClass == ClassType.UNKNOWN) {
			this.activeClass = classType;
			return;
		}
		//Save progress from last class and set new active class.
		exp.put(activeClass.getName(), cExp.getExp());
		try {
			Statement statement = cwc.getSql().createStatement();
			if (statement.executeUpdate("UPDATE Players SET ActiveClass='" + activeClass + "', ClassExp='" + expToString() + "' WHERE UUID='" + getUUID().toString() + "';") < 1) {
				sendMessage(Util.formatMsg("&cError connecting to the databse. Can't save data to prevent data loss you can't switch classes now."));
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		updateExp(null, true, false);
		
		//Set new class.
		this.activeClass = classType;
	}
	
	
	//Exp edit
	public void incrementExp(double xp) {
		if (activeClass != null && activeClass != ClassType.UNKNOWN) {
			exp.put(activeClass.toString(), exp.get(activeClass.toString()) + xp);
			cExp.incrementExp(xp);
		}
		return;
	}

	public void decrementExp(double xp) {
		if (activeClass != null && activeClass != ClassType.UNKNOWN) {
			exp.put(activeClass.toString(), Math.max(0, exp.get(activeClass.toString()) - xp));
			cExp.decrementExp(xp);
		}
		return;
	}
	
	
	//Update the cached exp from database. Put null to directly get it from the database.
	public void updateExp(String expStr, boolean updateClassExp, boolean override) {
		if (activeClass != null && activeClass != ClassType.UNKNOWN) {
			// Archer:123; Warrior:12; etc
			//If str is null get the string from database.
			if (expStr == null || expStr.isEmpty()) {
				try {
					Statement statement = cwc.getSql().createStatement();
					ResultSet res = statement.executeQuery("SELECT ClassExp FROM Players WHERE UUID='" + getUUID().toString() + "';");
					
					while (res.next()) {
						expStr = res.getString("ClassExp");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}			
			}
			//Split the string etc.
			String[] split1 = expStr.split("; ");
			String[] split2;
			for (int i = 0; i < split1.length; i++) {
				split2 = split1[i].split(":");
				if (split2.length > 1) {
					//Put the xp in cached map with last known values.
					if (override || !exp.containsKey(split2[0])) {
						exp.put(split2[0], (Double)Double.parseDouble(split2[1]));
					}
				}
			}
			if (updateClassExp) {
				cExp.setExp(exp.get(activeClass.getName()));
			}
		}
	}
	
	//Get the ClassExp class.
	public ClassExp getExpClass() {
		return cExp;
	}
	
	public String expToString() {
		String expStr = "";
		for (String key : exp.keySet()) {
			expStr += "" + key + ":" + exp.get(key).toString() + "; ";
		}
		if (expStr.endsWith("; ")) {
			expStr.substring(0, expStr.length() - 2);
		}
		return expStr;
	}
	
	
	
	

	//Bukkit methods
	public Player getPlayer() {
		return Bukkit.getPlayer(getUUID());
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getUUID());
	}
	
	public boolean isOnline() {
		return getPlayer() != null;
	}
	
	public void sendMessage(String msg) {
		getPlayer().sendMessage(msg);
	}
	
	public String getName() {
		return getOfflinePlayer().getName();
	}
	
	
	//Compare by UUID.
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CWPlayer) {
			CWPlayer other = (CWPlayer) obj;

			return other.getUUID() == getUUID();
		}
		return false;
	}
}

package com.clashwars.cwclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.CooldownManager;
import com.clashwars.cwclasses.utils.ParticleEffect;
import com.clashwars.cwclasses.utils.Util;

public class CWPlayer {
	private CWClasses	cwc;
	private UUID		uuid;
	
	private ClassType activeClass;
	private HashMap<String, Double> expData = new HashMap<String, Double>();
	private CooldownManager cdm;
	

	
	public CWPlayer(CWClasses cwc, UUID uid) {
		this.cwc = cwc;
		this.uuid = uid;
		//cExp = new ClassExp(this);
		cdm = new CooldownManager();
	}

	public CWClasses getPlugin() {
		return cwc;
	}
	
	
	//#######################
	//### Custom methods. ###
	//#######################
	public UUID getUUID() {
		return uuid;
	}
	
	public CooldownManager getCDM() {
		return cdm;
	}
	
	//Get the activated class.
	public ClassType getActiveClass() {
		return activeClass;
	}
	
	//Set a new active class.
	public void setActiveClass(ClassType classType) {
		//No previous class so can just set it.
		if (activeClass == null) {
			activeClass = classType;
			return;
		}
		//Save progress from last class.
		expData.put(activeClass.getName(), getExp());
		ClassType prevClass = activeClass;
		activeClass = classType;
		if (saveExp(expToString())) {
			setExp(getExp(activeClass));
		} else {
			activeClass = prevClass;
		}
	}
	
	
	//#########################
	//### Experience stuff. ###
	//#########################
	
	//Add given amount of xp
	public void incrementExp(double amt) {
		incrementExp(activeClass, amt);
		return;
	}
	public void incrementExp(ClassType ct, double amt) {
		if (ct != null) {
			int prevLevel = getLevel();
			amt = Util.roundDouble(amt);
			expData.put(ct.getName(), getExp(ct) + amt);
			
			//Check for level up.
			int level = getLevel();
			if (level != prevLevel) {
				//Call custom ClassLevelupEvent
				ClassLevelupEvent e = new ClassLevelupEvent(this);
				Bukkit.getServer().getPluginManager().callEvent(e);
			}
			ParticleEffect.ENCHANTMENT_TABLE.display(getPlayer().getLocation().add(0f, 2.8f, 0f), 0, 0.5f, 0, 3, (int)Math.max((int)Math.round(amt),1));
		}
		return;
	}

	//Take given amount of xp
	public void decrementExp(double amt) {
		decrementExp(activeClass, amt);
		return;
	}
	public void decrementExp(ClassType ct, double amt) {
		if (ct != null) {
			amt = Util.roundDouble(amt);
			expData.put(ct.getName(), Math.max(getExp(ct) - amt, 0));
		}
		return;
	}
	
	//Set given amount of xp
	public void setExp(double amt) {
		setExp(activeClass, amt);
		return;
	}
	public void setExp(ClassType ct, double amt) {
		if (ct != null) {
			amt = Util.roundDouble(amt);
			expData.put(ct.getName(), Math.max(amt, 0));
		}
		return;
	}
	
	//Get the amount of xp
	public double getExp() {
		return getExp(activeClass);
	}
	public double getExp(ClassType ct) {
		if (!expData.containsKey(ct.getName())) {
			expData.put(ct.getName(), (double)0.0);
		} 
		return Util.roundDouble(expData.get(ct.getName()));
	}
	
	//Calculate level
	public int getLevel() {
		return getLevel(activeClass);
	}
	public int getLevel(ClassType ct) {
		if (ct != null) {
			return getLevel(getExp(ct));
		}
		return -1;
	}
	public int getLevel(double xp) {
		return (int) Math.sqrt(xp / 20);
	}
	
	//Calculate Experience needed for the specified level.
	public int getExpForLevel(int level) {
		return (int) Math.ceil(Math.pow(level, 2) * 20);
	}
	
	//Calculate the xp difference between 2 levels
	public int getExpDiffNextLvl() {
		return getExpDiffNextLvl(activeClass);
	}
	public int getExpDiffNextLvl(ClassType ct) {
		if (ct != null) {
			int lvl = getLevel(ct);
			return getExpDiff(lvl + 1, lvl);
		}
		return -1;
	}
	public int getExpDiff(int highLvl, int lowLvl) {
		return (int)getExpForLevel(highLvl) - getExpForLevel(lowLvl);
	}
	
	//Calculate the xp at the current level.
	public int getExpProgress() {
		return getExpProgress(activeClass);
	}
	public int getExpProgress(ClassType ct) {
		if (ct != null) {
			return getExpProgress(getLevel(ct), getExp(ct));
		}
		return -1;
	}
	public int getExpProgress(int level, double xp) {
		return(int)(getExpDiff(level + 1, level) - (getExpForLevel(level + 1) - xp));
	}
	
	//Convert the map with all experience data to a string.
	//String example: "Archer:123; Warrior:12; " etc
	public String expToString() {
		String expStr = "";
		for (String key : expData.keySet()) {
			expStr += "" + key + ":" + expData.get(key).toString() + "; ";
		}
		if (expStr.endsWith("; ")) {
			expStr.substring(0, expStr.length() - 2);
		}
		return expStr;
	}
	
	//Load in experience data from string.
	//(Load directly from database if string is null)
	public void loadExp(String expStr, boolean override) {
		//If str is null get the xp data from database.
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
		//Split the string to get all xp keys/values.
		String[] split1 = expStr.split("; ");
		String[] split2;
		for (int i = 0; i < split1.length; i++) {
			split2 = split1[i].split(":");
			if (split2.length > 1) {
				//Put the xp in map
				if (override || !expData.containsKey(split2[0])) {
					expData.put(split2[0], (Double)Double.parseDouble(split2[1]));
				}
			}
		}
	}
	
	//Save cached xp in database and save the active class.
	public boolean saveExp() {
		return saveExp(expToString());
	}
	public boolean saveExp(String expStr) {
		try {
			Statement statement = cwc.getSql().createStatement();
			if (statement.executeUpdate("UPDATE Players SET ActiveClass='" + activeClass + "', ClassExp='" + expStr + "' WHERE UUID='" + getUUID().toString() + "';") < 1) {
				sendMessage(Util.formatMsg("&cError connecting to the databse. Can't save your class data."));
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			sendMessage(Util.formatMsg("&cError connecting to the databse. Can't save your class data."));
			return false;
		}
		return true;
	}
	
	

	//#######################
	//### Bukkit methods. ###
	//#######################
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
	
	
	
	//Compare CWPlayers with eachother by UUID
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CWPlayer) {
			CWPlayer other = (CWPlayer) obj;

			return other.getUUID() == getUUID();
		}
		return false;
	}
	
	
	
	//Custom ClassLevelupEvent.
	public static class ClassLevelupEvent extends Event {
		private CWPlayer cwp;
		
	    public ClassLevelupEvent(CWPlayer cwp) {
	    	this.cwp = cwp;
	    }
	    
	    public CWPlayer getCWPlayer() {
	    	return cwp;
	    }

	    private static final HandlerList handlers = new HandlerList();
	    @Override
	    public HandlerList getHandlers() {
	        return handlers;
	    }
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
	}
}

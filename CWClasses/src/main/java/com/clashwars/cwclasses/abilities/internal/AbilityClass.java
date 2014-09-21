package com.clashwars.cwclasses.abilities.internal;

import java.util.HashMap;

import com.clashwars.cwclasses.CWClasses;
import org.bukkit.event.Listener;

public class AbilityClass implements Listener {

    public CWClasses cwc = CWClasses.inst();

	public AbilityType getType() {
		return null;
	}
	
	public int getCooldown() {
		return 0;
	}

    public int getLevel() {
		return 0;
	}

    public boolean isPassive() {
		return false;
	}

    public String getDescription(int level) {
		return "&7No description available.";
	}

    public String getActivationInfo() {
		return "";
	}

    public HashMap<String, Scalable> getScales() {
		return null;
	}
}
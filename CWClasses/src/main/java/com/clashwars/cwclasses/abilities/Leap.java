package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Leap extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.LEAP;
	}

    public String getDescription(int level) {
        return "&7Leap XX blocks forward towards your enemy.";
    }

    public String getActivationInfo() {
        return "&7Block and quickly left click with a sword while sneaking.";
    }

	public int getLevel() {
		return 25;
	}
}

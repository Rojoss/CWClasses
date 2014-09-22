package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class ShadowWalk extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.SHADOWWALK;
	}

    public String getDescription(int level) {
        return "&7You will become invisible for XX seconds.";
    }

    public String getActivationInfo() {
        return "&7Sneak and left click with coal in your hand.";
    }

	public int getLevel() {
		return 25;
	}
}

package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Vitality extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.VITALITY;
	}

	public boolean isPassive() {
		return true;
	}

    public String getDescription(int level) {
        return "&7You will gain a half heart every XX seconds up to 10 extra hearts.";
    }

	public int getLevel() {
		return 25;
	}
}

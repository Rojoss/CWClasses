package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Guard extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.GUARD;
	}

	public int getLevel() {
		return 60;
	}
}

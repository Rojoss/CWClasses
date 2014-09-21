package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class FrostArrow extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.FROSTARROW;
	}

	public int getLevel() {
		return 25;
	}
}

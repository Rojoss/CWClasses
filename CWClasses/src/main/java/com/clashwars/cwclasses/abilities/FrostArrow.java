package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class FrostArrow extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.FROSTARROW;
	}

    public String getDescription(int level) {
        return "&7Frost arrows can be crafted. &8(&5/recipe arrow:2&8) When activated and equipped arrows shot will slow your target.";
    }

    public String getActivationInfo() {
        return "&7Left click your bow to activate frost arrows.";
    }

	public int getLevel() {
		return 25;
	}
}

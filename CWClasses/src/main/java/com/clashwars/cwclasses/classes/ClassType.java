package com.clashwars.cwclasses.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import com.clashwars.cwclasses.utils.Util;

public enum ClassType {
	WARRIOR(1, "Warrior", ChatColor.DARK_RED, "Beserk", "Leap", "Rage", "Cleave"),
	ARCHER(2, "Archer", ChatColor.DARK_GREEN, "TrueStrike", "FrostArrow", "Backflip", "Repel"),
	GUARDIAN(3, "Guardian", ChatColor.DARK_AQUA, "Smash", "Vitality", "Guard", "Absorb"),
	ROGUE(4, "Rogue", ChatColor.DARK_GRAY, "PickPocket", "ShadowWalk", "Backstab", "Assassinate");
	
	
	//Vars
	private static Map<String, ClassType> types;
	private int id;
	private String name;
	private ChatColor color;
	private String[] abilities;
	
	
	//Constructor
	private ClassType (int id, String name, ChatColor color, String... abilities) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.abilities = abilities;
	}
	
	
	//Get classType by name
	public static ClassType fromString(String name) {
        if (types == null) {
            initTypes();
        }
        name = name.toLowerCase();
        
        return types.get(Util.capitalize(name));
    }
	
	//Get ID
	public int getID() {
		return id;
	}
	
	//Get name
	public String getName() {
        return name;
    }
	
	//Get color
	public ChatColor getColor() {
		return color;
	}
	
	//Get abilities
	public String[] getAbilities() {
		return abilities;
	}
	
	
	//Fill hashmap with types.
	private static void initTypes() {
		types = new HashMap<String, ClassType>();
        for (ClassType c : values()) {
        	types.put(c.name, c);
        }
    }
}

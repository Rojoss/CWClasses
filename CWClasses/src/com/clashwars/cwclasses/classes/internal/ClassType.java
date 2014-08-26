package com.clashwars.cwclasses.classes.internal;

import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

import org.bukkit.ChatColor;

import com.clashwars.cwclasses.classes.Archer;
import com.clashwars.cwclasses.classes.Guardian;
import com.clashwars.cwclasses.classes.Rogue;
import com.clashwars.cwclasses.classes.Warrior;

public enum ClassType {
	WARRIOR(0, "Warrior", ChatColor.DARK_RED, Warrior.class, "ability1", "ability2", "etc"),
	ARCHER(1, "Archer", ChatColor.DARK_GREEN, Archer.class),
	GUARDIAN(2, "Guardian", ChatColor.DARK_AQUA, Guardian.class),
	ROGUE(3, "Rogue", ChatColor.DARK_GRAY, Rogue.class);
	
	
	//Vars
	private static Map<String, ClassType> types;
	private int id;
	private String name;
	private ChatColor color;
	private Class<? extends ClassClass> clazz;
	private String[] abilities;
	
	
	//Constructor
	private ClassType (int id, String name, ChatColor color, Class<? extends ClassClass> clazz, String... abilities) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.clazz = clazz;
		this.abilities = abilities;
	}
	
	
	//Get classType by name
	public static ClassType fromString(String name) {
        if (types == null) {
            initTypes();
        }
        return types.get(name);
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
	
	//Get the class Class
	public Class<? extends ClassClass> getClassClass() {
		return clazz;
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

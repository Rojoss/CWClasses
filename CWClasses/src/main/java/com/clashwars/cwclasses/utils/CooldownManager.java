package com.clashwars.cwclasses.utils;

import java.util.HashMap;

public class CooldownManager {
	
	private HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
	
	
	public void createCooldown(String identifier, long time) {
		if (cooldowns.containsKey(identifier)) {
			cooldowns.get(identifier).setTime(time);
		} else {
			cooldowns.put(identifier, new Cooldown(time));
		}
	}
	
	public void cleanupCooldowns() {
		for (String key : cooldowns.keySet()) {
			if (!cooldowns.get(key).onCooldown()) {
				cooldowns.remove(key);
			}
		}
	}
	
	public void removeCooldowns() {
		cooldowns.clear();
	}
	
	public Cooldown getCooldown(String identifier) {
		return cooldowns.get(identifier);
	}
	
	public HashMap<String, Cooldown> getCooldowns() {
		return cooldowns;
	}
	
	
	public class Cooldown {
		private long endTime;

		public Cooldown(long time) {
			endTime = System.currentTimeMillis() + time;
		}
		
		public long getTimeLeft() {
			return endTime - System.currentTimeMillis();
		}
		
		public long getEndTime() {
			return endTime;
		}
		
		public boolean onCooldown() {
			if (getTimeLeft() > 0) {
				return true;
			}
			return false;
		}
		
		public void setTime(long time) {
			endTime = System.currentTimeMillis() + time;
		}
		
		public void addTime(long time) {
			endTime += time;
		}
		
		public void removeTime(long time) {
			endTime -= time;
		}
	}
}



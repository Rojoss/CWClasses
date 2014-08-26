package com.clashwars.cwclasses;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CWPlayer {
	private CWClasses	cwc;
	private UUID		uuid;

	

	public CWPlayer(CWClasses cwc, UUID uid) {
		this.cwc = cwc;
		this.uuid = uid;
	}

	public CWClasses getPlugin() {
		return cwc;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	//Custom methods.
	
	

	//Bukkit methods
	public Player getPlayer() {
		return Bukkit.getPlayer(getUUID());
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getUUID());
	}
	
	public boolean isOnline() {
		return getPlayer() != null;
	}
	
	public void sendMessage(String msg) {
		getPlayer().sendMessage(msg);
	}
	
	public String getName() {
		return getOfflinePlayer().getName();
	}

	
	
	/*
	public void fetchData() {
		SqlConnection sql = cwc.getSQLConnection();

		ArrayList<Map<String, Object>> read = sql.read("users", "WHERE id = ?", getId());
		if (!read.isEmpty()) {
			Map<String, Object> assoc = read.get(0);
			interpretData(assoc);
		}
	}

	public void interpretData(Map<String, Object> assoc) {
		nick = (String) assoc.get("nick");
		tag = (String) assoc.get("tag");
		gm = (Integer) assoc.get("gm");
		god = (boolean) assoc.get("god");
		vanished = (boolean) assoc.get("vanished");
		flying = (boolean) assoc.get("flying");
		walkSpeed = (float) assoc.get("walkSpeed");
		flySpeed = (float) assoc.get("flySpeed");
		maxHealth = (Integer) assoc.get("maxHealth");
		powertools = (String) assoc.get("powertools");

	}
	*/

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CWPlayer) {
			CWPlayer other = (CWPlayer) obj;

			return other.getUUID() == getUUID();
		}
		return false;
	}
}

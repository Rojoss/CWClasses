package com.clashwars.cwclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.clashwars.cwclasses.classes.ClassType;
import com.clashwars.cwclasses.utils.Util;

public class PlayerManager {
	private CWClasses					cwc;
	private Map<UUID, CWPlayer>			players	= new HashMap<UUID, CWPlayer>();

	public PlayerManager(CWClasses cwc) {
		this.cwc = cwc;
	}

	public void populate() {
		players.clear();
		
		//Players database
		// 0:ID 1:UUID 2:Name 3:ActiveClass 4:ClassExp
		try {
			Statement statement = cwc.getSql().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM Players;");
			
			while (res.next()) {
				UUID uuid = UUID.fromString(res.getString("UUID"));
				if (!players.containsKey(uuid)) {
					CWPlayer cwp = new CWPlayer(cwc, uuid);
					players.put(uuid, cwp);
				}
				players.get(uuid).setActiveClass(ClassType.fromString(res.getString("ActiveClass")));
				players.get(uuid).updateExp(res.getString("ClassExp"), true, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public CWPlayer getPlayer(UUID uuid) {
		return players.get(uuid);
	}

	public CWPlayer getPlayer(String uuid) {
		return getPlayer(UUID.fromString(uuid));
	}

	public boolean removePlayer(UUID uuid) {
		return players.remove(uuid) != null;
	}

	public boolean removePlayer(String uuid) {
		return removePlayer(UUID.fromString(uuid));
	}

	public CWPlayer getOrCreatePlayer(UUID uuid) {
		CWPlayer player = players.get(uuid);
		if (player != null) {
			return player;
		}

		player = new CWPlayer(cwc, uuid);
		//Check for player in database.
		try {
			Statement statement = cwc.getSql().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM Players WHERE UUID='" + uuid + "';");
			if (!res.isBeforeFirst() ) {
				//New player with no data in the database yet.
				try {
					Statement statement2 = cwc.getSql().createStatement();
					statement2.executeUpdate("INSERT INTO Players (UUID, ItemCategory, UUID, Name) VALUES ('" + uuid.toString() + "', '" + player.getName() + "');");
				} catch (SQLException e) {
					player.sendMessage(Util.formatMsg("&cError connecting to the databse. Can't create class data."));
					e.printStackTrace();
				}
			} else {
				//Exisiting player so update the CWPlayer before returning it.
				while (res.next()) {
					player.setActiveClass(ClassType.fromString(res.getString("ActiveClass")));
					player.updateExp(res.getString("ClassExp"), true, true);
				}
			}
		} catch (SQLException e) {
			player.sendMessage(Util.formatMsg("&cError connecting to the databse. Can't load class data."));
			e.printStackTrace();
		}
		
		players.put(uuid, player);
		return player;
	}

	public CWPlayer getOrCreatePlayer(String uuid) {
		return getOrCreatePlayer(UUID.fromString(uuid));
	}
}

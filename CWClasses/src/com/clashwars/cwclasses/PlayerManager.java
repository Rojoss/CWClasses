package com.clashwars.cwclasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
				int id = res.getInt("ID");
				UUID uuid = UUID.fromString(res.getString("UUID"));
				if (!players.containsKey(uuid)) {
					CWPlayer cwp = new CWPlayer(cwc, uuid);
					players.put(uuid, cwp);
				}
				//players.get(uuid).setActiveClass(Class.fromString(res.getString("ActiveClass")));
				//players.get(uuid).setExp(res.getString("ClassExp"));
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
		players.put(uuid, player);
		return player;
	}

	public CWPlayer getOrCreatePlayer(String uuid) {
		return getOrCreatePlayer(UUID.fromString(uuid));
	}
}

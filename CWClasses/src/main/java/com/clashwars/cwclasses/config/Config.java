package com.clashwars.cwclasses.config;

import com.clashwars.cwclasses.sql.SqlInfo;


public class Config {

	private SqlInfo sqlInfo;
	
	
	public SqlInfo getSqlInfo() {
		return sqlInfo;
	}

	public void setSqlInfo(SqlInfo sqlInfo) {
		this.sqlInfo = sqlInfo;
	}
}

package com.example.campusmap.database;

import android.provider.BaseColumns;

public interface TableDefinition extends BaseColumns{
	
	public static final String TABLE_NAME = "BuildingDetails";
	public static final String BUILDING_ID = _ID;
	public static final String BUILDING_NAME = "BuildingName";
	public static final String BUILDING_ADDRESS = "BuildingAddress";
	
}

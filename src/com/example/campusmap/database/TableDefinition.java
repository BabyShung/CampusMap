package com.example.campusmap.database;

import android.provider.BaseColumns;

public interface TableDefinition extends BaseColumns{
	
	public static final String CAMPUSMAP_DATABASE = "CampusMap_Database.db";
	
	//Building table
	public static final String BUILDING_TABLE = "Building";
	public static final String BUILDING_ID = "Bid";
	public static final String BUILDING_NAME = "BuildingName";
	public static final String BUILDING_ADDRESS = "BuildingAddress";
	public static final String LOCATION_LAT = "Location_lat";
	public static final String LOCATION_LNG = "Location_lng";
	public static final String QUERY_TIME = "QueryTime";
	public static final String BUILDING_ICON = "BuildingIcon";
	
	//Route table
	public static final String ROUTE_TABLE = "Route";
	public static final String ROUTE_ID = "Rid";
	public static final String ROUTE_FILENAME = "RouteFileName";
	public static final String STARTING_LAT = "Starting_lat";
	public static final String STARTING_LNG = "Starting_lng";
	public static final String ENDING_LAT = "Ending_lat";
	public static final String ENDING_LNG = "Ending_lng";
	public static final String DISTANCE = "Distance";
	public static final String TAKETIME = "TakeTime";

	
	public static final String CREATE_TIME = "CreateTime";
	public static final String UPDATE_TIME = "UpdateTime";
	
	//RouteHistory table
	public static final String ROUTE_HISTORY_TABLE = "RouteHistory";
	
}

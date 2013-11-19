package com.example.campusmap.database;


import com.example.campusmap.BuildingDrawing.Building;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBData implements TableDefinition{

	 private SQLiteDatabase database;
	 private DatabaseEntry dbHelper;
	 
	 public DBData(Context context) {
		    dbHelper = new DatabaseEntry(context);
		  }
	 public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		    
		  }
	 public void close() {
		    dbHelper.close();
		  }
	 public void insertData()
	 {
		 database.execSQL("INSERT INTO BuildingTable ("+BUILDING_NAME+","+BUILDING_ADDRESS+") VALUES ('MClean','address1');");
		 database.execSQL("INSERT INTO BuildingTable ("+BUILDING_NAME+","+BUILDING_ADDRESS+") VALUES ('Jessup Hall','address2');");
		 database.execSQL("INSERT INTO BuildingTable ("+BUILDING_NAME+","+BUILDING_ADDRESS+") VALUES ('Schaeffers hall','address3');");
		 
 	 }
	 public Cursor readData()
     {
     	String[] FROM = { BUILDING_NAME, };
     	String ORDER_BY = BUILDING_NAME + " ASC";
     	
     	Cursor cursor = database.query("BuildingTable", FROM, null, null, null, null, ORDER_BY);
     	
     	cursor.moveToFirst();
     	return cursor;
     	
     }
}

package com.example.campusmap.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseEntry extends SQLiteOpenHelper implements TableDefinition {

	public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "BuildingDetails.db";
   
    public DatabaseEntry(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
 		
       
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
     	
 		db.execSQL("CREATE TABLE IF NOT EXISTS BuildingTable("+BUILDING_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+BUILDING_NAME+" VARCHAR,"+BUILDING_ADDRESS+" VARCHAR);");
   
     }
     
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
     {
     	db.execSQL("DROP TABLE IF EXISTS BuildingTable");
     	onCreate(db);
     }
     
}

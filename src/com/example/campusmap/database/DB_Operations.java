package com.example.campusmap.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DB_Operations implements TableDefinition {

	private SQLiteDatabase database;

	private Context passed_context;

	public void getDBPath() {
		System.out.println("DB Path: " + database.getPath());
	}

	public DB_Operations(Context context) {
		passed_context = context;
	}

	public void open() throws SQLException {

		database = SQLiteDatabase.openOrCreateDatabase(
				Environment.getExternalStorageDirectory() + "/CampusMap/"
						+ CAMPUSMAP_DATABASE, null);

	}

	public void close() {
		database.close();
	}

	public void insert(String bn, String ba) {
		// ContentValues cv = new ContentValues();
		// cv.put(bn, "MClean Hall");
		// cv.put(ba, "address1");
		// database.insert("Building", null, cv);
	}

	public Cursor readData() {
		String[] FROM = { BUILDING_NAME };
		String ORDER_BY = BUILDING_NAME + " ASC";
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, ORDER_BY);

		return cursor;

	}

	public ArrayList<String> getBuildingNames() {
		Cursor c = this.readData();
		ArrayList<String> result = new ArrayList<String>();

		int iBN = c.getColumnIndex(BUILDING_NAME);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result.add(c.getString(iBN));
			System.out.println("test----------" + c.getString(iBN));
		}

		return result;
	}

	public boolean BuildingTable_isEmpty() {
		String[] FROM = { BUILDING_ID };
		Cursor c = database.query(BUILDING_TABLE, FROM, null, null, null, null,
				null);
		System.out.println("test---------cursor count------" + c.getCount());
		if (c.getCount() == 0)
			return true;
		else
			return false;
	}

	public void DB_init() {

		if (BuildingTable_isEmpty()) {
			// if empty, insert the list
			initialize_Building(BUILDING_NAME, BUILDING_ADDRESS, LOCATION_LAT,
					LOCATION_LNG);
		}
	}

	private void initialize_Building(String bn, String ba, String lat,
			String lng) {
		System.out.println("test----------insert~~~~~");
		database.execSQL("INSERT INTO Building (" + bn + "," + ba + "," + lat
				+ "," + lng + ") VALUES ('MClean Hall','address1','41.66069993335234','-91.53652489185333');");
		database.execSQL("INSERT INTO Building (" + bn + "," + ba + "," + lat
				+ "," + lng + ") VALUES ('Jessup Hall','address2','41.66193405068999','-91.53651483356953');");
		database.execSQL("INSERT INTO Building (" + bn + "," + ba + "," + lat
				+ "," + lng + ") VALUES ('Schaeffer hall','address3','41.66069943238658','-91.53566591441631');");
		database.execSQL("INSERT INTO Building (" + bn + "," + ba + "," + lat
				+ "," + lng + ") VALUES ('Macbride Hall','address4','41.661910255267784','-91.5356632322073');");
		database.execSQL("INSERT INTO Building (" + bn + "," + ba + "," + lat
				+ "," + lng + ") VALUES ('Old Capitol Mall','address5','41.659275671930864','-91.5354198217392');");

		System.out.println("test----------insert!!!!");
		// need to add more
	}
}

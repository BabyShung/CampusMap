package com.example.campusmap.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.campusmap.db_object.DB_Building;
import com.google.android.gms.maps.model.LatLng;
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

	public DB_Operations() {
	}

	public DB_Operations(Context context) {
		passed_context = context;
	}

	public void open() throws SQLException {
		//open db in the CampusMap folder
		database = SQLiteDatabase.openOrCreateDatabase(
				Environment.getExternalStorageDirectory() + "/CampusMap/"
						+ CAMPUSMAP_DATABASE, null);
	}

	public void close() {
		database.close();
	}


	
//-------------------------------------Cursor should be private--------------------------
	private Cursor readData() {
		String[] FROM = { BUILDING_NAME, QUERY_TIME ,UPDATE_TIME};
		String ORDER_BY = UPDATE_TIME+" DESC,"+QUERY_TIME + " DESC,"+ BUILDING_NAME + " ASC";
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, ORDER_BY);

		return cursor;

	}

	private Cursor readDataFromABuildingName(String bn) {
		String[] FROM = { QUERY_TIME };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, BUILDING_NAME+"='"+bn+"'", null, null,
				null, null);
		return cursor;
	}
	
	private Cursor readCenterPointFromBuildings() {
		String[] FROM = { LOCATION_LAT,LOCATION_LNG };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, null);
		return cursor;
	}
	
	//get route info : Route_id, create_time
	//Aish, you might add more columns here
	//serve for getRouteInfo()
	private Cursor readRouteData() {
		String[] FROM = { ROUTE_ID, CREATE_TIME };
		Cursor cursor = database.query(ROUTE_TABLE, FROM, null, null, null,
				null, null);
		return cursor;

	}

	
//----------------------------------------------------------------------------------------
	
//-------------------------------------public get/read/select methods--------------------------	
	public ArrayList<String> getBuildingNames() {
		Cursor c = this.readData();
		ArrayList<String> result = new ArrayList<String>();

		int iBN = c.getColumnIndex(BUILDING_NAME);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result.add(c.getString(iBN));
		}
		return result;
	}
	
	//return query_times as well
	public ArrayList<DB_Building> getBuildingNamesWithTimes() {
		Cursor c = this.readData();
		ArrayList<DB_Building> result = new ArrayList<DB_Building>();

		int iBN = c.getColumnIndex(BUILDING_NAME);
		int iQT = c.getColumnIndex(QUERY_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			
			DB_Building dbb = new DB_Building(c.getString(iBN), c.getInt(iQT));
			result.add(dbb);

		}
		return result;
	}

	public ArrayList<LatLng> getCenterPointsFromBuildings() {
		Cursor c = this.readCenterPointFromBuildings();
		ArrayList<LatLng> centerPoints = new ArrayList<LatLng>();
		int iLat = c.getColumnIndex(LOCATION_LAT);
		int iLng = c.getColumnIndex(LOCATION_LNG);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			centerPoints.add(new LatLng(c.getDouble(iLat),c.getDouble(iLng)));
			//System.out.println(c.getDouble(iLat)+" - "+c.getDouble(iLng));
		}
		return centerPoints;
	}
	
	//get route info : Route_id, create_time
	//Aish, you might add more columns here
	public ArrayList<String> getRouteInfo() {
		Cursor c = this.readRouteData();
		ArrayList<String> result = new ArrayList<String>();

		int iRid = c.getColumnIndex(ROUTE_ID);
		int iRCT = c.getColumnIndex(CREATE_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			result.add("Route_" + c.getString(iRid) + ":     "
					+ c.getString(iRCT));
		}
		return result;
	}
	
	//input a building name, get its LatLng from the Building Table
	public LatLng getLatLngFromDB(String bn) {
		String[] columns = new String[] { LOCATION_LAT, LOCATION_LNG };
		Cursor c = database.query(BUILDING_TABLE, columns, BUILDING_NAME + "='"
				+ bn + "'", null, null, null, null);
		if (c.getCount() != 0) {
			int ilat = c.getColumnIndex(LOCATION_LAT);
			int ilng = c.getColumnIndex(LOCATION_LNG);
			c.moveToFirst();
			double lat = c.getDouble(ilat);
			double lng = c.getDouble(ilng);
			return new LatLng(lat, lng);
		} else
			return null;
	}

//----------------------------------------------------------------------------------------
	
//-------------------------------------public insert methods--------------------------		
	
	//insert, currently just insert the fileName ("Route1.txt")
	//Aish, you need to add more columns, this is consistent with getRouteInfo
	public void insertARoute(String db_fn) {
		ContentValues cv = new ContentValues();
		cv.put(ROUTE_FILENAME, db_fn);
		database.insert(ROUTE_TABLE, null, cv);
	}
	
	
	
//----------------------------------------------------------------------------------------
		
	
//-------------------------------------public update methods--------------------------	
	
	//***** Aish, if you have time, can think about what to update
	
	
	
	public void updateQueryTimesForABuilding(String bn) {
		Cursor c = readDataFromABuildingName(bn);
		if(c.getCount() != 0){
			c.moveToFirst();
			int qt = c.getInt(0);
			qt++;
			ContentValues cv = new ContentValues();
			cv.put(QUERY_TIME, qt);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();	
			cv.put(UPDATE_TIME, dateFormat.format(date));
			database.update(BUILDING_TABLE, cv, BUILDING_NAME + "='"
				+ bn + "'", null);
		}	
	}
	
	
//----------------------------------------------------------------------------------------
	
	
//-------------------------------------public delete methods--------------------------	
	
	//***** Aish, if you have time, can think about what to delete
	
//----------------------------------------------------------------------------------------
	

	
	
	
//-------------------------------------public other methods--------------------------		

	//check if building_table is empty
	public boolean BuildingTable_isEmpty() {
		String[] FROM = { BUILDING_ID };
		Cursor c = database.query(BUILDING_TABLE, FROM, null, null, null, null,
				null);
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

	//insert all the building info into the Building table
	private void initialize_Building(String bn, String ba, String lat,
			String lng) {
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Mclean Hall','2 West Washington Street, Iowa City, IA 52242','41.660715','-91.536520');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Jessup Hall','5 West Jefferson Street, Iowa City, IA 52242','41.661885','-91.536520');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Schaeffer hall','20 East Washington Street, Iowa City, IA 52242','41.660695','-91.535656');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Macbride Hall','17 North Clinton Street, Iowa City, IA 52242','41.661889','-91.535667');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Old Capitol Museum','21 N Clinton St, Iowa City, IA 52242','41.661284','-91.536155');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Main Library', '125 West Washington Street, Iowa City, IA 52242','41.659533','-91.538440');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Halsey Hall', '28 West Jefferson Street, Iowa City, IA 52242', '41.662859','-91.537147');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('IMU Parking Ramp', '120 North Madison Street, Iowa City, IA 52242','41.663106','-91.538247');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Calvin Hall', '2 West Jefferson Street, Iowa City, IA 52242', '41.662799','-91.536450');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Gilmore Hall', '112 North Capitol Street, Iowa City, IA 52242', '41.662817','-91.535801');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Trowbridge Hall', '123 North Capitol Street, Iowa City, IA 52242','41.663381','-91.536563');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Tippie College of Business','21 East Market Street, Iowa City, IA 52245','41.663311','-91.534987');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Chemistry Bldg', '251 North Capitol Street',	'41.664113','-91.536632');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Engineering Research Facility','330 South Madison Street', '41.656966','-91.537271');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Communications Center', '116 South Madison Street','41.659309','-91.537291');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Recreation Center', '309 South Madison','41.657278','-91.538461');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Iowa Memorial Union', '125 North Madison Street','41.663106','-91.538247');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Lindquist Center', '240 South Madison Street','41.658529','-91.537228');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Seaman Center', '103 South Capitol Street','41.659683','-91.536616');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Old Capitol Mall', '200 South Capitol Street','41.659378','-91.535447');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Pomerantz Center','213 North Clinton Street','41.663893','-91.535720');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Black Honors Center', '221 North Clinton','41.664354','-91.535656');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Lutheran Campus Ministry','109 E. Market Street, Iowa City, IA 52245','41.663961','-91.534894');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Duam Hall', '225 North Clinton Street', '41.664478','-91.535087');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Burge Residence Hall', '301 North Clinton Street','41.665244','-91.535216');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Iowa Advanced Technology Labs','205 North Madison Street','41.664178','-91.538081');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Womens Resource and Action Center','130 North Madison', '41.663415','-91.537273');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('North Campus Parking', '339 North Madison Street','41.665256','-91.536541');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('North Hall', '20 West Davenport Street', '41.666013','-91.536654');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Stanley Residence Hall', '10 East Davenport Street','41.666286','-91.535854');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Currier Residence Hall', '413 North Clinton Street','41.666358','-91.535044');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Dey House', '507 North Clinton Street','41.667245','-91.535060');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Glenn Schaeffer Library','507 North Clinton Street','41.667429','-91.535105');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Jefferson Bldg', '129 East Washington Street','41.659895','-91.533344');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Phillips Hall', '16 North Clinton Street','41.661825','-91.534191');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Biology Bldg', '129 East Jefferson Street','41.661859','-91.533392');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Biology Bldg East','210 East Iowa Avenue','41.661617','-91.532673');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Sciences Library', '120 East Iowa Avenue','41.662029','-91.533827');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Van Allen Hall', '30 North Dubuque Street','41.662166','-91.532148');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Spence Laboratories of Psychology','308 East Iowa Avenue', '41.661553','-91.531311');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Seashore Hall', '301 East Jefferson Street','41.661942','-91.531295');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Stuit Hall', '335 East Jefferson Street','41.662202','-91.530479');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('University Services Blgd','1 West Prentiss', '41.654082','-91.536664');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Hillcrest Hall', '25 Byington Road','41.659260','-91.542399');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Hillcrest Residence Hall','25 Byington Road', '41.659268','-91.542844');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('English Philosophy Building','308 English Philosophy Bldg, Iowa City, IA 52242', '41.660788103266725','-91.53988234698772');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Adler Journalism and Mass Communication Building','104 West Washington, Iowa City, IA 52242', '41.66060775558548','-91.53880879282951');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Becker Communications Studies Bldg','25 South Madison Street, Iowa City, IA 52242', '41.66049503802824','-91.53813119977713');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Boyd Law Building','220 Boyd Law Bldg, Iowa City, IA 52242', '41.65748189935511','-91.54279489070177');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Field House','225 South Grand Avenue, Iowa City, IA 52242', '41.65844078860639','-91.54661938548088');");

		// need to add more
	}



	
}
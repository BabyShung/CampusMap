package com.example.campusmap.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.campusmap.R;
import com.example.campusmap.db_object.DB_Building;
import com.example.campusmap.db_object.DB_Route;
import com.example.campusmap.file_upload.fileUploadTask;
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
	private fileUploadTask uploadTask;

	public void getDBPath() {
		System.out.println("DB Path: " + database.getPath());
	}

	public DB_Operations() {
	}

	public DB_Operations(Context context) {
		passed_context = context;
	}

	public void open() throws SQLException {
		// open db in the CampusMap folder
		database = SQLiteDatabase.openOrCreateDatabase(
				Environment.getExternalStorageDirectory() + "/CampusMap/"
						+ CAMPUSMAP_DATABASE, null);
	}

	public void close() {
		database.close();
	}

	/**
	 * ---------Cursor should be private------------------
	 * 
	 * @return
	 */
	private Cursor readData() {
		String[] FROM = { BUILDING_NAME, QUERY_TIME, BUILDING_ICON, UPDATE_TIME };
		String ORDER_BY = UPDATE_TIME + " DESC," + QUERY_TIME + " DESC,"
				+ BUILDING_NAME + " ASC";
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, ORDER_BY);
		return cursor;
	}

	private Cursor queryOneColumn_readDataFromABuildingName(String bn,
			String columnName) {
		String[] FROM = { columnName };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, BUILDING_NAME
				+ "='" + bn + "'", null, null, null, null);
		return cursor;
	}

	private Cursor queryOneColumn_readDataFromAColumnName(String columnName) {

		// int queryValue = 0;
		String[] FROM = { columnName };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, QUERY_TIME, null,
				null, null, null, null);

		return cursor;
	}

	private Cursor queryOneColumn_readDataFromALatLng(LatLng point,
			String columnName) {
		String[] FROM = { columnName };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, LOCATION_LAT
				+ "='" + point.latitude + "' and " + LOCATION_LNG + "='"
				+ point.longitude + "'", null, null, null, null);
		return cursor;
	}

	private Cursor readCenterPointFromBuildings() {
		String[] FROM = { LOCATION_LAT, LOCATION_LNG };
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, null);
		return cursor;
	}

	// get route info : Route_id, create_time
	// Aish, you might add more columns here
	// serve for getRouteInfo()
	private Cursor readRouteData(boolean normal) {
		String table;
		if (normal) {
			table = ROUTE_TABLE;
		} else {
			table = ROUTE_HISTORY_TABLE;
		}
		String[] FROM = { ROUTE_ID, ROUTE_FILENAME, STARTING_LAT, STARTING_LNG,
				ENDING_LAT, ENDING_LNG, DISTANCE, TAKETIME, CREATE_TIME };
		Cursor cursor = database.query(table, FROM, null, null, null, null,
				null);
		return cursor;

	}

	/**
	 * public get/ read/ select
	 * 
	 * @return
	 */
	public ArrayList<String> getBuildingNames() {
		Cursor c = this.readData();
		ArrayList<String> result = new ArrayList<String>();

		int iBN = c.getColumnIndex(BUILDING_NAME);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result.add(c.getString(iBN));
		}
		return result;
	}

	// return query_times as well
	public ArrayList<DB_Building> getBuildingOBJWithTimes() {
		Cursor c = this.readData();
		ArrayList<DB_Building> result = new ArrayList<DB_Building>();

		int iBN = c.getColumnIndex(BUILDING_NAME);
		int iQT = c.getColumnIndex(QUERY_TIME);
		int iBI = c.getColumnIndex(BUILDING_ICON);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			DB_Building dbb = new DB_Building(c.getString(iBN), c.getInt(iQT),
					c.getInt(iBI));
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
			centerPoints.add(new LatLng(c.getDouble(iLat), c.getDouble(iLng)));
			// System.out.println(c.getDouble(iLat)+" - "+c.getDouble(iLng));
		}
		return centerPoints;
	}

	/**
	 *  get route info 
	 * @param normal
	 * @return
	 */
	public ArrayList<DB_Route> getRouteInfo(boolean normal) {
		Cursor c = this.readRouteData(normal);
		ArrayList<DB_Route> result = new ArrayList<DB_Route>();

		int iRid = c.getColumnIndex(ROUTE_ID);
		int iRfn = c.getColumnIndex(ROUTE_FILENAME);
		int iRslat = c.getColumnIndex(STARTING_LAT);
		int iRslng = c.getColumnIndex(STARTING_LNG);
		int iRelat = c.getColumnIndex(ENDING_LAT);
		int iRelng = c.getColumnIndex(ENDING_LNG);
		int iRd = c.getColumnIndex(DISTANCE);
		int iRtt = c.getColumnIndex(TAKETIME);
		int iRCT = c.getColumnIndex(CREATE_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			//convert int to long
			int tt = c.getInt(iRtt);
			long taketime = Long.parseLong(String.valueOf(tt));
			
			result.add(new DB_Route(c.getInt(iRid), c.getString(iRfn),
					c.getDouble(iRslat),c.getDouble(iRslng),
					c.getDouble(iRelat),c.getDouble(iRelng),
					c.getDouble(iRd),taketime,
					c.getString(iRCT)));
		}
		return result;
	}

	// input a building name, get its LatLng from the Building Table
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

	// input a centerPoint LatLng, return its BN
	public String getBuildingNameFromLatLng(LatLng point) {
		Cursor c = this
				.queryOneColumn_readDataFromALatLng(point, BUILDING_NAME);
		if (c.getCount() != 0) {
			c.moveToFirst();
			int iBN = c.getColumnIndex(BUILDING_NAME);

			return c.getString(iBN);
		} else
			return null;
	}

	// input a centerPoint LatLng, return its Bid
	public int getBidFromLatLng(LatLng point) {
		Cursor c = this.queryOneColumn_readDataFromALatLng(point, BUILDING_ID);
		if (c.getCount() != 0) {
			c.moveToFirst();
			int iBid = c.getColumnIndex(BUILDING_ID);

			return c.getInt(iBid);
		} else
			return -1;
	}

	/**
	 * insert methods
	 * 
	 * 
	 */

	// insert, currently just insert the fileName ("Route1.txt")
	// Aish, you need to add more columns, this is consistent with getRouteInfo
	public void insertARoute(DB_Route drb, boolean connected) {

		// convert long to int
		int take_time_converted = (int) drb.getTakeTime();

		ContentValues cv = new ContentValues();
		cv.put(ROUTE_FILENAME, drb.getFileName());
		cv.put(STARTING_LAT, drb.getStarting_lat());
		cv.put(STARTING_LNG, drb.getStarting_lng());
		cv.put(ENDING_LAT, drb.getEnding_lat());
		cv.put(ENDING_LNG, drb.getEnding_lng());
		cv.put(DISTANCE, drb.getDistance());
		cv.put(TAKETIME, take_time_converted);
		String table;
		if (connected) {
			table = ROUTE_TABLE;
		} else {
			table = ROUTE_HISTORY_TABLE;
		}
		database.insert(table, null, cv);
		System.out.println("inserted..");
	}

	/**
	 * update methods
	 * 
	 * 
	 */

	// ***** Aish, if you have time, can think about what to update

	public void updateQueryTime_setToNULL() {
		Cursor c = queryOneColumn_readDataFromAColumnName(QUERY_TIME);
		if (c.getCount() != 0) {
			c.moveToFirst();
			int qt = 0;

			ContentValues cv = new ContentValues();
			cv.put(QUERY_TIME, qt);

			database.update(BUILDING_TABLE, cv, null, null);
			System.out.println("updated! set to null..");
		}
	}

	public void updateQueryTimesForABuilding(String bn) {
		Cursor c = queryOneColumn_readDataFromABuildingName(bn, QUERY_TIME);
		if (c.getCount() != 0) {
			c.moveToFirst();
			int qt = c.getInt(0);
			qt++;
			ContentValues cv = new ContentValues();
			cv.put(QUERY_TIME, qt);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			cv.put(UPDATE_TIME, dateFormat.format(date));
			database.update(BUILDING_TABLE, cv,
					BUILDING_NAME + "='" + bn + "'", null);
			System.out.println("updated..");
		}
	}

	/**
	 * delete methods
	 * 
	 * 
	 */

	// ***** Aish, if you have time, can think about what to delete
	public void deleteARoute(int id, boolean normal) {
		String table;
		if (normal) {
			table = ROUTE_TABLE;
		} else {
			table = ROUTE_HISTORY_TABLE;
		}
		database.delete(table, ROUTE_ID + "=" + id, null);
		System.out.println("deleted..");
	}

	public void deleteAllRoute() {
		String table;

		table = ROUTE_TABLE;

		database.delete(table, null, null);
		System.out.println("deleted all routes..");
	}

	/**
	 * other methods
	 * 
	 * 
	 */

	// check if building_table is empty
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
					LOCATION_LNG, BUILDING_ICON);
		}
	}

	public void uploadPreviousFailedRoute() {
		ArrayList<DB_Route> al = this.getRouteInfo(false);
		if (al.size() > 0) {
			for (DB_Route tmp : al) {
				uploadTask = new fileUploadTask(tmp, passed_context);
				uploadTask.execute();
				// and then delete the row in RouteHistory
				deleteARoute(tmp.getRid(), false);
			}
		}
	}

	// insert all the building info into the Building table
	private void initialize_Building(String bn, String ba, String lat,
			String lng, String bi) {
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES ('Mclean Hall','2 West Washington Street, Iowa City, IA 52242','41.660715','-91.536520',"
				+ R.drawable.bi1 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES ('Jessup Hall','5 West Jefferson Street, Iowa City, IA 52242','41.661885','-91.536520',"
				+ R.drawable.bi2 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES ('Schaeffer hall','20 East Washington Street, Iowa City, IA 52242','41.660695','-91.535656',"
				+ R.drawable.bi3 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES ('Macbride Hall','17 North Clinton Street, Iowa City, IA 52242','41.661889','-91.535667',"
				+ R.drawable.bi4 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES ('Old Capitol Museum','21 N Clinton St, Iowa City, IA 52242','41.661284','-91.536155',"
				+ R.drawable.bi5 + ");");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Main Library', '125 West Washington Street, Iowa City, IA 52242','41.659533','-91.538440',"
				+ R.drawable.bi6 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Halsey Hall', '28 West Jefferson Street, Iowa City, IA 52242', '41.662859','-91.537147',"
				+ R.drawable.bi7 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('IMU Parking Ramp', '120 North Madison Street, Iowa City, IA 52242','41.663106','-91.538247',"
				+ R.drawable.bi8 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Calvin Hall', '2 West Jefferson Street, Iowa City, IA 52242', '41.662799','-91.536450',"
				+ R.drawable.bi9 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Gilmore Hall', '112 North Capitol Street, Iowa City, IA 52242', '41.662817','-91.535801',"
				+ R.drawable.bi10 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Trowbridge Hall', '123 North Capitol Street, Iowa City, IA 52242','41.663381','-91.536563',"
				+ R.drawable.bi11 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Tippie College of Business','21 East Market Street, Iowa City, IA 52245','41.663311','-91.534987',"
				+ R.drawable.bi12 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Chemistry Bldg', '251 North Capitol Street',	'41.664113','-91.536632',"
				+ R.drawable.bi13 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Engineering Research Facility','330 South Madison Street', '41.656966','-91.537271',"
				+ R.drawable.bi14 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Communications Center', '116 South Madison Street','41.659309','-91.537291',"
				+ R.drawable.bi15 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Recreation Center', '309 South Madison','41.657278','-91.538461',"
				+ R.drawable.bi16 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Iowa Memorial Union', '125 North Madison Street','41.663106','-91.538247',"
				+ R.drawable.bi17 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Lindquist Center', '240 South Madison Street','41.658529','-91.537228',"
				+ R.drawable.bi18 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Seaman Center', '103 South Capitol Street','41.659683','-91.536616',"
				+ R.drawable.bi19 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Old Capitol Mall', '200 South Capitol Street','41.659378','-91.535447',"
				+ R.drawable.bi20 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Pomerantz Center','213 North Clinton Street','41.663893','-91.535720',"
				+ R.drawable.bi21 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Black Honors Center', '221 North Clinton','41.664354','-91.535656',"
				+ R.drawable.bi22 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Lutheran Campus Ministry','109 E. Market Street, Iowa City, IA 52245','41.663961','-91.534894',"
				+ R.drawable.bi23 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Duam Hall', '225 North Clinton Street', '41.664478','-91.535087',"
				+ R.drawable.bi24 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Burge Residence Hall', '301 North Clinton Street','41.665244','-91.535216',"
				+ R.drawable.bi25 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Iowa Advanced Technology Labs','205 North Madison Street','41.664178','-91.538081',"
				+ R.drawable.bi26 + ");");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Womens Resource and Action Center','130 North Madison', '41.663415','-91.537273',"
				+ R.drawable.bi27 + ");");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('North Campus Parking', '339 North Madison Street','41.665256','-91.536541',"
				+ R.drawable.bi28 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('North Hall', '20 West Davenport Street', '41.666013','-91.536654',"
				+ R.drawable.bi29 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Stanley Residence Hall', '10 East Davenport Street','41.666286','-91.535854',"
				+ R.drawable.bi30 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Currier Residence Hall', '413 North Clinton Street','41.666358','-91.535044',"
				+ R.drawable.bi31 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Dey House', '507 North Clinton Street','41.667245','-91.535060',"
				+ R.drawable.bi32 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Glenn Schaeffer Library','507 North Clinton Street','41.667429','-91.535105',"
				+ R.drawable.bi33 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Jefferson Bldg', '129 East Washington Street','41.659895','-91.533344',"
				+ R.drawable.bi34 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Phillips Hall', '16 North Clinton Street','41.661825','-91.534191',"
				+ R.drawable.bi35 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Biology Bldg', '129 East Jefferson Street','41.661859','-91.533392',"
				+ R.drawable.bi36 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Biology Bldg East','210 East Iowa Avenue','41.661617','-91.532673',"
				+ R.drawable.bi37 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Sciences Library', '120 East Iowa Avenue','41.662029','-91.533827',"
				+ R.drawable.bi38 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Van Allen Hall', '30 North Dubuque Street','41.662166','-91.532148',"
				+ R.drawable.bi39 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Spence Laboratories of Psychology','308 East Iowa Avenue', '41.661553','-91.531311',"
				+ R.drawable.bi40 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Seashore Hall', '301 East Jefferson Street','41.661942','-91.531295',"
				+ R.drawable.bi41 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Stuit Hall', '335 East Jefferson Street','41.662202','-91.530479',"
				+ R.drawable.bi42 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('University Services Blgd','1 West Prentiss', '41.654082','-91.536664',"
				+ R.drawable.bi43 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Hillcrest Hall', '25 Byington Road','41.659260','-91.542399',"
				+ R.drawable.bi44 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Hillcrest Residence Hall','25 Byington Road', '41.659268','-91.542844',"
				+ R.drawable.bi45 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('English Philosophy Building','308 English Philosophy Bldg, Iowa City, IA 52242', '41.660788103266725','-91.53988234698772',"
				+ R.drawable.bi46 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Adler Journalism and Mass Communication Building','104 West Washington, Iowa City, IA 52242', '41.66060775558548','-91.53880879282951',"
				+ R.drawable.bi47 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Becker Communications Studies Bldg','25 South Madison Street, Iowa City, IA 52242', '41.66049503802824','-91.53813119977713',"
				+ R.drawable.bi48 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Boyd Law Building','220 Boyd Law Bldg, Iowa City, IA 52242', '41.65748189935511','-91.54279489070177',"
				+ R.drawable.bi49 + ");");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ","
				+ bi
				+ ") VALUES('Field House','225 South Grand Avenue, Iowa City, IA 52242', '41.65844078860639','-91.54661938548088',"
				+ R.drawable.bi50 + ");");

		// need to add more
	}

}
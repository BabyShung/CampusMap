package com.example.campusmap.database;

import java.util.ArrayList;

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

	public void insert(String bn, String ba) {
		// ContentValues cv = new ContentValues();
		// cv.put(bn, "MClean Hall");
		// cv.put(ba, "address1");
		// database.insert("Building", null, cv);
	}

	
//-------------------------------------Cursor should be private--------------------------
	private Cursor readData() {
		String[] FROM = { BUILDING_NAME };
		String ORDER_BY = BUILDING_NAME + " ASC";
		Cursor cursor = database.query(BUILDING_TABLE, FROM, null, null, null,
				null, ORDER_BY);

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
			double lat = Double.valueOf(c.getString(ilat));
			double lng = Double.valueOf(c.getString(ilng));
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
				+ ") VALUES ('Mclean Hall','address1','41.660715','-91.536520');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Jessup Hall','address2','41.661885','-91.536520');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Schaeffer hall','address3','41.660695','-91.535656');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Macbride Hall','address4','41.661889','-91.535667');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES ('Old Capitol Museum','address5','41.661284','-91.536155');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Main Library', 'address6','41.659533','-91.538440');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Halsey Hall', 'address7', '41.662859','-91.537147');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('IMU Parking Ramp', 'address8','41.663106','-91.538247');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Calvin Hall', 'address9', '41.662799','-91.536450');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Gilmore Hall', 'address10', '41.662817','-91.535801');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Trowbridge Hall', 'address11','41.663381','-91.536563');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Tippie College of Business','address12','41.663311','-91.534987');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Chemistry Bldg', 'address13',	'41.664113','-91.536632');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Engineering Research Facility','address14', '41.656966','-91.537271');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Communications Center', 'address15','41.659309','-91.537291');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Recreation Center', 'address16','41.657278','-91.538461');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Iowa Memorial Union', 'address17','41.663106','-91.538247');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Lindquist Center', 'address18','41.658529','-91.537228');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Seaman Center', 'address19','41.659683','-91.536616');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Old Capitol Mall', 'address20','41.659378','-91.535447');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Admission Vistiors Center','address21','41.663893','-91.535720');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Black Honors Center', 'address22','41.664354','-91.535656');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Lutheran Campus Ministry','address23','41.663961','-91.534894');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Duam Hall', 'address24', '41.664478','-91.535087');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Burge Residence Hall', 'address25','41.665244','-91.535216');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Iowa Advanved Technology Labs','address26','41.664178','-91.538081');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Women Resource and Action Center','address27', '41.663415','-91.537273');");

		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('North Campus Parking', 'address28','41.665256','-91.536541');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Space Place Theater', 'address29', '41.666013','-91.536654');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Stanley Residence Hall', 'address30','41.666286','-91.535854');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Currier Residence Hall', 'address31','41.666358','-91.535044');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Writers Workshop', 'address32','41.667245','-91.535060');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Glenn Schaeffer Library','address33','41.667429','-91.535105');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Jefferson Bldg', 'address34','41.659895','-91.533344');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Phillips Hall', 'address35','41.661825','-91.534191');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Biology Bldg', 'address36','41.661859','-91.533392');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Biology Bldg East','address37','41.661617','-91.532673');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Sciences Library', 'address38','41.662029','-91.533827');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Van Allen Hall', 'address39','41.662166','-91.532148');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Spence Laboratories of Psychology','address40', '41.661553','-91.531311');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Seashore Hall', 'address41','41.661942','-91.531295');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Stuit Hall', 'address42','41.662202','-91.530479');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('University Services Blgd','address43', '41.654082','-91.536664');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Hillcrest Hall', 'address44','41.659260','-91.542399');");
		database.execSQL("INSERT INTO Building ("
				+ bn
				+ ","
				+ ba
				+ ","
				+ lat
				+ ","
				+ lng
				+ ") VALUES('Hillcrest Residence Hall','address45', '41.659268','-91.542844');");

		// need to add more
	}

	
}
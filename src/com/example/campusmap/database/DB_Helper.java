package com.example.campusmap.database;

import java.util.ArrayList;

import android.app.Activity;

import com.example.campusmap.db_object.DB_Building;
import com.google.android.gms.maps.model.LatLng;

public class DB_Helper {
	public DB_Helper() {
		
	}

	public LatLng getCenterPointOfABuildingFromDB(String destination) {
		DB_Operations op = new DB_Operations();
		op.open();
		LatLng to = op.getLatLngFromDB(destination);
		op.close();
		return to;
	}
	
	public ArrayList<DB_Building> readBuildingObjectFromDatabase(Activity act) {
		DB_Operations op = new DB_Operations(act);
		op.open();
		ArrayList<DB_Building> value = op.getBuildingOBJWithTimes();
		op.close();
		return value;
	}

	public void updateQueryTimesIntoDatabase(String bn) {
		DB_Operations dbo = new DB_Operations();
		dbo.open();
		dbo.updateQueryTimesForABuilding(bn);
		dbo.close();
	}
}

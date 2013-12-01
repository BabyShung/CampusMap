package com.example.campusmap.direction;

import com.example.campusmap.file.FileOperations;
import com.example.campusmap.location.MyLocation;
import com.example.campusmap.location.MyLocationTask;
import com.google.android.gms.maps.GoogleMap;

import android.graphics.Color;
import android.os.AsyncTask;

// Async <inputtype, progresstype, returntype>
public class RouteRequestTask extends AsyncTask<String, Integer, String> {

	private MyLocation ml;
	private MyLocationTask mt;
	private GoogleMap map;
	private String returnFileName;

	public RouteRequestTask(MyLocation ml, MyLocationTask mt,
			GoogleMap map) {
		this.ml = ml;
		this.mt = mt;
		this.map = map;
	}

	protected void onPreExecute(String b) {
	}

	@Override
	protected String doInBackground(String... arg0) {

		//returnFileName = ml.disableLocationUpdate(mt);

		return null;
	}

	protected void onProgressUpdated(Integer... progress) {
	}
	
	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);
		// also draw the route as well
		Route tmpR = new Route(new FileOperations());
		tmpR.showTestRoute(returnFileName, map, Color.RED);
	}
}
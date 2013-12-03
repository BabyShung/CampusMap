package com.example.campusmap.direction;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.campusmap.MapActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

// Async <inputtype, progresstype, returntype>
public class RouteRequestTask extends AsyncTask<String, Integer, String> {
	
	private MapActivity ma;
	private LatLng fromPosition;
	private LatLng toPosition;
	private GoogleMap map;
	private CampusMapDirection cmd;

	public RouteRequestTask(MapActivity ma, GoogleMap map,
			LatLng fromPosition, LatLng toPosition) {
		this.ma = ma;
		this.map = map;
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
	}


	@Override
	protected String doInBackground(String... arg0) {

		cmd = new CampusMapDirection();
		cmd.initializeJSONOject(fromPosition, toPosition);

		
		return null;
	}
 
	
	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);
		// also draw the route as well
		//Route tmpR = new Route(new FileOperations());
		//tmpR.showTestRoute(returnFileName, map, Color.RED);
		if(cmd.returnJSONObj()==null){
			System.out.println("********bad luck*******");
		}else{
		cmd.getStatus();
		ma.openOptionsMenu();
		}
	}
}
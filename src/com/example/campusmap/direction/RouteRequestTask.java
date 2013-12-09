package com.example.campusmap.direction;

import net.simonvt.messagebar.MessageBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.campusmap.MapActivity;
import com.example.campusmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

// Async <inputtype, progresstype, returntype>
public class RouteRequestTask extends AsyncTask<String, Integer, String> {
	
	private MapActivity ma;
	private LatLng fromPosition;
	private LatLng toPosition;
	private GoogleMap map;
	private CampusMapDirection cmd;
	private MessageBar mMessageBar;
	
	public RouteRequestTask(MapActivity ma, GoogleMap map,
			LatLng fromPosition, LatLng toPosition,MessageBar mMessageBar) {
		this.ma = ma;
		this.map = map;
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
		this.mMessageBar = mMessageBar;
	}


	@Override
	protected String doInBackground(String... arg0) {

		cmd = new CampusMapDirection();
		
		//send request to server
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
			Toast.makeText(ma, "Network error",
					Toast.LENGTH_LONG).show();
		}else{
			cmd.getStatus();
			cmd.test();
	        Bundle b = new Bundle();
	        b.putInt("onMsgClick", 1);
			mMessageBar.show("Request route success!", "Select",R.drawable.ic_messagebar_undo,b);
			//ma.openOptionsMenu();
		}
	}
}
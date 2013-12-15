package com.example.campusmap.direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;

import net.simonvt.messagebar.MessageBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.campusmap.MapActivity;
import com.example.campusmap.R;
import com.example.campusmap.routefilter.ReturnRoute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

// Async <inputtype, progresstype, returntype>
public class RouteRequestTask extends AsyncTask<String, Integer, String> {
	
	private MapActivity ma;
	private LatLng fromPosition;
	private LatLng toPosition;
	private GoogleMap map;
	private CampusMapDirection cmd;
	private MessageBar mMessageBar;
	
	private ArrayList<LatLng> googlePoints;
	private int googleDistance;
	private int googleTime;
	
	
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
 
		//get google route
		GMapV2Direction md = new GMapV2Direction();
		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_WALKING);
		if (doc != null) {
			googlePoints = md.getDirection(doc);
			googleDistance = md.getDistanceValue(doc);
			googleTime = md.getDurationValue(doc);
		}
		
		return null;
	}
 
	
	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);
		
		if(cmd.returnJSONObj()==null){
			Toast.makeText(ma, "Network error",
					Toast.LENGTH_LONG).show();
		}else{
			int status = cmd.getStatus();
			if(status == 1){	
				//at most give back five routes, add the google route and sort the six
				//return the best three base on distance
				cmd.test();
				cmd.initRouteOBJ();
				List<ReturnRoute> rr = cmd.getRoutesArrayList();
				rr.add(new ReturnRoute(googleDistance,googleTime,googlePoints));
				
				Collections.sort(rr);
				
				//draw
				int[] Colors = { Color.RED, Color.BLUE, Color.BLACK};
				int j = 0;
				//draw all the possible route
				for(ReturnRoute tmprr : rr){
					
					System.out.println("**** compare "+tmprr.getDistance());
					
					PolylineOptions rectline;
					
					ArrayList<LatLng> drawP = tmprr.getPoints();
					
					if (drawP != null) {
						rectline = new PolylineOptions().width(4).color(Colors[j]);
						for (int i = 0; i < drawP.size(); i++) {
							rectline.add(drawP.get(i));
						}
						map.addPolyline(rectline);

					}
					j++;
				}
				
				
				
				
			}else{
				//don't have any route,just use google
				
				
				PolylineOptions rectline;
 
				
				if (googlePoints != null) {
					rectline = new PolylineOptions().width(4).color(Color.RED);
					for (int i = 0; i < googlePoints.size(); i++) {
						rectline.add(googlePoints.get(i));
					}
					map.addPolyline(rectline);

				}
			}
			
			//camera
			map.animateCamera(CameraUpdateFactory.newLatLng(fromPosition));
			
	        Bundle b = new Bundle();
	        b.putInt("onMsgClick", 1);
			mMessageBar.show("Request route success!", "Select",R.drawable.ic_messagebar_undo,b);

		}
	}
}
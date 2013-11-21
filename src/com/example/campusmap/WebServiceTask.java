package com.example.campusmap;

import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class WebServiceTask extends AsyncTask<Void, Void, ArrayList<LatLng>> {

	private HomeActivity mContext;
	private PolylineOptions rectline;
	private GoogleMap map;
	private LatLng fromPosition;
	private LatLng toPosition;

	public WebServiceTask(HomeActivity homeActivity, GoogleMap map,
			LatLng fromPosition, LatLng toPosition) {	//constructor
		this.mContext = homeActivity;
		this.map = map;
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
	}

	@Override
	protected ArrayList<LatLng> doInBackground(Void... params) {
		
		GMapV2Direction md = new GMapV2Direction();
		
		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_WALKING);
		
		if (doc != null) {
			
			ArrayList<LatLng> directionPoint = md.getDirection(doc);

			return directionPoint;
		} else
			return null;
	}

	@Override
	protected void onPostExecute(ArrayList<LatLng> result) {
		super.onPostExecute(result);
		
		int[] randomColor = {Color.RED,Color.BLACK,Color.BLUE,Color.YELLOW,Color.CYAN};
		Random rm = new Random();
		int index = rm.nextInt(5);
		
		if (result != null) {
			rectline = new PolylineOptions().width(4).color(randomColor[index]);

			int i;
			for (i = 0; i < result.size(); i++){
				rectline.add(result.get(i));
			}
			map.addPolyline(rectline);


			
			map.animateCamera(CameraUpdateFactory.newLatLng(toPosition));
		
			
			Toast.makeText(mContext, "points for this route: "+i, Toast.LENGTH_SHORT).show();
		}
	}

}

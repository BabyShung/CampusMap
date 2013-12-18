package com.example.campusmap.mapdrawing;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolylineDrawing {

	
	public PolylineDrawing(){
		
	}
	
	
	public Polyline drawLineOnGoogleMap(GoogleMap map, ArrayList<LatLng> points,
			int color,int width) {
		Polyline pl = null;
		PolylineOptions rectline;

		if (points != null) {
			rectline = new PolylineOptions().width(width).color(color);
			for (int i = 0; i < points.size(); i++) {
				rectline.add(points.get(i));
			}
			pl = map.addPolyline(rectline);
			
		}
		return pl;
	}
}

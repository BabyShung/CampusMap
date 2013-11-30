package com.example.campusmap.mapdrawing;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MarkerAndPolyLine {

	private GoogleMap map;
	public MarkerAndPolyLine(GoogleMap map){
		this.map = map;
	}
	
	public MarkerOptions setupMarkerOptions(LatLng point, String title,
			float hue,String snippet,boolean isCampusMarker) {
		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();
		// Setting the position for the marker
		markerOptions.position(point);
		markerOptions.title(title);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hue));
		if(isCampusMarker){
			markerOptions.snippet(snippet);
			map.animateCamera(CameraUpdateFactory.newLatLng(point));
		}
		return markerOptions;

	}
	
	public PolylineOptions setupPolyLineOptions(int color, int width,
			ArrayList<LatLng> al) {
		PolylineOptions polyline = new PolylineOptions();
		polyline.color(color);
		polyline.width(width);
		polyline.addAll(al);
		return polyline;
	}
	
}

package com.example.campusmap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.campusmap.BuildingDrawing.Building;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity implements OnClickListener,
		OnMapClickListener, OnMapLongClickListener {

	// Location of Old Capitol
	private final LatLng LOCATION_OC = new LatLng(41.661272, -91.535964);
	private GoogleMap map;
	private ArrayList<LatLng> arrayPoints = null;

	private LatLng fromPosition = new LatLng(41.661272, -91.535964);
	private LatLng toPosition = new LatLng(41.811456, -90.019527);
	private ArrayList<LatLng> directionPoint;

	private Button btnHOME;
	private Button btnSEARCH;
	private Button btnSETTING;
	private Button btnFUTURE;

	private BuildingDrawing bd;
	private Marker currentMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize map
		mapInitialization();
		setUpHomeCamera(); // Set Home: the location of Old Capitol
		setUpListeners();

		arrayPoints = new ArrayList<LatLng>();

		CallDirection();

		// initialize all the builing-drawing on the map
		bd = new BuildingDrawing(map);
	}

	private void setUpHomeCamera() {
		CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(LOCATION_OC, 18);
		map.animateCamera(update);
		map.addMarker(new MarkerOptions().position(LOCATION_OC)
				.title("Old Capitol").snippet("Welcome to UI!"));

	}

	private void setUpListeners() {
		btnHOME = (Button) findViewById(R.id.mainBTN11);
		btnSEARCH = (Button) findViewById(R.id.mainBTN22);
		btnSETTING = (Button) findViewById(R.id.mainBTN33);
		btnFUTURE = (Button) findViewById(R.id.mainBTN44);

		btnHOME.setOnClickListener(this);
		btnSEARCH.setOnClickListener(this);
		btnSETTING.setOnClickListener(this);
		btnFUTURE.setOnClickListener(this);

		map.setOnMapClickListener(this);
		map.setOnMapLongClickListener(this);

	}

	private void mapInitialization() {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);

	}

	public void onClick(View v) {
		switch (v.getId()) {

		// Back to Home
		case R.id.mainBTN11:
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
					LOCATION_OC, 18);
			map.animateCamera(update);
			map.addMarker(new MarkerOptions().position(LOCATION_OC)
					.title("Old Capitol").snippet("Welcome to UI!"));
			break;

		// Open SearchActivity
		case R.id.mainBTN22:
			Intent searchIntent = new Intent();
			searchIntent.setClass(MainActivity.this, SearchActivity.class);
			startActivity(searchIntent);
			break;

		// Open SettingActivity
		case R.id.mainBTN33:
			Intent settingIntent = new Intent();
			settingIntent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(settingIntent);
			break;

		// Open FutureActivity
		case R.id.mainBTN44:
			Intent futureIntent = new Intent();
			futureIntent.setClass(MainActivity.this, FutureActivity.class);
			startActivity(futureIntent);
		}

	}

	@Override
	public void onMapClick(LatLng point) {
		
		if (bd.pointIsInPolygon(point)) {
			Toast.makeText(this, "it is within!", Toast.LENGTH_SHORT).show();
			
			Building tmpBuilding = bd.getCurrentTouchedBuilding();
			if(tmpBuilding != null)
			{
				addCampusBuildingMaker(point,tmpBuilding.getBuildingName(),tmpBuilding.getAddress());
			}
		} else {
			Toast.makeText(this, "Not within!", Toast.LENGTH_SHORT).show();
			addSimpleMaker(point);
		}
		
		// Clears the previously MapLongClick position
		arrayPoints.clear();
		arrayPoints.add(point);

		

	}

	private void addSimpleMaker(LatLng point) {
		if(currentMarker != null)	//delete the former marker
		{
			currentMarker.remove();
		}
		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();
		// Setting the position for the marker
		markerOptions.position(point);
		markerOptions.title(point.latitude + " : " + point.longitude);
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
		
		currentMarker = map.addMarker(markerOptions.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //GREEN marker on touched position
		currentMarker.showInfoWindow();
		
	}

	private void addCampusBuildingMaker(LatLng point,String bn,String addr) {
		
		if(currentMarker != null)	//delete the former marker
		{
			currentMarker.remove();
		}
		
		
		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();
		// Setting the position for the marker
		markerOptions.position(point);
		markerOptions.title(bn).snippet(addr);
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
		
		currentMarker = map.addMarker(markerOptions.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))); //marker on touched position
		currentMarker.showInfoWindow();
		
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// add marker
		MarkerOptions marker = new MarkerOptions();
		marker.position(point);
		map.addMarker(marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		// set polyline in the map
		arrayPoints.add(point);
		PolylineOptions polyline = new PolylineOptions();
		polyline.color(Color.BLUE);
		polyline.width(5);
		polyline.addAll(arrayPoints);
		map.addPolyline(polyline);
	}

	private void CallDirection() { // Async task

		new WebServiceTask(this, map, fromPosition, toPosition).execute();
	}

	public void setDirectionPoints(ArrayList<LatLng> result) {
		directionPoint = new ArrayList<LatLng>();
		directionPoint = result;
	}

	protected void onResume() {
		super.onResume();
		CallDirection();
	}

}
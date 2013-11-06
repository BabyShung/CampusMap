package com.example.campusmap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initial map
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);

		// Set Home: the location of Old Capitol
		CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(LOCATION_OC, 18);
		map.animateCamera(update);
		map.addMarker(new MarkerOptions().position(LOCATION_OC)
				.title("Old Capitol").snippet("Welcome to UI!"));

		Button btnHOME = (Button) findViewById(R.id.mainBTN11);
		Button btnSEARCH = (Button) findViewById(R.id.mainBTN22);
		Button btnSETTING = (Button) findViewById(R.id.mainBTN33);
		Button btnFUTURE = (Button) findViewById(R.id.mainBTN44);

		btnHOME.setOnClickListener(this);
		btnSEARCH.setOnClickListener(this);
		btnSETTING.setOnClickListener(this);
		btnFUTURE.setOnClickListener(this);

		map.setOnMapClickListener(this);
		map.setOnMapLongClickListener(this);
		
		map.setMyLocationEnabled(true);
		
		arrayPoints = new ArrayList<LatLng>();
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

		// Clears the previously MapLongClick position
		arrayPoints.clear();
		arrayPoints.add(point);
		// Clears the previously MapClick position
		map.clear();
		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting the position for the marker
		markerOptions.position(point);
		// Setting the title for the marker.
		// This will be displayed on taping the marker
		markerOptions.title(point.latitude + " : " + point.longitude);
		// Animating to the touched position
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
		// Placing a GREEN marker on the touched position
		map.addMarker(markerOptions.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		map.addMarker(new MarkerOptions().position(LOCATION_OC)
				.title("Old Capitol").snippet("Welcome to UI!"));

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



}

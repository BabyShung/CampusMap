package com.example.campusmap;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.direction.Route;
import com.example.campusmap.direction.WebServiceTask;
import com.example.campusmap.file.FileOperations;
import com.example.campusmap.location.MyLocation;
import com.example.campusmap.location.MyLocationTask;
import com.example.campusmap.location.MyLocation.LocationResult;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.mapdrawing.BuildingDrawing.Building;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnInfoWindowClickListener {

	private GoogleMap map;
	private ArrayList<LatLng> arrayPoints = null;
	private MyLocation ml;
	private MyLocationTask locationTask;
	private ArrayList<LatLng> directionPoint;
	private BuildingDrawing bd;
	private Marker currentMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setUpBroadCastManager();
		mapInitialization();
		setUpListeners();

		arrayPoints = new ArrayList<LatLng>();

		// GPSInitialization();
		GPS_Network_Initialization();

		// initialize all the builing-drawing on the map
		bd = new BuildingDrawing(map);
	}

	private void setUpBroadCastManager() {
		LocalBroadcastManager.getInstance(this).registerReceiver(
				BuildingNameReceiver, new IntentFilter("GetGoogleDirection"));
	}

	private BroadcastReceiver BuildingNameReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String bn = intent.getStringExtra("BuildingName");

			// search the building lat & lng by bn
			DB_Operations op = new DB_Operations();
			op.open();
			LatLng to = op.getLatLngFromDB(bn);
			op.close();

			// start an ansync task
			Location fromL = ml.getMyLastLocation();
			LatLng from = new LatLng(fromL.getLatitude(), fromL.getLongitude());
			CallDirection(from, to);

		}
	};

	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				BuildingNameReceiver);
		super.onDestroy();
	}

	private void GPS_Network_Initialization() {
		// abstract class, define its abstract method
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {// callback
				setUpMyLocationCamera(location, 17);
			}
		};
		ml = new MyLocation(this, map);
		ml.setupLocation(this, locationResult);

	}

	private void GPSInitialization() {// test, alternative
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, true);
		Location myLocation = lm.getLastKnownLocation(provider);
		setUpMyLocationCamera(myLocation, 17);
	}

	private void setUpMyLocationCamera(Location l, int zoomto) {
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
				l.getLatitude(), l.getLongitude())));
		map.animateCamera(CameraUpdateFactory.zoomTo(zoomto));

	}

	private void setUpListeners() {
		map.setOnMapClickListener(this);
		map.setOnMapLongClickListener(this);
		map.setOnInfoWindowClickListener(this);// click snippet
	}

	private void mapInitialization() {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.setMyLocationEnabled(true);
	}

	@Override
	public void onMapClick(LatLng point) {
		// if the clicked point is in polygon
		if (bd.pointIsInPolygon(point)) {
			// Toast.makeText(this, "it is within!", Toast.LENGTH_SHORT).show();

			Building tmpBuilding = bd.getCurrentTouchedBuilding();
			if (tmpBuilding != null) {
				addCampusBuildingMaker(point, tmpBuilding.getBuildingName(),
						tmpBuilding.getAddress());
			}
		} else {//add a simple marker
			addSimpleMaker(point);
		}

		// Clears the previously MapLongClick position
		arrayPoints.clear();
		arrayPoints.add(point);

	}

	private void addSimpleMaker(LatLng point) {
		if (currentMarker != null) // delete the former marker
		{
			currentMarker.remove();
		}
		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();
		// Setting the position for the marker
		markerOptions.position(point);
		markerOptions.title(point.latitude + " , " + point.longitude);
		currentMarker = map.addMarker(markerOptions
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); 
		currentMarker.showInfoWindow();

	}

	private void addCampusBuildingMaker(LatLng point, String bn, String addr) {

		if (currentMarker != null) // delete the former marker
		{
			currentMarker.remove();
		}

		// Creating a marker
		MarkerOptions markerOptions = new MarkerOptions();
		// Setting the position for the marker
		markerOptions.position(point);
		markerOptions.title(bn).snippet(addr);
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
		currentMarker = map.addMarker(markerOptions
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
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

	private void CallDirection(LatLng from, LatLng to) { // Async task
		new WebServiceTask(this, map, from, to).execute();
	}

	public void setDirectionPoints(ArrayList<LatLng> result) {
		directionPoint = new ArrayList<LatLng>();
		directionPoint = result;
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(marker.getTitle());
		alertDialog.setMessage(marker.getSnippet());

		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						// Async task, begin the route
						locationTask = new MyLocationTask(ml);
						locationTask.execute();
					}
				});

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Stop",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						if (ml != null) {
							String returnFileName = ml
									.disableLocationUpdate(locationTask);
							// also draw the route as well

							Route tmpR = new Route(new FileOperations());
							tmpR.showTestRoute(returnFileName, map, Color.RED);
						}

					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						// ...

					}
				});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();

	}

}

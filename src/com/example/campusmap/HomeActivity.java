package com.example.campusmap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.campusmap.BuildingDrawing.Building;
import com.example.campusmap.MyLocation.LocationResult;
import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.DatabaseEntry;
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

public class HomeActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnInfoWindowClickListener {
	// Location of Old Capitol
	private final LatLng LOCATION_OC = new LatLng(41.661272, -91.535964);
	private GoogleMap map;
	private ArrayList<LatLng> arrayPoints = null;
	private MyLocation ml;
	private MyLocationTask locationTask;

	private LatLng fromPosition = new LatLng(41.661272, -91.535964);
	private LatLng toPosition = new LatLng(41.811456, -90.019527);
	private ArrayList<LatLng> directionPoint;

	private BuildingDrawing bd;
	private Marker currentMarker;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
	
		mapInitialization();
		setUpListeners();
		
		arrayPoints = new ArrayList<LatLng>();

		// GPSInitialization();
		GPS_Network_Initialization();
		CallDirection();

		// initialize all the builing-drawing on the map
		bd = new BuildingDrawing(map);

	}

	private void GPS_Network_Initialization() {
		// abstract class, define its abstract method
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {// callback

				setUpMyLocationCamera(location);
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
		setUpMyLocationCamera(myLocation);

	}

	private void setUpMyLocationCamera(Location l) {

		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
				l.getLatitude(), l.getLongitude())));
		map.animateCamera(CameraUpdateFactory.zoomTo(17));

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

		if (bd.pointIsInPolygon(point)) {
			// Toast.makeText(this, "it is within!", Toast.LENGTH_SHORT).show();

			Building tmpBuilding = bd.getCurrentTouchedBuilding();
			if (tmpBuilding != null) {
				addCampusBuildingMaker(point, tmpBuilding.getBuildingName(),
						tmpBuilding.getAddress());
			}
		} else {
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
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // GREEN
																				// marker
																				// on
																				// touched
																				// position
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
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))); // marker
																				// on
																				// touched
																				// position
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
		// CallDirection();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(marker.getTitle());
		alertDialog.setMessage(marker.getSnippet());

		///////
		String[] tmp = marker.getTitle().replace(" ", "").split(",");
		
		System.out.println(",'"+tmp[0]+"','"+tmp[1]+"'");
		///////
		
		
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						// Async task, begin the route
						locationTask = new MyLocationTask(ml);
						locationTask.execute();
					}
				});

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						if (ml != null) {
							ml.disableLocationUpdate(locationTask);
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

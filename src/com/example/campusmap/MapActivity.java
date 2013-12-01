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
import android.widget.Toast;

import com.example.campusmap.algorithms.NearestPoint;
import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.direction.Route;
import com.example.campusmap.direction.GoogleRouteTask;
import com.example.campusmap.file.FileOperations;
import com.example.campusmap.location.MyLocation;
import com.example.campusmap.location.MyLocationTask;
import com.example.campusmap.location.MyLocation.LocationResult;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.mapdrawing.BuildingDrawing.Building;
import com.example.campusmap.mapdrawing.MarkerAndPolyLine;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnInfoWindowClickListener {

	private GoogleMap map;
	private ArrayList<LatLng> LongClickArrayPoints;
	private MyLocation ml;
	private MyLocationTask locationTask;
	private BuildingDrawing bd;
	private Marker currentMarker;
	private Marker LongClickMarkerA;
	private Marker LongClickMarkerB;
	private Polyline LongClickPolyLine;
	private MarkerAndPolyLine marker_polyline;
	private int LongClickCount;
	private GoogleRouteTask googleDirectionTask;
	private Location myLastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setUpBroadCastManager();
		mapInitialization();
		setUpListeners();

		LongClickArrayPoints = new ArrayList<LatLng>();

		// initialize all the builing-drawing on the map
		bd = new BuildingDrawing(map);

		//camera to my current location
		findMyLocation();
		
		// options for drawing markers and polylines
		marker_polyline = new MarkerAndPolyLine(map);

	}

	private void setUpBroadCastManager() {
		LocalBroadcastManager.getInstance(this).registerReceiver(
				BuildingNameReceiver, new IntentFilter("GetGoogleDirection"));
	}

	private BroadcastReceiver BuildingNameReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (googleDirectionTask != null) {
				Polyline lastGoogleLine = googleDirectionTask.getDrawnLine();
				if (lastGoogleLine != null) {
					lastGoogleLine.remove();
				}
			}
			
			String bn = intent.getStringExtra("BuildingName");
			// search the building lat & lng by bn
			DB_Operations op = new DB_Operations();
			op.open();
			LatLng to = op.getLatLngFromDB(bn);
			op.close();
			// start an ansync task
			Location fromL = myLastLocation;
			LatLng from = new LatLng(fromL.getLatitude(), fromL.getLongitude());
			CallDirection(from, to);

		}
	};



	private void GPS_Network_Initialization() {
		// abstract class, define its abstract method
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {// callback
				// setUpMyLocationCamera(location, 17);
			}
		};
		ml = new MyLocation(this, map);
		ml.setupLocation(this, locationResult);

	}

	private void findMyLocation() {// test, alternative
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, true);
		Location myLocation = lm.getLastKnownLocation(provider);
		setUpMyLocationCamera(myLocation, 17);
		myLastLocation = myLocation;
		
		/**
		 * put below in another method
		 * 
		 */
		// find the nearest building
		DB_Operations op = new DB_Operations(this);
		op.open();
		ArrayList<LatLng> al = op.getCenterPointsFromBuildings();
		
		LatLng origin = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
		NearestPoint np = new NearestPoint(origin,al);
		LatLng result = np.getNearestPoint();
		
		//use this point to get the buildingName in DB
		String bn = op.getBuildingNameFromLatLng(result);
		if(bd.pointIsInPolygon(origin)){
			Toast.makeText(this, "You are now in " + bn,
				Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "Your nearest building is "+ bn,
					Toast.LENGTH_LONG).show();
		}
		op.close();
		
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

		if (LongClickCount == 2 || LongClickArrayPoints.size() == 1) {
			clearMarkersAndLine();
		}

		// if the clicked point is in polygon
		if (bd.pointIsInPolygon(point)) {

			Building tmpBuilding = bd.getCurrentTouchedBuilding();
			if (tmpBuilding != null) {
				addCampusBuildingMaker(point, tmpBuilding.getBuildingName(),
						tmpBuilding.getAddress());
			}
		} else {// add a simple marker
			addSimpleMaker(point);
		}

		// Clears the previously MapLongClick position
		LongClickArrayPoints.clear();
		LongClickArrayPoints.add(point);
		LongClickCount++;
	}

	private void addSimpleMaker(LatLng point) {
		MarkerOptions mo = marker_polyline.setupMarkerOptions(point,
				point.latitude + " , " + point.longitude,
				BitmapDescriptorFactory.HUE_RED, null, false);
		currentMarker = map.addMarker(mo);
		currentMarker.showInfoWindow();

	}

	private void addCampusBuildingMaker(LatLng point, String bn, String addr) {

		MarkerOptions mo = marker_polyline.setupMarkerOptions(point, bn,
				BitmapDescriptorFactory.HUE_RED, addr, true);
		currentMarker = map.addMarker(mo);
		currentMarker.showInfoWindow();
	}

	@Override
	public void onMapLongClick(LatLng point) {
		MarkerOptions mo = marker_polyline.setupMarkerOptions(point, null,
				BitmapDescriptorFactory.HUE_RED, null, false);

		// set polyline in the map
		LongClickArrayPoints.add(point);

		if (LongClickCount == 2) {
			clearMarkersAndLine();
		}

		if (LongClickArrayPoints.size() == 1) {
			LongClickMarkerA = map.addMarker(mo);
		} else { // size is 2
			LongClickMarkerB = map.addMarker(mo);
		}

		if (LongClickArrayPoints.size() == 2) {// draw line

			PolylineOptions plo = marker_polyline.setupPolyLineOptions(
					Color.RED, 5, LongClickArrayPoints);
			LongClickPolyLine = map.addPolyline(plo);

			// start google direction async task
			CallDirection(LongClickArrayPoints.get(0),
					LongClickArrayPoints.get(1));
			LongClickArrayPoints.clear();

		}
		LongClickCount++;
	}

	private void clearMarkersAndLine() {
		// remove the last line
		if (LongClickPolyLine != null) {
			LongClickPolyLine.remove();
		}
		// remove the last two markers
		if (LongClickMarkerA != null) {
			LongClickMarkerA.remove();
		}
		if (LongClickMarkerB != null) {
			LongClickMarkerB.remove();
		}
		// remove currentmarker
		if (currentMarker != null) {
			currentMarker.remove();
		}

		LongClickCount = 0;
		if (googleDirectionTask != null) {
			Polyline lastGoogleLine = googleDirectionTask.getDrawnLine();
			if (lastGoogleLine != null) {
				lastGoogleLine.remove();
			}
		}
	}

	private void CallDirection(LatLng from, LatLng to) { // Async task
		googleDirectionTask = new GoogleRouteTask(this, map, from, to);
		googleDirectionTask.execute();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(marker.getTitle());
		alertDialog.setMessage(marker.getSnippet());

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Go",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						
						//set up MyLocation.class, for recording
						GPS_Network_Initialization();
						
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
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						// ...

					}
				});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();

	}


	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();

	}
	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				BuildingNameReceiver);
		super.onDestroy();
	}
}

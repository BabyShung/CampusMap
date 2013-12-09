package com.example.campusmap.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.campusmap.MapActivity;
import com.example.campusmap.db_object.DB_Route;
import com.example.campusmap.direction.Route;
import com.example.campusmap.file.FileOperations;
import com.example.campusmap.file_upload.fileUploadTask;
import com.example.campusmap.geometry.EnterWhichBuilding;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.mapdrawing.BuildingDrawing.Building;
import com.example.campusmap.routefilter.Location_Hao;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

//This class will be execute in Async task
public class MyLocation implements Runnable {

	private Timer timer1;
	private LocationManager lm;
	private LocationResult locationResult;
	private boolean gps_enabled = false;
	private boolean gps_lost_flag = false;
	private boolean gps_indoor_recording_flag = false;
	private boolean network_enabled = false;
	private boolean timer_cancelled = false;
	private MapActivity mContext;
	private final static int TIME_FOR_GPS_WHEN_NO_NETWORK = 120000;
	private GoogleMap map;
	private Route rr;
	private Thread mythread = null;
	private FileOperations fo;
	private fileUploadTask uploadTask;

	private Location MyLastLocation;
	private boolean isGPSFix;
	private Long mLastLocationMillis;
	private HaoGPSListener myGpsListener;
	private BuildingDrawing bd;
	private int counter = 0;

	// abstract class for call back
	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

	/**
	 * Constructor
	 * 
	 * @param homeActivity
	 * @param map
	 * @param bd
	 */
	public MyLocation(MapActivity homeActivity, GoogleMap map,
			BuildingDrawing bd) {
		this.mContext = homeActivity;
		this.map = map;
		this.bd = bd;
		fo = new FileOperations();
		rr = new Route(fo);
		this.myGpsListener = new HaoGPSListener();
		
		
		
		//testing
//		
//		DB_Route dbr = new DB_Route(41.659926,-91.537904, 41.659946,-91.536904,100,120);
//		dbr.setFileName("MyRoute1_a.txt");
//
//		uploadTask = new fileUploadTask(dbr, mContext);
//		uploadTask.execute();
//		
		
		
	}

	// ---------------------------begin route, stop route-----------------------
	public void beginRoute() {
		// set 'can record'
		rr.toggleRecordState();

		gps_lost_flag = false;
		// initialize file
		fo.fileInitialization("txt");
		// start a thread for recording
		startThread();
	}

	private void startThread() {
		mythread = new Thread(this); // connect to and start the run method
		mythread.start();
	}

	/**
	 * stop route-recording and process and upload, important *
	 * 
	 * @param locationTask
	 * @return
	 */
	public String disableLocationUpdate(MyLocationTask locationTask) {

		String returnName = fo.getFileName();

		if (rr.recordHasStarted()) {

			rr.toggleRecordState();

			// lm.removeUpdates(locationListenerGps);
			// lm.removeUpdates(locationListenerNetwork);

			Toast.makeText(mContext, "File saved as " + returnName,
					Toast.LENGTH_SHORT).show();

			// stop thread
			if (mythread != null) {
				mythread.interrupt();
			}

			// add remaining elements and close buffer
			rr.checkRemainingElementsInBQandCloseBuffer(fo);

			// 1. delete consecutive same lines
			fo.processRecord_delete_consecutive();

			// 2. smooth a little bit using kalmen filter
			for (int i = 0; i < 30; i++)
				fo.processRecord_kalman_filter("a");

			//3. calculate all other route info
			DB_Route returnRoute = fo.calculate_distance_time_andGet_StartEndLatLng();
			
			
			
			if(returnRoute == null){	//invalid route,too short
				
			}else{
			
				returnRoute.setFileName(fo.getProcessedFileName());
				
				/**
				 * Async task, upload txt file to server
				 * also insert in server db
				 */
				uploadTask = new fileUploadTask(returnRoute, mContext);
				uploadTask.execute();
	
				
				
				/**
				 * insert route data into device db
				 */
				fo.insertDataIntoDB(returnRoute);
			}
			
			// counter for gps signal
			counter = 0;

			// iterrupt
			locationTask.cancel(true);
		}
		return returnName;
	}

	/**
	 * Check GPS availability
	 * 
	 * @author haozheng
	 * 
	 */
	private class HaoGPSListener implements GpsStatus.Listener {

		@Override
		public void onGpsStatusChanged(int event) {

			switch (event) {
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				if (MyLastLocation != null) {
					isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 5000;
				} else {
					return;
					// MyLastLocation = getMyLastLocation();
				}

				if (isGPSFix) { // A fix has been acquired.
					System.out.println("GPS running and counter:" + counter);

					//counter>=8, meaning signal comes back
					if (counter >= 8) {
						
						//reset counter
						counter = 0;
						
						//reset so that if signal lost again,can check which building we entered
						gps_lost_flag = false;
						
						// check can keep on recording
						if(gps_indoor_recording_flag == false){
						
							gps_indoor_recording_flag = true;
						}
						
					}

				} else { // lost signal for consecutive 5 seconds
					System.out.println("GPS lost");

					counter = 0;

					if (!gps_lost_flag) { // just one time
						
						Toast.makeText(mContext, "GPS signal lost",
								Toast.LENGTH_SHORT).show();
						
						gps_lost_flag = true;

						// set the flag so that no more recording
						gps_indoor_recording_flag = false;

						// judge which building I go, return a building obj
						EnterWhichBuilding ewb = new EnterWhichBuilding(
								mContext, MyLastLocation, bd);
						Building closestBuilding = ewb
								.getWhichBuildingEntered();
						Toast.makeText(
								mContext,
								"You entered "
										+ closestBuilding.getBuildingName(),
								Toast.LENGTH_LONG).show();
						LatLng enteredBLL = ewb.getEnteredBuildingLatLng();
						System.out.println("Entered Building:  " + enteredBLL);

						// add that center point and add into the route
						if (rr.recordHasStarted()) {
							long ctime = System.currentTimeMillis();
							Location_Hao lh = new Location_Hao(
									enteredBLL.latitude, enteredBLL.longitude,
									ctime);
							
							//can I fix that?
							rr.bufferStore(lh);
							rr.bufferStore(lh);
							rr.bufferStore(lh);
							rr.bufferStore(lh);
							rr.bufferStore(lh);
						}
					}
				}

				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX: // execute only once

				Toast.makeText(mContext, "GPS first time fixed",
						Toast.LENGTH_SHORT).show();

				// sure can record
				gps_indoor_recording_flag = true;
				gps_lost_flag = false;
				counter = 0;

				// if the route has started, set back the flag so that it can
				// keep on recording

				System.out.println("****First Time");

				isGPSFix = true;

				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				System.out.println("****GPS stop");
				break;
			}

		}

	}

	/**
	 * Shared method for location listeners
	 * 
	 * @param location
	 */
	private void checkTimerAndRoute(Location location) {
		
		if (!timer_cancelled) {
			timer_cancelled = true; // no more execute this if
			timer1.cancel();

		}

		//update my location to map_activity
		locationResult.gotLocation(location);

		if (location != null) {
			mLastLocationMillis = SystemClock.elapsedRealtime();
			
			//update my location to this class
			MyLastLocation = location;
			
			//increment my signal counter
			counter++;
		} else {
			return;
		}

		//if a record has been started
		if (rr.recordHasStarted() && gps_indoor_recording_flag) {
			Location_Hao lh = new Location_Hao(location.getLatitude(),
					location.getLongitude(), location.getTime());
			rr.bufferStore(lh);
		}
	}

	/**
	 * define GPS and Network listeners
	 */
	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			checkTimerAndRoute(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			checkTimerAndRoute(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * thread start run method
	 */
	@Override
	public void run() {
		while (true) {
			System.out.println("Taking a hao_location out-----------thread!!");
			rr.bufferTakeAndAddToFile();
		}
	}

	/**
	 * setup the location manager
	 * 
	 * @param context
	 * @param result
	 * @return
	 */
	public boolean setupLocation(Context context, LocationResult result) {

		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {

		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			return false;

		// setup listeners
		if (gps_enabled) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		}
		if (network_enabled) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}

		// add the gps listener
		lm.addGpsStatusListener(myGpsListener);

		timer1 = new Timer();
		timer1.schedule(new RetrieveLastLocation(),
				TIME_FOR_GPS_WHEN_NO_NETWORK);
		return true;
	}

	/**
	 * Hao: get my last location once the listener starts not useful
	 * 
	 * @return
	 */
	public Location getMyLastLocation() {
		Location net_loc = null, gps_loc = null;
		if (gps_enabled) {
			gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (network_enabled) {
			net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		// if there are both values use the latest one
		if (gps_loc != null && net_loc != null) {
			if (gps_loc.getTime() > net_loc.getTime()) {
				return gps_loc;
			} else {
				return net_loc;
			}
		}
		if (gps_loc != null) {
			return gps_loc;
		}
		if (net_loc != null) {
			return net_loc;
		}
		return null;
	}

	/**
	 * inner class, useless
	 * 
	 * @author haozheng
	 * 
	 */
	class RetrieveLastLocation extends TimerTask { // runnable,it's a thread
		@Override
		public void run() {
			try {
				lm.removeUpdates(locationListenerGps);
				lm.removeUpdates(locationListenerNetwork);

				Location net_loc = null, gps_loc = null;
				if (gps_enabled) {
					gps_loc = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
				if (network_enabled) {
					net_loc = lm
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				// if there are both values use the latest one
				if (gps_loc != null && net_loc != null) {
					if (gps_loc.getTime() > net_loc.getTime()) {
						locationResult.gotLocation(gps_loc);
					} else {
						locationResult.gotLocation(net_loc);
					}
					return;
				}
				if (gps_loc != null) {
					locationResult.gotLocation(gps_loc);
					return;
				}
				if (net_loc != null) {
					locationResult.gotLocation(net_loc);
					return;
				}
				locationResult.gotLocation(null);
				return;
			} catch (Exception e) {
				Toast.makeText(mContext, "Can't get any signals",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void removeLMUpdate(){
		lm.removeUpdates(locationListenerGps);
		lm.removeUpdates(locationListenerNetwork);
		lm.removeGpsStatusListener(myGpsListener);
	}
}
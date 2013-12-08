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
	private boolean gps_flag = false;
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

	public MyLocation(MapActivity homeActivity, GoogleMap map,
			BuildingDrawing bd) {
		this.mContext = homeActivity;
		this.map = map;
		this.bd = bd;
		fo = new FileOperations();
		rr = new Route(fo);
		this.myGpsListener = new HaoGPSListener();
	}

	// ---------------------------begin route, stop route-----------------------
	public void beginRoute() {
		// initialize file and start a thread for recording
		rr.toggleRecordState();
		gps_flag = false;
		fo.fileInitialization("txt");
		startThread();
	}

	private void startThread() {
		mythread = new Thread(this); // connect to and start the run method
		mythread.start();
	}

	// stopping a route-recording, important *
	public String disableLocationUpdate(MyLocationTask locationTask) {

		String returnName = fo.getFileName();
		if (rr.recordHasStarted()) {
			rr.toggleRecordState();
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);
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

			// Async task, upload to server
			// send the proceeded txt to the cloud, also insert in server db
			uploadTask = new fileUploadTask(fo.getProcessedFileName(), mContext);
			uploadTask.execute();

			// insert route data into device db
			fo.insertDataIntoDB();

			counter = 0;

			// iterrupt
			locationTask.cancel(true);
		}
		return returnName;
	}

	// ------------------------------------------------------------------------

	// ---------------------------Check GPS availability-----------------------
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

					if (counter >= 8 && gps_indoor_recording_flag == false) {
						counter = 0;
						// can keep on recording
						gps_indoor_recording_flag = true;
						gps_flag = false;
					}

				} else { // The fix has been lost.
					System.out.println("GPS lost");

					counter = 0;

					if (!gps_flag) { // just one time
						Toast.makeText(mContext, "GPS signal lost",
								Toast.LENGTH_SHORT).show();
						gps_flag = true;

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
				gps_flag = false;
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

	// ------------------------------------------------------------------------

	// ------Hao: get my last location once the listener starts------------
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

	// ------------------------------------------------------------------------

	// --------------------------------setup the location manager--------------
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

		lm.addGpsStatusListener(myGpsListener);

		timer1 = new Timer();
		timer1.schedule(new RetrieveLastLocation(),
				TIME_FOR_GPS_WHEN_NO_NETWORK);
		return true;
	}

	// ------------------------------------------------------------------------

	// -------------------------define GPS and Network listeners----------------
	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			// System.out.println("-----------main1");
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
			// System.out.println("-----------main2");
			checkTimerAndRoute(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	// ------------------------------------------------------------------------

	private void checkTimerAndRoute(Location location) {
		if (!timer_cancelled) {
			timer_cancelled = true; // no more execute this if
			timer1.cancel();

		}

		locationResult.gotLocation(location);

		if (location != null) {
			mLastLocationMillis = SystemClock.elapsedRealtime();
			// Do something.
			MyLastLocation = location;
			counter++;
		} else {
			return;
		}

		if (rr.recordHasStarted() && gps_indoor_recording_flag) {
			Location_Hao lh = new Location_Hao(location.getLatitude(),
					location.getLongitude(), location.getTime());
			rr.bufferStore(lh);
		}
	}

	// ------------------------------run
	// definition---------------------------------
	@Override
	public void run() {
		while (true) {
			System.out.println("-----------thread!!");
			rr.bufferTakeAndAddToFile();
		}
	}

	// -----------------------------inner class---------------------------------
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
	// ------------------------------------------------------------------------
}
package com.example.campusmap;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.Toast;

public class MyLocation implements Runnable {
	private Timer timer1;
	private LocationManager lm;
	private LocationResult locationResult;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private boolean timer_cancelled = false;
	private MainActivity mContext;
	private final static int TIME_FOR_GPS_WHEN_NO_NETWORK = 120000;

	private RouteRecord rr;

	private Thread mythread = null;


	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

	public MyLocation(MainActivity mContext) {// constructor
		this.mContext = mContext;
	}

	public boolean setupLocation(Context context, LocationResult result) {
		// LocationResult callback class to pass location value from
		// MyLocation to user code.

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

		if (gps_enabled) {// setup listeners
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);

		}
		if (network_enabled) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}

		rr = new RouteRecord();

		timer1 = new Timer();

		timer1.schedule(new RetrieveLastLocation(),
				TIME_FOR_GPS_WHEN_NO_NETWORK);
		return true;
	}

	public void beginRoute() {
		// initialize file and start a thread for recording
		rr.toggleRecordState();
		rr.fileInitialization();
		startThread();
	}

	private void startThread() {
		mythread = new Thread(this); // connect to and start the run method
		mythread.start();
	}

	public void disableLocationUpdate(MyLocationTask locationTask) {

		if (rr.recordHasStarted()) {
			rr.toggleRecordState();
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);
			Toast.makeText(mContext, "File saved as " + rr.getFileName(),
					Toast.LENGTH_SHORT).show();

			// stop thread
			if (mythread != null) {
				mythread.interrupt();
			}
			
		    //add remaining ele and close buffer
			rr.checkRemainingElementsInBQandCloseBuffer();
			//iterrupt
			locationTask.cancel(true);
		}
	}

	// define a couple of listeners
	LocationListener locationListenerGps = new LocationListener() {

		public void onLocationChanged(Location location) {
			System.out.println("-----------main1");

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
			System.out.println("-----------main2");

			checkTimerAndRoute(location);
			// System.out.println("Lat:"+location.getLatitude()+" Lng:"+location.getLongitude()+" (Network)");
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void checkTimerAndRoute(Location location) {
		if (!timer_cancelled) {
			timer_cancelled = true; // no more execute this if
			timer1.cancel();
			locationResult.gotLocation(location);
		}

		if (rr.recordHasStarted()) {

			if (rr.RWTrue()) { // keep on appending data in the csv file
				rr.bufferStore(location);
			}

		}

	}

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

	@Override
	public void run() {

		while (true) {

			System.out.println("-----------thread!!");

			rr.bufferTakeAndAddToFile();

		}

	}

}
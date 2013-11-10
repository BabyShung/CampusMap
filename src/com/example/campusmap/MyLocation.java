package com.example.campusmap;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocation {
	private Timer timer1;
	private LocationManager lm;
	private LocationResult locationResult;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private MainActivity mContext;
	private final static int TIME_FOR_GPS_WHEN_NO_NETWORK = 60000;

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
	
	public MyLocation(MainActivity mContext){
		this.mContext = mContext;
	}
	
	public boolean setupLocation(Context context, LocationResult result) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.	
		
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			Toast.makeText(mContext, "GPS Error", Toast.LENGTH_SHORT).show();
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
		}
		
		
		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			return false;

		if (gps_enabled){
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
			
		}
		if (network_enabled){
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}
		

		timer1 = new Timer();
 
		timer1.schedule(new RetrieveLastLocation(), TIME_FOR_GPS_WHEN_NO_NETWORK);
		return true;
	}

	public void disableLocationUpdate(){
		lm.removeUpdates(locationListenerGps);
		lm.removeUpdates(locationListenerNetwork);
	}
	
	//define a couple of listeners
	LocationListener locationListenerGps = new LocationListener() {
		
		public void onLocationChanged(Location location) {
			timer1.cancel();
			System.out.println("Lat:"+location.getLatitude()+" Lng:"+location.getLongitude()+" (GPS)");
			locationResult.gotLocation(location);
//			lm.removeUpdates(this);
//			lm.removeUpdates(locationListenerNetwork);
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
			timer1.cancel();
			System.out.println("Lat:"+location.getLatitude()+" Lng:"+location.getLongitude()+" (Network)");
			locationResult.gotLocation(location);
			
//			lm.removeUpdates(this);
//			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	};

	class RetrieveLastLocation extends TimerTask {	//runnable,it's a thread
		@Override
		public void run() {
			

//			lm.removeUpdates(locationListenerGps);
//			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled){
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (network_enabled){
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime()){
					locationResult.gotLocation(gps_loc);
				}
				else{
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
		}
	}
}
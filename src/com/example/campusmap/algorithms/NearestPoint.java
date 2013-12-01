package com.example.campusmap.algorithms;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class NearestPoint {

	private LatLng origin;
	private ArrayList<LatLng> al;

	public NearestPoint(LatLng origin, ArrayList<LatLng> al) {
		this.origin = origin;
		this.al = al;
	}

	// not efficient, can modify some time later
	public LatLng getNearestPoint() {
		LatLng returnPoint = null;
		double min = Double.MAX_VALUE;
		for (LatLng tmp : al) {
			double current = getDistanceSquare(tmp);
			System.out.println("distance:  "+current);
			
			if (current < min) {
				min = current;
				returnPoint = tmp;
			}
		}
		System.out.println("return smallest:  "+min);
		return returnPoint;
	}

	private double getDistanceSquare(LatLng tmp) {

		double result = Math.pow(tmp.latitude - origin.latitude, 2)
				+ Math.pow(tmp.longitude - origin.longitude, 2);
		return result;
	}
}

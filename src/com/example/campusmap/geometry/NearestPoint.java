package com.example.campusmap.geometry;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.maps.model.LatLng;

public class NearestPoint {

	private LatLng origin;
	private ArrayList<LatLng> al;
//	private LatLng mFirst;
//	private LatLng mSecond;
//	private LatLng mThird;
	private ArrayList<LatLng> pointsForBuildingSet;

	public NearestPoint(LatLng origin, ArrayList<LatLng> al) {
		this.origin = origin;
		this.al = al;
		pointsForBuildingSet = new ArrayList<LatLng>();
	}

	public LatLng[] returnClosestFour() {
		LatLng closest_1 = getNearestPoint();
		LatLng closest_2 = getNearestPoint();
		LatLng closest_3 = getNearestPoint();
		LatLng closest_4 = getNearestPoint();
		return new LatLng[]{ closest_1, closest_2, closest_3, closest_4  };
	}
	
	public LatLng[] returnClosestTwoPoints(LatLng[] points) {
		
		pointsForBuildingSet.clear();
		for(LatLng tmpPoint: points)
			pointsForBuildingSet.add(tmpPoint);
		
		LatLng closest_1 = getNearestPointForTwo();
		LatLng closest_2  = getNearestPointForTwo();
		return new LatLng[]{ closest_1, closest_2  };
	}
	

//	public LatLng returnOneOfThreeSmallest(int index) {
//		if (index == 1)
//			return mFirst;
//		else if (index == 2)
//			return mSecond;
//		else if (index == 3)
//			return mThird;
//		else
//			return null;
//	}

	// not efficient, can modify some time later
	private LatLng getNearestPoint() {
		LatLng returnPoint = null;
		double min = Double.MAX_VALUE;
		int i = 0;
		int minIndex = Integer.MAX_VALUE;
		for (LatLng tmp : al) {
			double current = getDistance(tmp);

			if (current < min) {
				min = current;
				returnPoint = tmp;
				minIndex = i;
			}
			i++;
		}
		// delete the min element
		al.remove(minIndex);
		return returnPoint;
	}

	private LatLng getNearestPointForTwo() {
		LatLng returnPoint = null;
		double min = Double.MAX_VALUE;
		int i = 0;
		int minIndex = Integer.MAX_VALUE;
		for (LatLng tmp : pointsForBuildingSet) {
			double current = getDistance(tmp);

			if (current < min) {
				min = current;
				returnPoint = tmp;
				minIndex = i;
			}
			i++;
		}
		// delete the min element
		pointsForBuildingSet.remove(minIndex);
		return returnPoint;
	}
	
	
	// not efficient, can modify some time later
	public double[] sortDistances() {

		double[] distances = new double[al.size()];
		int i = 0;
		for (LatLng tmp : al) {
			double current = getDistance(tmp);
			// assign distance to the double array
			distances[i] = current;
			i++;
		}

		// sort the double array
		Arrays.sort(distances);
		for (double tt : distances) {
			System.out.println("sorting--: " + tt);
		}
		return distances;
	}

	public double[] find2Smallest(double[] arr) {
		double first, second;

		first = second = Double.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < first) {
				second = first;
				first = arr[i];
			} else if (arr[i] < second)
				second = arr[i];
		}
		if (second == Double.MAX_VALUE) {
			System.out.println("There is no second smallest element");
			return null;
		} else
			return new double[] { first, second };
	}

	private double getDistance(LatLng tmp) {

		double result = Math.pow(tmp.latitude - origin.latitude, 2)
				+ Math.pow(tmp.longitude - origin.longitude, 2);
		result = Math.sqrt(result);
		return result;
	}

	private double getDistanceSquare(LatLng tmp) {

		double result = Math.pow(tmp.latitude - origin.latitude, 2)
				+ Math.pow(tmp.longitude - origin.longitude, 2);
		return result;
	}
}

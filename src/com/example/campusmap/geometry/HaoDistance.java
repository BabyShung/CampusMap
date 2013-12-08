package com.example.campusmap.geometry;

import com.google.android.gms.maps.model.LatLng;

public class HaoDistance {

	private LatLng origin;
	private LatLng closest_1;
	private LatLng closest_2;

	public HaoDistance(LatLng origin, LatLng closest_1, LatLng closest_2) {
		this.origin = origin;
		this.closest_1 = closest_1;
		this.closest_2 = closest_2;
	}

	public double getHaoDistance() {

		double origin_closest1 = getEDistance(origin, closest_1);
		double origin_closest2 = getEDistance(origin, closest_2);
		double closest1_closest2 = getEDistance(closest_1, closest_2);

		if (cosDegree(origin_closest1, origin_closest2, closest1_closest2) > 0) {
			// degree<90d, return point to line distance
			System.out.println("used point to distance, < 90 degree");
			return pointToLineDistance();

		} else if (cosDegree(origin_closest1, origin_closest2,
				closest1_closest2) <= 0) {
			// return just ED from origin to closest1;
			System.out.println("used just ED, >= 90 degree");
			return origin_closest1;
		}
		return -1;
	}

	private double pointToLineDistance() {
		double normalLength = Math
				.sqrt((closest_2.latitude - closest_1.latitude)
						* (closest_2.latitude - closest_1.latitude)
						+ (closest_2.longitude - closest_1.longitude)
						* (closest_2.longitude - closest_1.longitude));
		return Math.abs((origin.latitude - closest_1.latitude) * (closest_2.longitude - closest_1.longitude)
				- (origin.longitude - closest_1.longitude) *(closest_2.latitude - closest_1.latitude))
				/ normalLength;
	}

	private double cosDegree(double origin_closest1, double origin_closest2,
			double closest1_closest2) {

		double cosD = (Math.pow(origin_closest1, 2)
				+ Math.pow(closest1_closest2, 2) - Math.pow(origin_closest2, 2))
				/ (2 * origin_closest1 * closest1_closest2);
		return cosD;

	}

	private double getEDistance(LatLng a, LatLng b) {

		double result = Math.pow(a.latitude - b.latitude, 2)
				+ Math.pow(a.longitude - b.longitude, 2);
		result = Math.sqrt(result);
		return result;
	}

}

package com.example.campusmap.geometry;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.location.Location;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.mapdrawing.BuildingDrawing.Building;
import com.google.android.gms.maps.model.LatLng;

public class EnterWhichBuilding {

	private Context mContext;
	private Location MyLastLocation;
	private BuildingDrawing bd;
	private LatLng enteredBuildingLatLng;

	public EnterWhichBuilding(Context mContext, Location MyLastLocation,
			BuildingDrawing bd) {
		this.mContext = mContext;
		this.MyLastLocation = MyLastLocation;
		this.bd = bd;
	}

	/**
	 * determine which building we entered
	 * @author haozheng
	 */
	public Building getWhichBuildingEntered() {

		DB_Operations op = new DB_Operations(mContext);
		op.open();
		
		ArrayList<LatLng> al = op.getCenterPointsFromBuildings();

		//origin
		LatLng origin = new LatLng(MyLastLocation.getLatitude(),
				MyLastLocation.getLongitude());

		NearestPoint np = new NearestPoint(origin, al);

		// calculate and return 4 center points closest to my location
		LatLng[] returnFour = np.returnClosestFour();

		// use center points to get 4 Bid
		int first_bid = op.getBidFromLatLng(returnFour[0]);
		int second_bid = op.getBidFromLatLng(returnFour[1]);
		int third_bid = op.getBidFromLatLng(returnFour[2]);
		int forth_bid = op.getBidFromLatLng(returnFour[3]);

		Map<Integer, Building> tmpBS = bd.getBuildingSet();

		Building firstb = tmpBS.get(first_bid);
		Building secondb = tmpBS.get(second_bid);
		Building thirdb = tmpBS.get(third_bid);
		Building forthb = tmpBS.get(forth_bid);

		// for each building, calculate two closest points and get Hao's defined
		// distance
		HaoDistance hd;

		LatLng[] F_returnTwo = np.returnClosestTwoPoints(firstb.getPoints());

		hd = new HaoDistance(origin, F_returnTwo[0], F_returnTwo[1]);
		double distance_1 = hd.getHaoDistance();

		System.out.println("-------*****  " + distance_1);
		LatLng[] S_returnTwo = np.returnClosestTwoPoints(secondb.getPoints());

		hd = new HaoDistance(origin, S_returnTwo[0], S_returnTwo[1]);
		double distance_2 = hd.getHaoDistance();

		System.out.println("-------*****  " + distance_2);
		LatLng[] T_returnTwo = np.returnClosestTwoPoints(thirdb.getPoints());

		hd = new HaoDistance(origin, T_returnTwo[0], T_returnTwo[1]);
		double distance_3 = hd.getHaoDistance();

		System.out.println("-------*****  " + distance_3);
		LatLng[] Four_returnTwo = np.returnClosestTwoPoints(forthb.getPoints());

		hd = new HaoDistance(origin, Four_returnTwo[0], Four_returnTwo[1]);
		double distance_4 = hd.getHaoDistance();

		System.out.println("-------*****  " + distance_4);

		System.out.println("######origin     " + origin);

		double[] arr = { distance_1, distance_2, distance_3, distance_4 };

		// get minHaodistance's corresponding building

		double minHaoDistance = Double.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < minHaoDistance) {
				minHaoDistance = arr[i];
				minIndex = i;
			}
		}

		System.out.println("######min HaoDistance     " + minHaoDistance);
		
		
		String bn1 = op.getBuildingNameFromLatLng(returnFour[0]);
		System.out.println(bn1 + " " + first_bid);
		String bn2 = op.getBuildingNameFromLatLng(returnFour[1]);
		System.out.println(bn2 + " " + second_bid);
		String bn3 = op.getBuildingNameFromLatLng(returnFour[2]);
		System.out.println(bn3 + " " + third_bid);
		String bn4 = op.getBuildingNameFromLatLng(returnFour[3]);
		System.out.println(bn4 + " " + forth_bid);

		op.close();

		if (minIndex == 0) {
			enteredBuildingLatLng = returnFour[0];
			return firstb;
		} else if (minIndex == 1) {
			enteredBuildingLatLng = returnFour[1];
			return secondb;
		} else if (minIndex == 2) {
			enteredBuildingLatLng = returnFour[2];
			return thirdb;
		} else if (minIndex == 3) {
			enteredBuildingLatLng = returnFour[3];
			return forthb;
		}
		return null;
	}
	
	public LatLng getEnteredBuildingLatLng(){
		return enteredBuildingLatLng;
	}
}

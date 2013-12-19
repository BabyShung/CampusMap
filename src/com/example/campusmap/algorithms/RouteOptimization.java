package com.example.campusmap.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.campusmap.database.DB_Helper;
import com.example.campusmap.geometry.GoogleLatLngDistance;
import com.example.campusmap.geometry.NearestPoint;
import com.example.campusmap.location.MyLocation;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.routefilter.ReturnRoute;
import com.google.android.gms.maps.model.LatLng;

public class RouteOptimization {

	private LatLng myLatLng;
	private ReturnRoute googleRoute;
	private LatLng googleCross;
	private LatLng routeCross;
	private int googleI;
	private int routeI;
	private GoogleLatLngDistance glld;
	private BuildingDrawing bd;
	private DB_Helper dbh;
	private LatLng CenterPoint;
	private NearestPoint np;

	public RouteOptimization(LatLng myLatLng, LatLng CenterPoint,
			BuildingDrawing bd) {
		this.myLatLng = myLatLng;
		this.CenterPoint = CenterPoint;
		this.bd = bd;
		glld = new GoogleLatLngDistance();
		dbh = new DB_Helper();
		np = new NearestPoint();
	}

	public List<ReturnRoute> routeOptimize(List<ReturnRoute> returnRoutes,
			MyLocation ml) {

		for (ReturnRoute rroute : returnRoutes) {
			if (rroute.getType() == -1) {

				googleRoute = rroute;
				break;
			}

		}

		for (ReturnRoute rroute : returnRoutes) {

			int nearestReverseIndex = -1;

			if (rroute.getType() == 2 || rroute.getType() == 3) {

				ArrayList<LatLng> gPoints = googleRoute.getPoints();
				ArrayList<LatLng> rPoints = rroute.getPoints();
				ArrayList<LatLng> newRoutePoints = new ArrayList<LatLng>();

				// check if myPosition within a building
				// if (bd.pointIsInPolygon(myLatLng)) {
				// String bn = bd.getCurrentTouchedBuilding().getBuildingName();
				// myLatLng = dbh.getCenterPointOfABuildingFromDB(bn);
				// }

				// LatLng checkPoint = ml.getEnteredBuildingLatLng();
				// if(checkPoint != null){
				// myLatLng = checkPoint;
				// }

				int LastIndex = rPoints.size() - 1;
				LatLng lastPoint = rPoints.get(LastIndex);
				double distance_last_center = glld.GetDistance(
						lastPoint.latitude, lastPoint.longitude,
						CenterPoint.latitude, CenterPoint.longitude);

				if (distance_last_center > 35) {
					// find the closest one to center point, also the index
					nearestReverseIndex = np.getNearestPointForTwoReverse(
							CenterPoint, rPoints);

				}

				// check this route whether crossed with googleroute
				if (crossWithGoogleRoute(rroute)) {
					// modify rroute.points

					newRoutePoints.add(myLatLng);

					for (int i = 0; i <= googleI; i++) {
						newRoutePoints.add(gPoints.get(i));
					}

					//hao modified
					if (nearestReverseIndex != -1) {

						for (int j = routeI; j < nearestReverseIndex; j++) {
							newRoutePoints.add(rPoints.get(j));
						}
						newRoutePoints.add(CenterPoint);
						
					} else {

						for (int j = routeI; j < rPoints.size(); j++) {
							newRoutePoints.add(rPoints.get(j));
						}
					}

				} else { // not crossed

					// get closest point, also that point not within a building

					int nearestIndex = np.getNearestPointForTwo(myLatLng,
							rPoints, bd);

					newRoutePoints.add(myLatLng);

					
					//hao modified
					if (nearestReverseIndex != -1) {

						for (int j = nearestIndex; j < nearestReverseIndex; j++) {
							newRoutePoints.add(rPoints.get(j));
						}
						newRoutePoints.add(CenterPoint);
						
					} else {

						for (int i = nearestIndex; i < rPoints.size(); i++) {
							newRoutePoints.add(rPoints.get(i));
						}
					}
					


				}

				// also recalculate distance?
				// ..
				double distance = 0;
				LatLng front;
				LatLng rear;
				for (int i = 0; i < newRoutePoints.size() - 1; i++) {
					front = newRoutePoints.get(i);
					rear = newRoutePoints.get(i + 1);
					distance += glld.GetDistance(front.latitude,
							front.longitude, rear.latitude, rear.longitude);
				}

				rroute.setDistance((int) distance);
				rroute.setPoints(newRoutePoints);

			}

		}

		return returnRoutes;
	}

	private boolean crossWithGoogleRoute(ReturnRoute rroute) {

		ArrayList<LatLng> gPoints = googleRoute.getPoints();

		ArrayList<LatLng> rPoints = rroute.getPoints();

		double distance;

		for (int i = 0; i < gPoints.size() / 2; i++) {

			for (int j = 0; j < rPoints.size() / 2; j++) {

				distance = glld.GetDistance(gPoints.get(i).latitude,
						gPoints.get(i).longitude, rPoints.get(j).latitude,
						rPoints.get(j).longitude);

				if (distance <= 5) {
					googleCross = gPoints.get(i);
					routeCross = rPoints.get(j);
					googleI = i;
					routeI = j;
					return true;
				}

			}
		}

		return false;
	}

}

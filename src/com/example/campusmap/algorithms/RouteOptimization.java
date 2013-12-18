package com.example.campusmap.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.campusmap.geometry.GoogleLatLngDistance;
import com.example.campusmap.geometry.NearestPoint;
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

	public RouteOptimization(LatLng myLatLng, BuildingDrawing bd) {
		this.myLatLng = myLatLng;
		glld = new GoogleLatLngDistance();
		this.bd = bd;
	}

	public List<ReturnRoute> routeOptimize(List<ReturnRoute> returnRoutes) {

		for (ReturnRoute rroute : returnRoutes) {
			if (rroute.getType() == -1) {

				googleRoute = rroute;
				break;
			}

		}

		for (ReturnRoute rroute : returnRoutes) {
			
			if (rroute.getType() == 2 || rroute.getType() == 3) {

				ArrayList<LatLng> gPoints = googleRoute.getPoints();
				ArrayList<LatLng> rPoints = rroute.getPoints();
				ArrayList<LatLng> newRoutePoints = new ArrayList<LatLng>();
				
				// check this route whether crossed with googleroute
				if (crossWithGoogleRoute(rroute)) {
					//modify rroute.points
					

					newRoutePoints.add(myLatLng);
					
					for(int i = 0;i<=googleI;i++){
						newRoutePoints.add(gPoints.get(i));						
					}

					for(int j = routeI; j<rPoints.size();j++){
						newRoutePoints.add(rPoints.get(j));
					}
					
					
					
					
				}else{	//not crossed
					
					//get closest point, also that point not within a building
					
					NearestPoint np = new NearestPoint();
					
					int nearestIndex = np.getNearestPointForTwo(myLatLng, rPoints, bd);
					
					newRoutePoints.add(myLatLng);
					
					for(int i = nearestIndex; i<rPoints.size();i++){
						newRoutePoints.add(rPoints.get(i));
					}
					
				}
				
				
				//also recalculate distance?
				//..
				
				
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
						gPoints.get(i).longitude, 
						rPoints.get(j).latitude,
						rPoints.get(j).longitude);
				
				if(distance<=5){
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

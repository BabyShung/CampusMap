package com.example.campusmap;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class BuildingDrawing {

	private Map<Integer, Building> buildingSet;
	private GoogleMap map;
	private Building currentTouchedBuilding;

	public BuildingDrawing(GoogleMap map) {

		buildingSet = new HashMap<Integer, Building>();
		this.map = map;
		drawAllTheCampusBuildings();
	}

	public boolean pointIsInPolygon(LatLng touchPoint) { // return true if point
															// is in polygon
		int i;
		int j;
		boolean result = false;
		for (Building b : buildingSet.values()) {
			// each time, we take one points-set from the hashmap
			LatLng[] bp = b.getPoints();
			// algorithm for detecting a point in a polygon
			for (i = 0, j = bp.length - 1; i < bp.length; j = i++) {
				if ((bp[i].longitude > touchPoint.longitude) != (bp[j].longitude > touchPoint.longitude)
						&& (touchPoint.latitude < (bp[j].latitude - bp[i].latitude)
								* (touchPoint.longitude - bp[i].longitude)
								/ (bp[j].longitude - bp[i].longitude)
								+ bp[i].latitude)) {
					result = !result;
				}
			}
			if (result)// if true, just return
			{
				currentTouchedBuilding = b;
				return result;
			}
		}
		return result;
	}

	public Building getCurrentTouchedBuilding() {
		return currentTouchedBuilding;
	}

	private void drawAllTheCampusBuildings() {

		buildingSet.put(1, new Building("Meclean Hall", "address1", new LatLng(
				41.661017, -91.536656), new LatLng(41.661017, -91.536420),
				new LatLng(41.660418, -91.536420), new LatLng(41.660418,
						-91.536656)));
		buildingSet.put(2, new Building("Jessup Hall", "address2", new LatLng(
				41.662203, -91.536628), new LatLng(41.662203, -91.536393),
				new LatLng(41.661598, -91.536400), new LatLng(41.661598,
						-91.536635)));
		buildingSet.put(2, new Building("Main Library", "address3", new LatLng(
				41.659932, -91.538974), new LatLng(41.659932, -91.537903),
				new LatLng(41.659144, -91.537903), new LatLng(41.659144,
						-91.538167), new LatLng(41.659108, -91.538167),
				new LatLng(41.659108, -91.538680),new LatLng(41.659144, -91.538680), new LatLng(41.659144,
						-91.538974)));

	}

	public class Building {

		private String buildingName;
		private LatLng[] points;
		private String address;

		public Building(String buildingName, String address, LatLng... points) {
			this.buildingName = buildingName;
			this.points = points;
			this.address = address;
			this.drawPloygon();
		}

		public String getBuildingName() {
			return buildingName;
		}

		public LatLng[] getPoints() {
			return points;
		}

		public String getAddress() {
			return address;
		}

		private void drawPloygon() {

			PolygonOptions po = new PolygonOptions();
			for (LatLng point : this.points) {
				po.add(point);
			}
			po.strokeColor(Color.RED).fillColor(Color.BLUE);
			map.addPolygon(po);
		}

	}
}

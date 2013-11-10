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
		//buildingSet is used to search if a touch belongs to a building
		buildingSet.put(1, new Building("Meclean Hall", "address1", new LatLng(
				41.661017, -91.536656), new LatLng(41.661017, -91.536420),
				new LatLng(41.660418, -91.536420), new LatLng(41.660418,
						-91.536656)));
		buildingSet.put(2, new Building("Jessup Hall", "address2", new LatLng(
				41.662203, -91.536628), new LatLng(41.662203, -91.536393),
				new LatLng(41.661598, -91.536400), new LatLng(41.661598,
						-91.536635)));
		buildingSet.put(3, new Building("Schaeffer Hall", "address3", new LatLng(41.660991, -91.5358898) , 
				new LatLng(41.6609937, -91.5357735), 
				new LatLng(41.66100, -91.5357735), 
				new LatLng(41.660997, -91.535718),
				new LatLng(41.66100426, -91.5357195), 

				new LatLng(41.661005,-91.535608), 
				new LatLng(41.660997, -91.535609),  
				new LatLng(41.6609987, -91.5355589),  
				new LatLng(41.66099099, -91.535557),  
				new LatLng(41.6609877, -91.535448),
								 

				new LatLng(41.660844, -91.535449),  
				new LatLng(41.6608412, -91.535561), 
				new LatLng(41.6605646, -91.5355582), 
				new LatLng(41.660562, -91.5354456), 
				new LatLng(41.6604133, -91.5354456), 

				new LatLng(41.66041237, -91.53555996), 
				new LatLng(41.66040436, -91.5355579),
				new LatLng(41.66040587, -91.5356082), 
				new LatLng(41.6604006, -91.5356116), 
				new LatLng(41.6604006, -91.5357246), 

				new LatLng(41.66040611, -91.5357246), 
				new LatLng(41.6604021, -91.535776),
				new LatLng(41.66041714, -91.535776), 
				new LatLng(41.6604156, -91.535885), 
				new LatLng(41.6605616, -91.53589155), 

				new LatLng(41.6605621, -91.5357738), 
				new LatLng(41.6606250, -91.535774), 
				new LatLng(41.660634056, -91.53582282),
				new LatLng(41.6606538, -91.5358496), 

				new LatLng(41.6606879, -91.53586439), 
				new LatLng(41.6607192, -91.5358684), 
				new LatLng(41.6607465, -91.535852), 
				new LatLng(41.660771, -91.5358234), 
				new LatLng(41.660784, -91.5357725), 
								 
				new LatLng(41.6608427, -91.5357715), 
				new LatLng(41.6608445, -91.535889)

							));
		
		buildingSet.put(4, new Building("Macbride Hall", "address4", new LatLng(41.6621840, -91.535876),
				new LatLng(41.6621840, -91.53543826), new LatLng(41.66204, -91.5354382), 
				new LatLng(41.6620402, -91.5355432), new LatLng(41.66194, -91.5355452),
				new LatLng(41.6619393, -91.5355187), new LatLng(41.6618447, -91.5355194),
				new LatLng(41.661847, -91.535543), new LatLng(41.661738, -91.535545),
				new LatLng(41.6617359, -91.5354456), new LatLng(41.66159289, -91.5354499),
				new LatLng(41.6615931, -91.5358892), new LatLng(41.6617339, -91.5358845),
				new LatLng(41.6617354, -91.5357826), new LatLng(41.6618131, -91.535783),
				new LatLng(41.6618263, -91.5358335), new LatLng(41.6618461, -91.535863),
				new LatLng(41.6618789, -91.5358821), new LatLng(41.6619044, -91.5358805),
				new LatLng(41.6619393, -91.5358633), new LatLng(41.66196035, -91.5358385),
				new LatLng(41.6619728, -91.5357829), new LatLng(41.66204200, -91.5357849),
				new LatLng(41.6620445, -91.53587680)
				));
		
		buildingSet.put(5, new Building("Old Capitol Mall", "address5", new LatLng(41.6614684, -91.5362435), new LatLng(41.66146941, -91.5360156),
				new LatLng(41.6613489, -91.5360095), new LatLng(41.66135268, -91.5359703),
				new LatLng(41.6612355, -91.53597135), new LatLng(41.6612347, -91.5360135),
				new LatLng(41.6611355, -91.5360260), new LatLng(41.6611345, -91.53624963),
				new LatLng(41.6612347, -91.5362486), new LatLng(41.6612407, -91.5362630),
				new LatLng(41.6613566, -91.5362677), new LatLng(41.6613582, -91.53624449)
				));
		
		buildingSet.put(6, new Building("Main Library", "address6", new LatLng(
				41.659932, -91.538974), new LatLng(41.659932, -91.537903),
				new LatLng(41.659144, -91.537903), new LatLng(41.659144,
						-91.538167), new LatLng(41.659108, -91.538167),
				new LatLng(41.659108, -91.538680),new LatLng(41.659144, -91.538680), new LatLng(41.659144,
						-91.538974)));

		buildingSet.put(7, new Building("Halsey Hall", "address7", new LatLng(41.663016, -91.53746) , new LatLng(41.663016, -91.53724), new LatLng(41.663037, -91.53724),
				new LatLng(41.66303288, -91.53693325), new LatLng(41.66264064, -91.53693), new LatLng(41.66264, -91.5371777), new LatLng(41.662629, -91.53718), new LatLng(41.662628, -91.537469)
				));

		buildingSet.put(8, new Building("IMU", "address8", new LatLng(41.6633357, -91.53746467), 
				new LatLng(41.663331202, -91.537007689), new LatLng(41.66326833, -91.5370009839),
				new LatLng(41.663266830, -91.536550037), new LatLng(41.66309250, -91.5365453),
				new LatLng(41.6630927, -91.537468)
				));
		
		buildingSet.put(9, new Building("Calvin Hall", "address9", new LatLng(41.66302988, -91.53670158),
				new LatLng(41.66303213, -91.53650209), new LatLng(41.66299732, -91.536503098), 
				new LatLng(41.66299506, -91.5364682), new LatLng(41.66291917, -91.5364645),
				new LatLng(41.66291817, -91.5364350), new LatLng(41.662884357, -91.53643235),
				new LatLng(41.6628816, -91.53629556), new LatLng(41.66285255, -91.53629455),
				new LatLng(41.66285029, -91.5362818), new LatLng(41.66276438, -91.536283828),
				new LatLng(41.66276337, -91.53629355), new LatLng(41.6627278, -91.536295562),
				new LatLng(41.662726307, -91.536383405), new LatLng(41.66268648, -91.53638139),
				new LatLng(41.662685230, -91.536436714), new LatLng(41.66267596, -91.53643637),
				new LatLng(41.662672706, -91.5365255), new LatLng(41.6626869, -91.53653768),
				new LatLng(41.662687774, -91.53658054), new LatLng(41.662727059, -91.53657887),
				new LatLng(41.662727309, -91.53666336), new LatLng(41.66275861, -91.53666503),
				new LatLng(41.662761124, -91.53667878), new LatLng(41.66284879, -91.53667677),
				new LatLng(41.662850543, -91.53666369), new LatLng(41.662881852, -91.5366633),
				new LatLng(41.6628841, -91.5365282), new LatLng(41.66292167, -91.5365248),
				new LatLng(41.66292368, -91.5367009)
				));
		
		buildingSet.put(10, new Building("Gilmore Hall", "address10", new LatLng(41.66302637, -91.53592206),
				new LatLng(41.66302512, -91.5357239), new LatLng(41.66286832, -91.53572324),
				new LatLng(41.66286356, -91.53569977), new LatLng(41.66281723, -91.53570514),
				new LatLng(41.66281447, -91.53572458), new LatLng(41.66265091, -91.535727605),
				new LatLng(41.66265417, -91.53592776), new LatLng(41.662746596, -91.535924747),
				new LatLng(41.66274684, -91.53593949), new LatLng(41.662939461, -91.535937488),
				new LatLng(41.66294021, -91.535924747)
				));
		
		buildingSet.put(11, new Building("Trowbridge Hall", "address11", new LatLng(41.66349025, -91.53693057),
				new LatLng(41.66349100, -91.5363153), new LatLng(41.663230261, -91.53631635),
				new LatLng(41.663230011, -91.53634317), new LatLng(41.66324579, -91.53634384),
				new LatLng(41.66324378, -91.536493375), new LatLng(41.66327835, -91.5364957),
				new LatLng(41.66327860, -91.53692688), new LatLng(41.663461197, -91.536931246)
		));
		
		buildingSet.put(12, new Building("Tippie College of Business", "address12", new LatLng(41.663128068, -91.53594788),
				new LatLng(41.66349551, -91.53594754), new LatLng(41.6634829885, -91.5347720682),
				new LatLng(41.6626361369, -91.5347687155), new LatLng(41.66263638, -91.535270959),
				new LatLng(41.66288034, -91.535264253), new LatLng(41.662870804, -91.535096615),
				new LatLng(41.66324654, -91.5350929275), new LatLng(41.66325305, -91.535596512),
				new LatLng(41.66311754, -91.5355877950), new LatLng(41.66312681, -91.535951904)
		));
		buildingSet.put(13, new Building("Chemistry Bldg", "address13", 
				new LatLng(41.6645955, -91.5369718), new LatLng(41.6645955, -91.5367622),
				new LatLng(41.6643516, -91.5367622), new LatLng(41.6643516, -91.5364759),
				new LatLng(41.6645863, -91.5364759), new LatLng(41.6645863, -91.5362942),
				new LatLng(41.6642291, -91.5362942), new LatLng(41.6642291, -91.5362868),
				new LatLng(41.6640595, -91.5362868), new LatLng(41.6640595, -91.5362942),
				new LatLng(41.6637001, -91.5362942), new LatLng(41.6637001, -91.5364759),
		
				new LatLng(41.6637001, -91.5367237), new LatLng(41.6637001, -91.5367237),	
				new LatLng(41.6637001, -91.5369302), new LatLng(41.6639456, -91.5369302),
				new LatLng(41.6639456, -91.5372082), new LatLng(41.6642556, -91.5372082),
				new LatLng(41.6642556, -91.5369718)
				));
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
			
			po.strokeWidth(3).strokeColor(Color.RED).fillColor(Color.BLUE);
			map.addPolygon(po);
		}

	}
}

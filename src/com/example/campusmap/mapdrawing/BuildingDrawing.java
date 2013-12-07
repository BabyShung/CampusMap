package com.example.campusmap.mapdrawing;

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

	public Map<Integer, Building> getBuildingSet(){
		return buildingSet;
	}
	
	// ALGO: return true if point is in polygon
	public boolean pointIsInPolygon(LatLng touchPoint) {
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
		// buildingSet is used to search if a touch belongs to a building
		buildingSet.put(1, new Building("Meclean Hall", "2 West Washington Street, Iowa City, IA 52242", new LatLng(
				41.661017, -91.536656), new LatLng(41.661017, -91.536420),
				new LatLng(41.660418, -91.536420), new LatLng(41.660418,
						-91.536656)));
		buildingSet.put(2, new Building("Jessup Hall", "5 West Jefferson Street, Iowa City, IA 52242", new LatLng(
				41.662203, -91.536628), new LatLng(41.662203, -91.536393),
				new LatLng(41.661598, -91.536400), new LatLng(41.661598,
						-91.536635)));
		buildingSet.put(3, new Building("Schaeffer Hall", "20 East Washington Street, Iowa City, IA 52242",
				new LatLng(41.660991, -91.5358898), new LatLng(41.6609937,
						-91.5357735), new LatLng(41.66100, -91.5357735),
				new LatLng(41.660997, -91.535718), new LatLng(41.66100426,
						-91.5357195),

				new LatLng(41.661005, -91.535608), new LatLng(41.660997,
						-91.535609), new LatLng(41.6609987, -91.5355589),
				new LatLng(41.66099099, -91.535557), new LatLng(41.6609877,
						-91.535448),

				new LatLng(41.660844, -91.535449), new LatLng(41.6608412,
						-91.535561), new LatLng(41.6605646, -91.5355582),
				new LatLng(41.660562, -91.5354456), new LatLng(41.6604133,
						-91.5354456),

				new LatLng(41.66041237, -91.53555996), new LatLng(41.66040436,
						-91.5355579), new LatLng(41.66040587, -91.5356082),
				new LatLng(41.6604006, -91.5356116), new LatLng(41.6604006,
						-91.5357246),

				new LatLng(41.66040611, -91.5357246), new LatLng(41.6604021,
						-91.535776), new LatLng(41.66041714, -91.535776),
				new LatLng(41.6604156, -91.535885), new LatLng(41.6605616,
						-91.53589155),

				new LatLng(41.6605621, -91.5357738), new LatLng(41.6606250,
						-91.535774), new LatLng(41.660634056, -91.53582282),
				new LatLng(41.6606538, -91.5358496),

				new LatLng(41.6606879, -91.53586439), new LatLng(41.6607192,
						-91.5358684), new LatLng(41.6607465, -91.535852),
				new LatLng(41.660771, -91.5358234), new LatLng(41.660784,
						-91.5357725),

				new LatLng(41.6608427, -91.5357715), new LatLng(41.6608445,
						-91.535889)

		));

		buildingSet.put(4, new Building("Macbride Hall", "17 North Clinton Street, Iowa City, IA 52242",
				new LatLng(41.6621840, -91.535876), new LatLng(41.6621840,
						-91.53543826), new LatLng(41.66204, -91.5354382),
				new LatLng(41.6620402, -91.5355432), new LatLng(41.66194,
						-91.5355452), new LatLng(41.6619393, -91.5355187),
				new LatLng(41.6618447, -91.5355194), new LatLng(41.661847,
						-91.535543), new LatLng(41.661738, -91.535545),
				new LatLng(41.6617359, -91.5354456), new LatLng(41.66159289,
						-91.5354499), new LatLng(41.6615931, -91.5358892),
				new LatLng(41.6617339, -91.5358845), new LatLng(41.6617354,
						-91.5357826), new LatLng(41.6618131, -91.535783),
				new LatLng(41.6618263, -91.5358335), new LatLng(41.6618461,
						-91.535863), new LatLng(41.6618789, -91.5358821),
				new LatLng(41.6619044, -91.5358805), new LatLng(41.6619393,
						-91.5358633), new LatLng(41.66196035, -91.5358385),
				new LatLng(41.6619728, -91.5357829), new LatLng(41.66204200,
						-91.5357849), new LatLng(41.6620445, -91.53587680)));

		buildingSet.put(5, new Building("Old Capitol Museum", "21 N Clinton St, Iowa City, IA 52242",
				new LatLng(41.6614684, -91.5362435), new LatLng(41.66146941,
						-91.5360156), new LatLng(41.6613489, -91.5360095),
				new LatLng(41.66135268, -91.5359703), new LatLng(41.6612355,
						-91.53597135), new LatLng(41.6612347, -91.5360135),
				new LatLng(41.6611355, -91.5360260), new LatLng(41.6611345,
						-91.53624963), new LatLng(41.6612347, -91.5362486),
				new LatLng(41.6612407, -91.5362630), new LatLng(41.6613566,
						-91.5362677), new LatLng(41.6613582, -91.53624449)));

		buildingSet.put(6, new Building("Main Library", "125 West Washington Street, Iowa City, IA 52242", new LatLng(
				41.659932, -91.538974), new LatLng(41.659932, -91.537903),
				new LatLng(41.659144, -91.537903), new LatLng(41.659144,
						-91.538167), new LatLng(41.659108, -91.538167),
				new LatLng(41.659108, -91.538680), new LatLng(41.659144,
						-91.538680), new LatLng(41.659144, -91.538974)));

		buildingSet.put(7, new Building("Halsey Hall", "28 West Jefferson Street, Iowa City, IA 52242", new LatLng(
				41.663016, -91.53746), new LatLng(41.663016, -91.53724),
				new LatLng(41.663037, -91.53724), new LatLng(41.66303288,
						-91.53693325), new LatLng(41.66264064, -91.53693),
				new LatLng(41.66264, -91.5371777), new LatLng(41.662629,
						-91.53718), new LatLng(41.662628, -91.537469)));

		buildingSet.put(8, new Building("IMU Parking Ramp", "120 North Madison Street, Iowa City, IA 52242",
				new LatLng(41.6633357, -91.53746467), new LatLng(41.663331202,
						-91.537007689),
				new LatLng(41.66326833, -91.5370009839), new LatLng(
						41.663266830, -91.536550037), new LatLng(41.66309250,
						-91.5365453), new LatLng(41.6630927, -91.537468)));

		buildingSet.put(9, new Building("Calvin Hall", "2 West Jefferson Street, Iowa City, IA 52242", new LatLng(
				41.66302988, -91.53670158), new LatLng(41.66303213,
				-91.53650209), new LatLng(41.66299732, -91.536503098),
				new LatLng(41.66299506, -91.5364682), new LatLng(41.66291917,
						-91.5364645), new LatLng(41.66291817, -91.5364350),
				new LatLng(41.662884357, -91.53643235), new LatLng(41.6628816,
						-91.53629556), new LatLng(41.66285255, -91.53629455),
				new LatLng(41.66285029, -91.5362818), new LatLng(41.66276438,
						-91.536283828), new LatLng(41.66276337, -91.53629355),
				new LatLng(41.6627278, -91.536295562), new LatLng(41.662726307,
						-91.536383405), new LatLng(41.66268648, -91.53638139),
				new LatLng(41.662685230, -91.536436714), new LatLng(
						41.66267596, -91.53643637), new LatLng(41.662672706,
						-91.5365255), new LatLng(41.6626869, -91.53653768),
				new LatLng(41.662687774, -91.53658054), new LatLng(
						41.662727059, -91.53657887), new LatLng(41.662727309,
						-91.53666336), new LatLng(41.66275861, -91.53666503),
				new LatLng(41.662761124, -91.53667878), new LatLng(41.66284879,
						-91.53667677), new LatLng(41.662850543, -91.53666369),
				new LatLng(41.662881852, -91.5366633), new LatLng(41.6628841,
						-91.5365282), new LatLng(41.66292167, -91.5365248),
				new LatLng(41.66292368, -91.5367009)));

		buildingSet
				.put(10, new Building("Gilmore Hall", "112 North Capitol Street, Iowa City, IA 52242", new LatLng(
						41.66302637, -91.53592206), new LatLng(41.66302512,
						-91.5357239), new LatLng(41.66286832, -91.53572324),
						new LatLng(41.66286356, -91.53569977), new LatLng(
								41.66281723, -91.53570514), new LatLng(
								41.66281447, -91.53572458), new LatLng(
								41.66265091, -91.535727605), new LatLng(
								41.66265417, -91.53592776), new LatLng(
								41.662746596, -91.535924747), new LatLng(
								41.66274684, -91.53593949), new LatLng(
								41.662939461, -91.535937488), new LatLng(
								41.66294021, -91.535924747)));

		buildingSet
				.put(11, new Building("Trowbridge Hall", "123 North Capitol Street, Iowa City, IA 52242",
						new LatLng(41.66349025, -91.53693057), new LatLng(
								41.66349100, -91.5363153), new LatLng(
								41.663230261, -91.53631635), new LatLng(
								41.663230011, -91.53634317), new LatLng(
								41.66324579, -91.53634384), new LatLng(
								41.66324378, -91.536493375), new LatLng(
								41.66327835, -91.5364957), new LatLng(
								41.66327860, -91.53692688), new LatLng(
								41.663461197, -91.536931246)));

		buildingSet.put(12, new Building("Tippie College of Business",
				"21 East Market Street, Iowa City, IA 52245", new LatLng(41.663128068, -91.53594788),
				new LatLng(41.66349551, -91.53594754), new LatLng(
						41.6634829885, -91.5347720682), new LatLng(
						41.6626361369, -91.5347687155), new LatLng(41.66263638,
						-91.535270959), new LatLng(41.66288034, -91.535264253),
				new LatLng(41.662870804, -91.535096615), new LatLng(
						41.66324654, -91.5350929275), new LatLng(41.66325305,
						-91.535596512),
				new LatLng(41.66311754, -91.5355877950), new LatLng(
						41.66312681, -91.535951904)));
		buildingSet.put(13, new Building("Chemistry Bldg", "251 North Capitol Street",
				new LatLng(41.6645955, -91.5369718), new LatLng(41.6645955,
						-91.5367622), new LatLng(41.6643516, -91.5367622),
				new LatLng(41.6643516, -91.5364759), new LatLng(41.6645863,
						-91.5364759), new LatLng(41.6645863, -91.5362942),
				new LatLng(41.6642291, -91.5362942), new LatLng(41.6642291,
						-91.5362868), new LatLng(41.6640595, -91.5362868),
				new LatLng(41.6640595, -91.5362942), new LatLng(41.6637001,
						-91.5362942), new LatLng(41.6637001, -91.5364759),

				new LatLng(41.6637001, -91.5367237), new LatLng(41.6637001,
						-91.5367237), new LatLng(41.6637001, -91.5369302),
				new LatLng(41.6639456, -91.5369302), new LatLng(41.6639456,
						-91.5372082), new LatLng(41.6642556, -91.5372082),
				new LatLng(41.6642556, -91.5369718)));
		buildingSet.put(14, new Building("Engineering Research Facility",
				"330 South Madison Street", new LatLng(41.6572138, -91.5372762), new LatLng(
						41.6572138, -91.5371260), new LatLng(41.65710891,
						-91.5371260), new LatLng(41.65710891, -91.5370184),
				new LatLng(41.6568478, -91.5370184), new LatLng(41.6568478,
						-91.5373979), new LatLng(41.6569966, -91.5373979),
				new LatLng(41.6569966, -91.5374398), new LatLng(41.6570280,
						-91.5374251), new LatLng(41.6570280, -91.5373959),
				new LatLng(41.6571089, -91.5373959), new LatLng(41.6571089,
						-91.5372762)));
		buildingSet.put(15, new Building("Communications Center", "116 South Madison Street",
				new LatLng(41.65950461794223, -91.53745595365763), new LatLng(
						41.65950511891731, -91.53736174106598), new LatLng(
						41.659496852828276, -91.5373620763421), new LatLng(
						41.659496852828276, -91.53715889900923), new LatLng(
						41.659109597892495, -91.5371572226286), new LatLng(
						41.65910834544716, -91.5373707935214), new LatLng(
						41.659101081263614, -91.53736777603626), new LatLng(
						41.6591013317527, -91.53745092451572)));
		buildingSet.put(16, new Building("Recreation Center", "309 South Madison",
				new LatLng(41.65772337646764, -91.53888624161482), new LatLng(
						41.65772012003958, -91.53795417398214), new LatLng(
						41.65756331030851, -91.53795685619116), new LatLng(
						41.65756130634782, -91.53806649148464), new LatLng(
						41.65752273009231, -91.53806649148464), new LatLng(
						41.65752473405419, -91.53797294944525), new LatLng(
						41.65735514855817, -91.53797294944525), new LatLng(
						41.65735414657459, -91.53800413012505), new LatLng(
						41.65687093817755, -91.53801251202822), new LatLng(
						41.65687093817755, -91.53812013566494), new LatLng(
						41.656741931693745, -91.53812315315008), new LatLng(
						41.6567411801989, -91.53819356113672), new LatLng(
						41.6567016014584, -91.53819356113672), new LatLng(
						41.65670460743953, -91.53856236487627), new LatLng(
						41.656742933686836, -91.53856102377176), new LatLng(
						41.65675019813637, -91.53905924409628), new LatLng(
						41.657526237025564, -91.53904616832733), new LatLng(
						41.65752523504466, -91.53884131461382), new LatLng(
						41.65756105585273, -91.53884064406157), new LatLng(
						41.65756305981345, -91.53893250972033), new LatLng(
						41.657597377630786, -91.53892815113068), new LatLng(
						41.65759612515604, -91.53889127075672)));
		buildingSet.put(17, new Building("Iowa Memorial Union", "125 North Madison Street",
				new LatLng(41.66349876825926, -91.5386163443327), new LatLng(
						41.66349701495529, -91.53783179819584), new LatLng(
						41.6631243115367, -91.53784018009901), new LatLng(
						41.66303113534497, -91.53781235218048), new LatLng(
						41.66296751496458, -91.53780866414309), new LatLng(
						41.662965511172025, -91.53784856200218), new LatLng(
						41.66266368921014, -91.5378512442112), new LatLng(
						41.66267671392153, -91.53898246586323), new LatLng(
						41.66282198936989, -91.53897676616907), new LatLng(
						41.66281948462357, -91.53893753886223), new LatLng(
						41.662863317670315, -91.53893686830997), new LatLng(
						41.66286256624692, -91.53923325240612), new LatLng(
						41.66288360609902, -91.53923526406288), new LatLng(
						41.66292293056603, -91.53920777142048), new LatLng(
						41.66291917345238, -91.538942232728), new LatLng(
						41.66326106989651, -91.53874475508928), new LatLng(
						41.663262823206914, -91.53869614005089), new LatLng(
						41.663392067100624, -91.53869614005089)));
		buildingSet.put(18, new Building("Lindquist Center", "240 South Madison Street",
				new LatLng(41.65889442742307, -91.53750658035278), new LatLng(
						41.65889417693317, -91.53719946742058), new LatLng(
						41.65888165243663, -91.53718069195747), new LatLng(
						41.658878897047074, -91.5371099486947), new LatLng(
						41.65867474740122, -91.53681490570307), new LatLng(
						41.658672492984344, -91.53671264648438), new LatLng(
						41.65827270847648, -91.53671532869339), new LatLng(
						41.6582719569995, -91.53688933700323), new LatLng(
						41.65853547438807, -91.53688699007034), new LatLng(
						41.65853522389676, -91.53716158121824), new LatLng(
						41.65828548358371, -91.53716392815113), new LatLng(
						41.65828398063005, -91.53707977384329), new LatLng(
						41.65829174589027, -91.53706435114145), new LatLng(
						41.65829149539802, -91.53701037168503), new LatLng(
						41.658277467830445, -91.53699293732643), new LatLng(
						41.65801645431946, -91.5369962900877), new LatLng(
						41.658017205799396, -91.53716493397951), new LatLng(
						41.658004180145575, -91.53718069195747), new LatLng(
						41.6580061840925, -91.53724305331707), new LatLng(
						41.65801545234616, -91.53725545853376), new LatLng(
						41.65800392965221, -91.5372722223401), new LatLng(
						41.658005933599135, -91.5373258665204), new LatLng(
						41.658020963199064, -91.53734363615513), new LatLng(
						41.65801820777266, -91.53751596808434), new LatLng(
						41.65827972226116, -91.53751596808434), new LatLng(
						41.65829575376615, -91.53748981654644), new LatLng(
						41.65829700622731, -91.53744220733643), new LatLng(
						41.65828122521493, -91.53741873800755), new LatLng(
						41.6582819766918, -91.53733927756548), new LatLng(
						41.65852645670056, -91.53733793646097), new LatLng(
						41.65852520424387, -91.53739091008902), new LatLng(
						41.658539482248756, -91.53741002082825), new LatLng(
						41.65853973274006, -91.53744388371706), new LatLng(
						41.65859283687061, -91.53751596808434), new LatLng(
						41.658614880081764, -91.5375179797411), new LatLng(
						41.65861287615377, -91.5375330671668), new LatLng(
						41.65887764459723, -91.53752937912941)));
		buildingSet.put(19, new Building("Seaman Center", "103 South Capitol Street",
				new LatLng(41.65999782600442, -91.53743483126163), new LatLng(
						41.65999331726321, -91.53709888458252), new LatLng(
						41.6599667657808, -91.53709921985865), new LatLng(
						41.65996576383786, -91.53707105666399), new LatLng(
						41.659996573576336, -91.53707038611174), new LatLng(
						41.65999456969134, -91.5363435074687), new LatLng(
						41.65985229369835, -91.53634015470743), new LatLng(
						41.65984853640555, -91.53632942587137), new LatLng(
						41.65981196541078, -91.53632909059525), new LatLng(
						41.6598047013066, -91.53633277863264), new LatLng(
						41.65980645471115, -91.53634183108807), new LatLng(
						41.65939941309278, -91.53634518384933), new LatLng(
						41.65940016455661, -91.53635893017054), new LatLng(
						41.65937937405437, -91.53635926544666), new LatLng(
						41.659379123566346, -91.53642028570175), new LatLng(
						41.659364094283376, -91.53641927987337), new LatLng(
						41.659364094283376, -91.53660971671343), new LatLng(
						41.659130638970815, -91.53661675751209), new LatLng(
						41.65912963701487, -91.53679478913546), new LatLng(
						41.65912387576784, -91.53679579496384), new LatLng(
						41.65912487772388, -91.53682664036751), new LatLng(
						41.659130638970815, -91.53682865202427), new LatLng(
						41.65913314386064, -91.53694331645966), new LatLng(
						41.659171719151566, -91.53694398701191), new LatLng(
						41.65917347257334, -91.5369201824069), new LatLng(
						41.659183993102914, -91.53692051768303), new LatLng(
						41.659183993102914, -91.53694164007902), new LatLng(
						41.6594758118696, -91.53693962842226), new LatLng(
						41.65947455943136, -91.53691951185465), new LatLng(
						41.6595256588915, -91.53691984713078), new LatLng(
						41.659525909378935, -91.53693895787), new LatLng(
						41.659725547566644, -91.53693862259388), new LatLng(
						41.659722792213195, -91.53707340359688), new LatLng(
						41.659789421636525, -91.5370723977685), new LatLng(
						41.65979042358224, -91.53704892843962), new LatLng(
						41.65981697513732, -91.5370462462306), new LatLng(
						41.65981522173307, -91.5369413048029), new LatLng(
						41.65984953835032, -91.53693862259388), new LatLng(
						41.65984803543317, -91.53706837445498), new LatLng(
						41.659817225623634, -91.53706971555948), new LatLng(
						41.65982123340458, -91.53712268918753), new LatLng(
						41.659876089881074, -91.53712403029203), new LatLng(
						41.65987508793668, -91.53716426342726), new LatLng(
						41.65959253900603, -91.53716325759888), new LatLng(
						41.65959078559565, -91.53744053095579), new LatLng(
						41.65959078559565, -91.53744053095579)));
		buildingSet.put(20, new Building("Old Capitol Mall", "200 South Capitol Street",
				new LatLng(41.66002512893053, -91.5360203012824), new LatLng(
						41.66002588038705, -91.53554856777191), new LatLng(
						41.660007594942606, -91.53554387390614), new LatLng(
						41.660008596884914, -91.53552208095789), new LatLng(
						41.65996801820947, -91.53552174568176), new LatLng(
						41.6599667657808, -91.53485253453255), new LatLng(
						41.659955744407554, -91.53485018759966), new LatLng(
						41.6599494822628, -91.53484214097261), new LatLng(
						41.65891471710224, -91.53484750539064), new LatLng(
						41.6589182239597, -91.5348133072257), new LatLng(
						41.658537227827146, -91.53480928391218), new LatLng(
						41.658537728809726, -91.53511472046375), new LatLng(
						41.65852244883905, -91.53511840850115), new LatLng(
						41.658524202278485, -91.53537187725306), new LatLng(
						41.658541235687736, -91.53537254780531), new LatLng(
						41.658541235687736, -91.53600454330444), new LatLng(
						41.658582566735554, -91.53600487858057), new LatLng(
						41.65858056280657, -91.53601862490177), new LatLng(
						41.659064008865876, -91.53601828962564), new LatLng(
						41.65906651375828, -91.53606288135052), new LatLng(
						41.659433980419415, -91.53605952858925), new LatLng(
						41.65943297846818, -91.53602600097656), new LatLng(
						41.65952540840404, -91.53603069484234), new LatLng(
						41.659521150117236, -91.53604578226805), new LatLng(
						41.6600121036828, -91.53604209423065), new LatLng(
						41.66000884737046, -91.53601761907339)));

		buildingSet.put(21, new Building("Pomerantz Center",
				"213 North Clinton Street", new LatLng(41.664125, -91.535714), new LatLng(
						41.664097, -91.535714), new LatLng(41.664095,
						-91.535853), new LatLng(41.664067, -91.535858),
				new LatLng(41.664069, -91.535905), new LatLng(41.663822,
						-91.535913), new LatLng(41.663818, -91.535862),
				new LatLng(41.663789, -91.535860), new LatLng(41.663788,
						-91.535894), new LatLng(41.663708, -91.535894),
				new LatLng(41.663710, -91.535572), new LatLng(41.663746,
						-91.535580), new LatLng(41.663756, -91.535497),
				new LatLng(41.664112, -91.535573), new LatLng(41.664101,
						-91.535658), new LatLng(41.664124, -91.535665)));
		buildingSet.put(22, new Building("Black Honors Center", "221 North Clinton",
				new LatLng(41.664606, -91.535690), new LatLng(41.664565,
						-91.535690), new LatLng(41.664561, -91.535769),
				new LatLng(41.664183, -91.535766), new LatLng(41.664179,
						-91.535518), new LatLng(41.664537, -91.535516),
				new LatLng(41.664536, -91.535628), new LatLng(41.664605,
						-91.535630)));

		buildingSet.put(23, new Building("Lutheran Campus Ministry",
				"109 E. Market Street, Iowa City, IA 52245", new LatLng(41.664122, -91.535218), new LatLng(
						41.663991, -91.535217), new LatLng(41.663987,
						-91.535007), new LatLng(41.663816, -91.535010),
				new LatLng(41.663810, -91.534928), new LatLng(41.663791,
						-91.534927), new LatLng(41.663791, -91.534875),
				new LatLng(41.663812, -91.534875), new LatLng(41.663813,
						-91.534806), new LatLng(41.664119, -91.534801)));
		buildingSet.put(24, new Building("Duam Hall", "225 North Clinton Street", new LatLng(
				41.664562, -91.535455), new LatLng(41.664512, -91.535455),
				new LatLng(41.664512, -91.535497), new LatLng(41.664426,
						-91.535499), new LatLng(41.664426, -91.535459),
				new LatLng(41.664412, -91.535459), new LatLng(41.664411,
						-91.535231), new LatLng(41.664356, -91.535230),
				new LatLng(41.664354, -91.535047), new LatLng(41.664409,
						-91.535047), new LatLng(41.664408, -91.534809),
				new LatLng(41.664463, -91.534807), new LatLng(41.664464,
						-91.534765), new LatLng(41.664544, -91.534765),
				new LatLng(41.664546, -91.534807), new LatLng(41.664557,
						-91.534807)));
		buildingSet.put(25, new Building("Burge Residence Hall", "301 North Clinton Street",
				new LatLng(41.665620, -91.535904), new LatLng(41.665491,
						-91.535896), new LatLng(41.665483, -91.535719),
				new LatLng(41.665223, -91.535722), new LatLng(41.665215,
						-91.535909), new LatLng(41.665087, -91.535907),
				new LatLng(41.665079, -91.535673), new LatLng(41.665039,
						-91.535668), new LatLng(41.665041, -91.535737),
				new LatLng(41.665001, -91.535725), new LatLng(41.664926,
						-91.535682), new LatLng(41.664885, -91.535637),
				new LatLng(41.664844, -91.535577), new LatLng(41.664842,
						-91.535572), new LatLng(41.664818, -91.535516),
				new LatLng(41.664792, -91.535417), new LatLng(41.664789,
						-91.535348), new LatLng(41.664800, -91.535279),
				new LatLng(41.664821, -91.535189), new LatLng(41.664796,
						-91.535191), new LatLng(41.664795, -91.535122),
				new LatLng(41.664870, -91.535124), new LatLng(41.664867,
						-91.534776), new LatLng(41.665011, -91.534776),
				new LatLng(41.665012, -91.534809), new LatLng(41.665274,
						-91.534811), new LatLng(41.665275, -91.534774),
				new LatLng(41.665297, -91.534775), new LatLng(41.665296,
						-91.534734), new LatLng(41.665361, -91.534735),
				new LatLng(41.665363, -91.534778), new LatLng(41.665413,
						-91.534776), new LatLng(41.665416, -91.534734),
				new LatLng(41.665467, -91.534705), new LatLng(41.665525,
						-91.534689), new LatLng(41.665583, -91.534704),
				new LatLng(41.665635, -91.534754), new LatLng(41.665680,
						-91.534837), new LatLng(41.665691, -91.534892),
				new LatLng(41.665692, -91.534937), new LatLng(41.665689,
						-91.534978), new LatLng(41.665680, -91.535018),
				new LatLng(41.665666, -91.535062), new LatLng(41.665649,
						-91.535091), new LatLng(41.665606, -91.535090),
				new LatLng(41.665607, -91.535162), new LatLng(41.665622,
						-91.535162)));
		buildingSet.put(26, new Building("Iowa Advanced Technology Labs",
				"205 North Madison Street", new LatLng(41.664639, -91.537935), new LatLng(
						41.664572, -91.537951), new LatLng(41.664573,
						-91.538225), new LatLng(41.664313, -91.538234),
				new LatLng(41.664312, -91.538339), new LatLng(41.664199,
						-91.538345), new LatLng(41.664256, -91.538402),
				new LatLng(41.664197, -91.538537), new LatLng(41.664226,
						-91.538556), new LatLng(41.664175, -91.538681),
				new LatLng(41.664183, -91.538745), new LatLng(41.664154,
						-91.538766), new LatLng(41.664124, -91.538769),
				new LatLng(41.664097, -91.538760), new LatLng(41.664070,
						-91.538738), new LatLng(41.664082, -91.538699),
				new LatLng(41.664061, -91.538680), new LatLng(41.664018,
						-91.538778), new LatLng(41.663983, -91.538757),
				new LatLng(41.663958, -91.538849), new LatLng(41.663865,
						-91.538819), new LatLng(41.663919, -91.538689),
				new LatLng(41.663907, -91.538679), new LatLng(41.663914,
						-91.538660), new LatLng(41.663906, -91.538651),
				new LatLng(41.663987, -91.538470), new LatLng(41.664000,
						-91.538477), new LatLng(41.664029, -91.538362),
				new LatLng(41.663963, -91.538365), new LatLng(41.663961,
						-91.538399), new LatLng(41.663831, -91.538522),
				new LatLng(41.663755, -91.538462), new LatLng(41.663783,
						-91.538385), new LatLng(41.663829, -91.538324),
				new LatLng(41.663756, -91.538326), new LatLng(41.663753,
						-91.538283), new LatLng(41.663703, -91.538310),
				new LatLng(41.663701, -91.538212), new LatLng(41.663761,
						-91.538245), new LatLng(41.663760, -91.538011),
				new LatLng(41.663889, -91.538011), new LatLng(41.663891,
						-91.537828), new LatLng(41.663990, -91.537796),
				new LatLng(41.664126, -91.537769), new LatLng(41.664235,
						-91.537764), new LatLng(41.664314, -91.537766),
				new LatLng(41.664463, -91.537787), new LatLng(41.664555,
						-91.537803), new LatLng(41.664638, -91.537833)));
		buildingSet.put(27, new Building("Womens Resource and Action Center",
				"130 North Madison", new LatLng(41.663456, -91.537393), new LatLng(
						41.663387, -91.537391), new LatLng(41.663387,
						-91.537158), new LatLng(41.663459, -91.537158),
				new LatLng(41.663459, -91.537268), new LatLng(41.663467,
						-91.537270), new LatLng(41.663467, -91.537333),
				new LatLng(41.663457, -91.537334)));
		buildingSet.put(28, new Building("North Campus Parking", "339 North Madison Street",
				new LatLng(41.665662, -91.536854), new LatLng(41.664863,
						-91.536864), new LatLng(41.664866, -91.536814),
				new LatLng(41.664789, -91.536814), new LatLng(41.664827,
						-91.536446), new LatLng(41.664893, -91.536446),
				new LatLng(41.664899, -91.536332), new LatLng(41.664880,
						-91.536330), new LatLng(41.664879, -91.536296),
				new LatLng(41.665549, -91.536275), new LatLng(41.665550,
						-91.536299), new LatLng(41.665568, -91.536301),
				new LatLng(41.665569, -91.536334), new LatLng(41.665664,
						-91.536333)));
		buildingSet.put(29, new Building("North Hall", "20 West Davenport Street",
				new LatLng(41.666134, -91.536948), new LatLng(41.666032,
						-91.536947), new LatLng(41.666032, -91.536979),
				new LatLng(41.665997, -91.536979), new LatLng(41.665998,
						-91.537000), new LatLng(41.665870, -91.536998),
				new LatLng(41.665866, -91.536553), new LatLng(41.665966,
						-91.536555), new LatLng(41.665965, -91.536289),
				new LatLng(41.666238, -91.536284), new LatLng(41.666239,
						-91.536348), new LatLng(41.666278, -91.536352),
				new LatLng(41.666280, -91.536403), new LatLng(41.666238,
						-91.536402), new LatLng(41.666239, -91.536486),
				new LatLng(41.666133, -91.536487)));
		buildingSet.put(30, new Building("Stanley Residence Hall", "10 East Davenport Street",
				new LatLng(41.666686, -91.535831), new LatLng(41.666666,
						-91.535890), new LatLng(41.666617, -91.535917),
				new LatLng(41.666143, -91.535934), new LatLng(41.666110,
						-91.535908), new LatLng(41.666085, -91.535862),
				new LatLng(41.666109, -91.535806), new LatLng(41.666148,
						-91.535774), new LatLng(41.666620, -91.535755),
				new LatLng(41.666664, -91.535774)));
		buildingSet.put(31, new Building("Currier Residence Hall", "413 North Clinton Street",
				new LatLng(41.666088, -91.535611), new LatLng(41.666048,
						-91.535611), new LatLng(41.666047, -91.535639),
				new LatLng(41.666009, -91.535639), new LatLng(41.666010,
						-91.535611), new LatLng(41.665957, -91.535608),
				new LatLng(41.665955, -91.535557), new LatLng(41.665891,
						-91.535559), new LatLng(41.665888, -91.535433),
				new LatLng(41.665955, -91.535430), new LatLng(41.665952,
						-91.534908), new LatLng(41.665932, -91.534876),
				new LatLng(41.666052, -91.534714), new LatLng(41.666070,
						-91.534737), new LatLng(41.666657, -91.534735),
				new LatLng(41.666666, -91.534725), new LatLng(41.666772,
						-91.534866), new LatLng(41.666758, -91.534887),
				new LatLng(41.666759, -91.535637), new LatLng(41.666715,
						-91.535639), new LatLng(41.666714, -91.535658),
				new LatLng(41.666671, -91.535656), new LatLng(41.666671,
						-91.535656), new LatLng(41.666627, -91.535637),
				new LatLng(41.666626, -91.535049), new LatLng(41.666592,
						-91.535046), new LatLng(41.666550, -91.535098),
				new LatLng(41.666551, -91.535218), new LatLng(41.666482,
						-91.535218), new LatLng(41.666485, -91.535324),
				new LatLng(41.666454, -91.535367), new LatLng(41.666254,
						-91.535369), new LatLng(41.666223, -91.535331),
				new LatLng(41.666219, -91.535226), new LatLng(41.666156,
						-91.535224), new LatLng(41.666152, -91.534926),
				new LatLng(41.666120, -91.534921), new LatLng(41.666087,
						-91.534963)));
		buildingSet.put(32, new Building("Dey House", "507 North Clinton Street",
				new LatLng(41.667426, -91.535202), new LatLng(41.667410,
						-91.535202), new LatLng(41.667410, -91.535293),
				new LatLng(41.667198, -91.535296), new LatLng(41.667197,
						-91.535211), new LatLng(41.667184, -91.535210),
				new LatLng(41.667183, -91.535003), new LatLng(41.667175,
						-91.535002), new LatLng(41.667175, -91.534870),
				new LatLng(41.667283, -91.534868), new LatLng(41.667285,
						-91.534971), new LatLng(41.667307, -91.534972),
				new LatLng(41.667309, -91.535105), new LatLng(41.667280,
						-91.535104), new LatLng(41.667280, -91.535173),
				new LatLng(41.667379, -91.535172), new LatLng(41.667379,
						-91.535172), new LatLng(41.667379, -91.535172),
				new LatLng(41.667379, -91.535172), new LatLng(41.667427,
						-91.535172)));
		buildingSet.put(33, new Building("Glenn Schaeffer Library",
				"507 North Clinton Street", new LatLng(41.667492, -91.535158), new LatLng(
						41.667362, -91.535161), new LatLng(41.667359,
						-91.535063), new LatLng(41.667492, -91.535059)));
		buildingSet.put(34, new Building("Jefferson Bldg", "129 East Washington Street",
				new LatLng(41.659955, -91.533559), new LatLng(41.659729,
						-91.533559), new LatLng(41.659730, -91.533250),
				new LatLng(41.659972, -91.533233)));
		buildingSet.put(35, new Building("Phillips Hall", "16 North Clinton Street",
				new LatLng(41.662065, -91.534311), new LatLng(41.661625,
						-91.534327), new LatLng(41.661622, -91.534278),
				new LatLng(41.661509, -91.534280), new LatLng(41.661504,
						-91.533940), new LatLng(41.661617, -91.533937),
				new LatLng(41.661620, -91.534074), new LatLng(41.662069,
						-91.534064)));
		buildingSet.put(36, new Building("Biology Bldg", "129 East Jefferson Street",
				new LatLng(41.662320, -91.533772), new LatLng(41.662174,
						-91.533770), new LatLng(41.662172, -91.533402),
				new LatLng(41.662105, -91.533403), new LatLng(41.662107,
						-91.533508), new LatLng(41.661465, -91.533517),
				new LatLng(41.661463, -91.533285), new LatLng(41.662106,
						-91.533283), new LatLng(41.662106, -91.533339),
				new LatLng(41.662174, -91.533342), new LatLng(41.662173,
						-91.533232), new LatLng(41.662318, -91.533228)));
		buildingSet.put(37, new Building("Biology Bldg East", "210 East Iowa Avenue",
				new LatLng(41.661552, -91.533287), new LatLng(41.661550,
						-91.532929), new LatLng(41.661490, -91.532927),
				new LatLng(41.661503, -91.532734), new LatLng(41.661514,
						-91.532732), new LatLng(41.661509, -91.532238),
				new LatLng(41.661677, -91.532238), new LatLng(41.661682,
						-91.532618), new LatLng(41.661778, -91.532615),
				new LatLng(41.661778, -91.532734), new LatLng(41.661891,
						-91.532740), new LatLng(41.661889, -91.532917),
				new LatLng(41.661596, -91.532921), new LatLng(41.661599,
						-91.533287)));
		buildingSet.put(38, new Building("Sciences Library", "120 East Iowa Avenue",
				new LatLng(41.662123, -91.533873), new LatLng(41.662078,
						-91.533924), new LatLng(41.661961, -91.533922),
				new LatLng(41.661964, -91.533719), new LatLng(41.662078,
						-91.533721), new LatLng(41.662123, -91.533774)));
		buildingSet.put(39, new Building("Van Allen Hall", "30 North Dubuque Street",
				new LatLng(41.662291, -91.532838), new LatLng(41.662100,
						-91.532836), new LatLng(41.662097, -91.532533),
				new LatLng(41.662035, -91.532533), new LatLng(41.662033,
						-91.532581), new LatLng(41.662007, -91.532585),
				new LatLng(41.662007, -91.532599), new LatLng(41.661854,
						-91.532599), new LatLng(41.661855, -91.532504),
				new LatLng(41.661754, -91.532500), new LatLng(41.661757,
						-91.532284), new LatLng(41.662014, -91.532286),
				new LatLng(41.662013, -91.532365), new LatLng(41.662070,
						-91.532372), new LatLng(41.662074, -91.532355),
				new LatLng(41.662101, -91.532351), new LatLng(41.662104,
						-91.532139), new LatLng(41.661704, -91.532136),
				new LatLng(41.661702, -91.531821), new LatLng(41.662091,
						-91.531822), new LatLng(41.662093, -91.531765),
				new LatLng(41.662271, -91.531771), new LatLng(41.662268,
						-91.531864), new LatLng(41.662292, -91.531868),
				new LatLng(41.662293, -91.532355), new LatLng(41.662312,
						-91.532353), new LatLng(41.662310, -91.532400),
				new LatLng(41.662298, -91.532400), new LatLng(41.662294,
						-91.532673), new LatLng(41.662317, -91.532677),
				new LatLng(41.662312, -91.532733), new LatLng(41.662297,
						-91.532728)));
		buildingSet.put(40, new Building("Spence Laboratories of Psychology",
				"308 East Iowa Avenue", new LatLng(41.661661, -91.531507), new LatLng(
						41.661612, -91.531507), new LatLng(41.661611,
						-91.531540), new LatLng(41.661464, -91.531541),
				new LatLng(41.661460, -91.531090), new LatLng(41.661605,
						-91.531097), new LatLng(41.661607, -91.531132),
				new LatLng(41.661663, -91.531133)));
		buildingSet.put(41, new Building("Seashore Hall", "301 East Jefferson Street",
				new LatLng(41.661503, -91.531060), new LatLng(41.661589,
						-91.531062), new LatLng(41.661842, -91.531061),
				new LatLng(41.661845, -91.531176), new LatLng(41.661743,
						-91.531176), new LatLng(41.661733, -91.531191),
				new LatLng(41.661731, -91.531224), new LatLng(41.661743,
						-91.531247), new LatLng(41.661744, -91.531317),
				new LatLng(41.661732, -91.531332), new LatLng(41.661730,
						-91.531370), new LatLng(41.661741, -91.531383),
				new LatLng(41.661846, -91.531385), new LatLng(41.661847,
						-91.531524), new LatLng(41.661765, -91.531524),
				new LatLng(41.661768, -91.531687), new LatLng(41.662268,
						-91.531682), new LatLng(41.662266, -91.531355),
				new LatLng(41.662086, -91.531357), new LatLng(41.662085,
						-91.531188), new LatLng(41.662007, -91.531192),
				new LatLng(41.662008, -91.531092), new LatLng(41.662116,
						-91.531094), new LatLng(41.662116, -91.530840),
				new LatLng(41.661949, -91.530841), new LatLng(41.661950,
						-91.530899), new LatLng(41.661502, -91.530903)));
		buildingSet.put(42, new Building("Stuit Hall", "335 East Jefferson Street", new LatLng(
				41.662266, -91.530726), new LatLng(41.662135, -91.530730),
				new LatLng(41.662130, -91.530337), new LatLng(41.662267,
						-91.530337), new LatLng(41.662267, -91.530483),
				new LatLng(41.662277, -91.530480), new LatLng(41.662279,
						-91.530558), new LatLng(41.662269, -91.530559)));
		buildingSet.put(43, new Building("University Services Blgd",
				"1 West Prentiss", new LatLng(41.654314, -91.536877), new LatLng(
						41.654310, -91.536868), new LatLng(41.654299,
						-91.536871), new LatLng(41.654299, -91.536801),
				new LatLng(41.654307, -91.536442), new LatLng(41.654296,
						-91.536441), new LatLng(41.654295, -91.536460),
				new LatLng(41.654237, -91.536456), new LatLng(41.654232,
						-91.536393), new LatLng(41.654105, -91.536395),
				new LatLng(41.654102, -91.536411),

				new LatLng(41.654077, -91.536411), new LatLng(41.654077,
						-91.536393), new LatLng(41.653933, -91.536396),
				new LatLng(41.653931, -91.536462), new LatLng(41.653862,
						-91.536470), new LatLng(41.653867, -91.536887),
				new LatLng(41.653961, -91.536922), new LatLng(41.654020,
						-91.536935), new LatLng(41.654110, -91.536938),
				new LatLng(41.654196, -91.536931), new LatLng(41.654240,
						-91.536917)));
		buildingSet.put(44, new Building("Hillcrest Hall", "25 Byington Road",
				new LatLng(41.659612, -91.542485), new LatLng(41.659466,
						-91.542487), new LatLng(41.659469, -91.542652),
				new LatLng(41.659255, -91.542666), new LatLng(41.659257,
						-91.542642), new LatLng(41.659175, -91.542645),
				new LatLng(41.659173, -91.542662), new LatLng(41.659105,
						-91.542661), new LatLng(41.659098, -91.542523),
				new LatLng(41.658957, -91.542526), new LatLng(41.658947,
						-91.542256), new LatLng(41.659143, -91.542244),
				new LatLng(41.659143, -91.542162), new LatLng(41.659399,
						-91.542145), new LatLng(41.659406, -91.542211),
				new LatLng(41.659459, -91.542208), new LatLng(41.659463,
						-91.542231), new LatLng(41.659609, -91.542221)));
		buildingSet.put(45, new Building("Hillcrest Residence Hall",
				"25 Byington Road", new LatLng(41.659955, -91.543474), new LatLng(
						41.659847, -91.543478), new LatLng(41.659834,
						-91.543015), new LatLng(41.659762, -91.542913),
				new LatLng(41.659349, -91.542932), new LatLng(41.659322,
						-91.542963), new LatLng(41.659327, -91.543257),
				new LatLng(41.659224, -91.543265), new LatLng(41.659210,
						-91.542963), new LatLng(41.659181, -91.542923),
				new LatLng(41.658802, -91.542941), new LatLng(41.658779,
						-91.542979), new LatLng(41.658814, -91.543368),
				new LatLng(41.658703, -91.543377), new LatLng(41.658702,
						-91.543343), new LatLng(41.658709, -91.543343),
				new LatLng(41.658618, -91.542484), new LatLng(41.658610,
						-91.542484), new LatLng(41.658603, -91.542411),
				new LatLng(41.658614, -91.542409), new LatLng(41.658605,
						-91.542356), new LatLng(41.658714, -91.542344),
				new LatLng(41.658719, -91.542395), new LatLng(41.658726,
						-91.542396), new LatLng(41.658764, -91.542788),
				new LatLng(41.658824, -91.542783), new LatLng(41.658825,
						-91.542796), new LatLng(41.659016, -91.542779),
				new LatLng(41.659016, -91.542792), new LatLng(41.659047,
						-91.542788), new LatLng(41.659049, -91.542766),
				new LatLng(41.659184, -91.542760), new LatLng(41.659214,
						-91.542724), new LatLng(41.659260, -91.542724),
				new LatLng(41.659256, -91.542644), new LatLng(41.659285,
						-91.542661), new LatLng(41.659287, -91.542723),
				new LatLng(41.659319, -91.542722), new LatLng(41.659348,
						-91.542766), new LatLng(41.659484, -91.542760),
				new LatLng(41.659485, -91.542792), new LatLng(41.659820,
						-91.542773), new LatLng(41.659847, -91.542812),
				new LatLng(41.659870, -91.542790), new LatLng(41.659897,
						-91.542827), new LatLng(41.659919, -91.542800),
				new LatLng(41.659933, -91.542819), new LatLng(41.659937,
						-91.542934), new LatLng(41.659943, -91.542940)));
		buildingSet.put(46, new Building("English Philosophy Building",
				"308 English Philosophy Bldg, Iowa City, IA 52242", new LatLng(
						41.66100351789066, -91.54030848294497), new LatLng(
						41.6610010130736, -91.54016967862844), new LatLng(
						41.660870762451815, -91.54017101973295), new LatLng(
						41.66086925955848, -91.54005534946918), new LatLng(
						41.660894558258136, -91.54005769640207), new LatLng(
						41.66089505922239, -91.53953567147255), new LatLng(
						41.660858739304196, -91.53953433036804), new LatLng(
						41.660858238339664, -91.53949040919542), new LatLng(
						41.66072773694633, -91.53948940336704), new LatLng(
						41.66072673501522, -91.53954036533833), new LatLng(
						41.660706445906946, -91.53954304754734), new LatLng(
						41.66070920121831, -91.5401753783226), new LatLng(
						41.66063305438788, -91.54017504304647), new LatLng(
						41.660631551489026, -91.54030814766884)));
		buildingSet.put(47, new Building(
				"Adler Journalism and Mass Communication Building",
				"104 West Washington, Iowa City, IA 52242", new LatLng(
						41.66089080102614, -91.53901096433401), new LatLng(
						41.660895309704536, -91.53896100819111), new LatLng(
						41.66091960646585, -91.53896100819111), new LatLng(
						41.660930126710184, -91.5388922765851), new LatLng(
						41.660935386831696, -91.53879605233669), new LatLng(
						41.66091284345073, -91.53879571706057), new LatLng(
						41.660912092004565, -91.53872530907393), new LatLng(
						41.660895309704536, -91.53872463852167), new LatLng(
						41.66089280488325, -91.53860326856375), new LatLng(
						41.660480760456906, -91.53860226273537), new LatLng(
						41.660478756587, -91.5386475250125), new LatLng(
						41.66041914142856, -91.53865054249763), new LatLng(
						41.66041788900867, -91.53861835598946), new LatLng(
						41.66040887158474, -91.53860528022051), new LatLng(
						41.66039434351025, -91.53859253972769), new LatLng(
						41.66037680962285, -91.53859119862318), new LatLng(
						41.660339988443795, -91.5385952219367), new LatLng(
						41.66031368758874, -91.53861433267593), new LatLng(
						41.66028788769189, -91.53866160660982), new LatLng(
						41.66028838866085, -91.53870955109596), new LatLng(
						41.66029815755497, -91.53874810785055), new LatLng(
						41.660336732148046, -91.53874408453703), new LatLng(
						41.6603384855381, -91.53892245143652), new LatLng(
						41.660371549455704, -91.53892613947392), new LatLng(
						41.66036729122485, -91.53901029378176), new LatLng(
						41.66047825561951, -91.5390096232295), new LatLng(
						41.660480760456906, -91.53903141617775), new LatLng(
						41.66056166665259, -91.53902772814035), new LatLng(
						41.66056291906968, -91.53901264071465)));
		buildingSet.put(48, new Building("Becker Communications Studies Bldg",
				"25 South Madison Street, Iowa City, IA 52242", new LatLng(
						41.660747274599785, -91.53838098049164), new LatLng(
						41.660743016393766, -91.53828676789999), new LatLng(
						41.66070419156121, -91.53823312371969), new LatLng(
						41.66066336284106, -91.53821870684624), new LatLng(
						41.66072297777343, -91.53812449425459), new LatLng(
						41.66067413361024, -91.53805676847696), new LatLng(
						41.66067313167829, -91.53787169605494), new LatLng(
						41.66058496160651, -91.53787337243557), new LatLng(
						41.66058070338975, -91.53789382427931), new LatLng(
						41.66056291906968, -91.5378924831748), new LatLng(
						41.660562668586266, -91.53787571936846), new LatLng(
						41.66032295551033, -91.53787605464458), new LatLng(
						41.6603164429169, -91.53833571821451), new LatLng(
						41.66045846740061, -91.53833471238613), new LatLng(
						41.660509566080016, -91.53840411454439), new LatLng(
						41.66059397900575, -91.5382931381464), new LatLng(
						41.660595732388785, -91.53838634490967), new LatLng(
						41.66063681163494, -91.53843026608229), new LatLng(
						41.66070920121831, -91.53842724859715)));
		buildingSet.put(49, new Building("Boyd Law Building",
				"220 Boyd Law Bldg, Iowa City, IA 52242", new LatLng(
						41.65708135596558, -91.54315866529942), new LatLng(
						41.65710840963131, -91.54321029782295), new LatLng(
						41.6571640199085, -91.5432719886303), new LatLng(
						41.657222636094666, -91.54331792145967), new LatLng(
						41.657272484860556, -91.54334474354982), new LatLng(
						41.65732508904404, -91.54336016625166), new LatLng(
						41.65740499722152, -91.5433732420206), new LatLng(
						41.657407502178444, -91.54338229447603), new LatLng(
						41.65741526754433, -91.54338330030441), new LatLng(
						41.65743029728211, -91.54337927699089), new LatLng(
						41.65743630917624, -91.54336888343096), new LatLng(
						41.657436058680666, -91.54335480183363), new LatLng(
						41.6574826508413, -91.54335044324398), new LatLng(
						41.65754101624228, -91.54332898557186), new LatLng(
						41.65758936179187, -91.54330383986235), new LatLng(
						41.657639711263506, -91.54326360672712), new LatLng(
						41.657682044868416, -91.54320996254683), new LatLng(
						41.65771936855613, -91.54315866529942), new LatLng(
						41.65776896644328, -91.54305003583431), new LatLng(
						41.657810047492625, -91.5428901091218), new LatLng(
						41.65781555836308, -91.54280997812748), new LatLng(
						41.65781029798672, -91.54272615909576), new LatLng(
						41.657792011913216, -91.54261115938425), new LatLng(
						41.65777122089178, -91.54253840446472), new LatLng(
						41.657737153661486, -91.5424633026123), new LatLng(
						41.657691814157886, -91.54238853603601), new LatLng(
						41.657693567619944, -91.54244888573885), new LatLng(
						41.65756756872478, -91.54245559126139), new LatLng(
						41.65756305981345, -91.54226616024971), new LatLng(
						41.657500937002645, -91.54223263263702), new LatLng(
						41.65742027745732, -91.54221754521132), new LatLng(
						41.65741902497911, -91.54218837618828), new LatLng(
						41.657409005152566, -91.54217932373285), new LatLng(
						41.65739247243534, -91.54218569397926), new LatLng(
						41.657391720948105, -91.54223665595055), new LatLng(
						41.657302043407036, -91.54225375503302), new LatLng(
						41.65722664403706, -91.54229566454887), new LatLng(
						41.657153749545614, -91.5423583611846), new LatLng(
						41.65707233807447, -91.54247235506773), new LatLng(
						41.65702273965085, -91.54259540140629), new LatLng(
						41.65716251692866, -91.54270939528942), new LatLng(
						41.657283757189205, -91.54272951185703), new LatLng(
						41.65731156225811, -91.54278114438057), new LatLng(
						41.657276492799845, -91.5428039431572), new LatLng(
						41.657274488830254, -91.54284987598658), new LatLng(
						41.65731582069102, -91.54287736862898), new LatLng(
						41.6573060513445, -91.54293738305569)));
		buildingSet.put(50, new Building("Field House",
				"225 South Grand Avenue, Iowa City, IA 52242", new LatLng(
						41.659253378457386, -91.5469915419817), new LatLng(
						41.65925162503779, -91.54633641242981), new LatLng(
						41.65893876412109, -91.54633909463882), new LatLng(
						41.65893851363137, -91.54624722898006), new LatLng(
						41.65864017966706, -91.54625326395035), new LatLng(
						41.65863867672167, -91.54621437191963), new LatLng(
						41.65882228628987, -91.54621034860611), new LatLng(
						41.65882253678005, -91.54611077159643), new LatLng(
						41.6588127676621, -91.54611311852932), new LatLng(
						41.65881402011321, -91.54605779796839), new LatLng(
						41.65870931511683, -91.54605410993099), new LatLng(
						41.65871131904179, -91.54597967863083), new LatLng(
						41.65869979647236, -91.5459793433547), new LatLng(
						41.658700046963006, -91.54595017433167), new LatLng(
						41.65853171701842, -91.5459531918168), new LatLng(
						41.65853021407051, -91.5459793433547), new LatLng(
						41.65851819048591, -91.54598101973534), new LatLng(
						41.658513431149736, -91.5460604801774), new LatLng(
						41.65842250271188, -91.54606316238642), new LatLng(
						41.658418494843914, -91.54621470719576), new LatLng(
						41.65849840166474, -91.546212695539), new LatLng(
						41.65849815117328, -91.54624119400978), new LatLng(
						41.658277467830445, -91.54624052345753), new LatLng(
						41.65827546389197, -91.54634647071362), new LatLng(
						41.657969111064475, -91.54635015875101), new LatLng(
						41.65796585464883, -91.54645208269358), new LatLng(
						41.65786139876743, -91.54645208269358), new LatLng(
						41.65786114827348, -91.54628846794367), new LatLng(
						41.65742954579531, -91.54629282653332), new LatLng(
						41.6574315497601, -91.54707100242376), new LatLng(
						41.65787217000524, -91.54706630855799), new LatLng(
						41.657867661115205, -91.54688593000174), new LatLng(
						41.65797487241483, -91.54688660055399), new LatLng(
						41.65797286846694, -91.54698483645916), new LatLng(
						41.65830978132973, -91.54697645455599), new LatLng(
						41.658315793141774, -91.54699992388487), new LatLng(
						41.6592503725952, -91.54699087142944)));
	}

	// ------------------------inner class------------------------------
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

			po.strokeWidth(3).strokeColor(Color.rgb(105, 105, 105)).fillColor(Color.rgb(240, 165, 20));
			map.addPolygon(po);
		}

	}
	// -----------------------------------------------------------
}

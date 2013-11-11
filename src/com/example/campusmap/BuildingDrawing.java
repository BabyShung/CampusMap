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
		// buildingSet is used to search if a touch belongs to a building
		buildingSet.put(1, new Building("Meclean Hall", "address1", new LatLng(
				41.661017, -91.536656), new LatLng(41.661017, -91.536420),
				new LatLng(41.660418, -91.536420), new LatLng(41.660418,
						-91.536656)));
		buildingSet.put(2, new Building("Jessup Hall", "address2", new LatLng(
				41.662203, -91.536628), new LatLng(41.662203, -91.536393),
				new LatLng(41.661598, -91.536400), new LatLng(41.661598,
						-91.536635)));
		buildingSet.put(3, new Building("Schaeffer Hall", "address3",
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

		buildingSet.put(4, new Building("Macbride Hall", "address4",
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

		buildingSet.put(5, new Building("Old Capitol Mall", "address5",
				new LatLng(41.6614684, -91.5362435), new LatLng(41.66146941,
						-91.5360156), new LatLng(41.6613489, -91.5360095),
				new LatLng(41.66135268, -91.5359703), new LatLng(41.6612355,
						-91.53597135), new LatLng(41.6612347, -91.5360135),
				new LatLng(41.6611355, -91.5360260), new LatLng(41.6611345,
						-91.53624963), new LatLng(41.6612347, -91.5362486),
				new LatLng(41.6612407, -91.5362630), new LatLng(41.6613566,
						-91.5362677), new LatLng(41.6613582, -91.53624449)));

		buildingSet.put(6, new Building("Main Library", "address6", new LatLng(
				41.659932, -91.538974), new LatLng(41.659932, -91.537903),
				new LatLng(41.659144, -91.537903), new LatLng(41.659144,
						-91.538167), new LatLng(41.659108, -91.538167),
				new LatLng(41.659108, -91.538680), new LatLng(41.659144,
						-91.538680), new LatLng(41.659144, -91.538974)));

		buildingSet.put(7, new Building("Halsey Hall", "address7", new LatLng(
				41.663016, -91.53746), new LatLng(41.663016, -91.53724),
				new LatLng(41.663037, -91.53724), new LatLng(41.66303288,
						-91.53693325), new LatLng(41.66264064, -91.53693),
				new LatLng(41.66264, -91.5371777), new LatLng(41.662629,
						-91.53718), new LatLng(41.662628, -91.537469)));

		buildingSet.put(8, new Building("IMU", "address8", new LatLng(
				41.6633357, -91.53746467), new LatLng(41.663331202,
				-91.537007689), new LatLng(41.66326833, -91.5370009839),
				new LatLng(41.663266830, -91.536550037), new LatLng(
						41.66309250, -91.5365453), new LatLng(41.6630927,
						-91.537468)));

		buildingSet.put(9, new Building("Calvin Hall", "address9", new LatLng(
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
				.put(10, new Building("Gilmore Hall", "address10", new LatLng(
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
				.put(11, new Building("Trowbridge Hall", "address11",
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
				"address12", new LatLng(41.663128068, -91.53594788),
				new LatLng(41.66349551, -91.53594754), new LatLng(
						41.6634829885, -91.5347720682), new LatLng(
						41.6626361369, -91.5347687155), new LatLng(41.66263638,
						-91.535270959), new LatLng(41.66288034, -91.535264253),
				new LatLng(41.662870804, -91.535096615), new LatLng(
						41.66324654, -91.5350929275), new LatLng(41.66325305,
						-91.535596512),
				new LatLng(41.66311754, -91.5355877950), new LatLng(
						41.66312681, -91.535951904)));
		buildingSet.put(13, new Building("Chemistry Bldg", "address13",
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
				"address14", new LatLng(41.6572138, -91.5372762), new LatLng(
						41.6572138, -91.5371260), new LatLng(41.65710891,
						-91.5371260), new LatLng(41.65710891, -91.5370184),
				new LatLng(41.6568478, -91.5370184), new LatLng(41.6568478,
						-91.5373979), new LatLng(41.6569966, -91.5373979),
				new LatLng(41.6569966, -91.5374398), new LatLng(41.6570280,
						-91.5374251), new LatLng(41.6570280, -91.5373959),
				new LatLng(41.6571089, -91.5373959), new LatLng(41.6571089,
						-91.5372762)));
		buildingSet.put(15, new Building("Communications Center", "address15",
				new LatLng(41.65950461794223, -91.53745595365763), new LatLng(
						41.65950511891731, -91.53736174106598), new LatLng(
						41.659496852828276, -91.5373620763421), new LatLng(
						41.659496852828276, -91.53715889900923), new LatLng(
						41.659109597892495, -91.5371572226286), new LatLng(
						41.65910834544716, -91.5373707935214), new LatLng(
						41.659101081263614, -91.53736777603626), new LatLng(
						41.6591013317527, -91.53745092451572)));
		buildingSet.put(16, new Building("Recreation Center", "address16",
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
		buildingSet.put(17, new Building("Iowa Memorial Union", "address17",
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
		buildingSet.put(18, new Building("Lindquist Center", "address18",
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
		buildingSet.put(19, new Building("Seaman Center", "address19",
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
		buildingSet.put(20, new Building("Old Capital Mall", "address20",
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

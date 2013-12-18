package com.example.campusmap.direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.simonvt.messagebar.MessageBar;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.campusmap.MapActivity;
import com.example.campusmap.R;
import com.example.campusmap.algorithms.RouteOptimization;
import com.example.campusmap.location.MyLocation;
import com.example.campusmap.mapdrawing.BuildingDrawing;
import com.example.campusmap.mapdrawing.PolylineDrawing;
import com.example.campusmap.routefilter.ReturnRoute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

// Async <inputtype, progresstype, returntype>
public class RouteRequestTask extends AsyncTask<String, Integer, String> {

	private MapActivity ma;
	private LatLng fromPosition;
	private LatLng toPosition;
	private GoogleMap map;
	private CampusMapDirection cmd;
	private MessageBar mMessageBar;

	private ArrayList<LatLng> googlePoints;
	private int googleDistance;
	private int googleTime;
	private BuildingDrawing bd;
	private ArrayList<Polyline> polylineAL;
	private boolean cameraToStart;
	private MyLocation ml;

	public RouteRequestTask(MapActivity ma, GoogleMap map, LatLng fromPosition,
			LatLng toPosition, MessageBar mMessageBar, BuildingDrawing bd,
			boolean cameraToStart, MyLocation ml) {
		this.ma = ma;
		this.map = map;
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
		this.mMessageBar = mMessageBar;
		this.bd = bd;
		this.cameraToStart = cameraToStart;
		this.ml = ml;

		this.polylineAL = new ArrayList<Polyline>();

	}

	public ArrayList<Polyline> getPolyLineArrayList() {
		return polylineAL;
	}

	@Override
	protected String doInBackground(String... arg0) {

		cmd = new CampusMapDirection();

		// send request to server
		cmd.initializeJSONOject(fromPosition, toPosition);

		// get google route
		GMapV2Direction md = new GMapV2Direction();
		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_WALKING);
		if (doc != null) {
			googlePoints = md.getDirection(doc);
			googleDistance = md.getDistanceValue(doc);
			googleTime = md.getDurationValue(doc);
		}

		return null;
	}

	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);

		int row_to_optionMenu = 0;

		if (cmd.returnJSONObj() == null) {
			Toast.makeText(ma, "Network error, please try again",
					Toast.LENGTH_LONG).show();
		} else {
			int status = cmd.getStatus();

			Bundle b = new Bundle();
			PolylineDrawing pdrawing = new PolylineDrawing();

			if (status == 1) {

				// return the best three base on distance

				cmd.initRouteOBJ();

				List<ReturnRoute> rr = cmd.getRoutesArrayList();

				// add google direction as well
				rr.add(new ReturnRoute(googleDistance, googleTime,
						googlePoints, -1));

				// used fromPosition, should I get MyLocation updated one?
				RouteOptimization rop = new RouteOptimization(fromPosition, bd);

				// optimizing the routes
				rr = rop.routeOptimize(rr, ml);

				// pass to optionmenu
				if (rr.size() >= 3) {
					row_to_optionMenu = 3;
				} else {
					row_to_optionMenu = rr.size();

				}

				// sort
				Collections.sort(rr);

				b.putInt("onMsgClick", 1);// 1 means will pop up optionMenu
				b.putInt("NumberOfRoutes", row_to_optionMenu);

				// draw all the possible route
				int[] Colors = { Color.RED, Color.BLUE, Color.BLACK };
				int j = 0;
				for (ReturnRoute tmprr : rr) {
					if (j >= 3)
						break;
					Polyline pl = pdrawing.drawLineOnGoogleMap(map,
							tmprr.getPoints(), Colors[j], 10 - 3 * j);
					polylineAL.add(pl);
					b.putInt("distance_" + (j + 1), tmprr.getDistance());
					b.putInt("time_" + (j + 1), tmprr.getTakeTime());

					j++;
				}

			} else {// 0 requested, just use google
				row_to_optionMenu = 1;
				Polyline pl = pdrawing.drawLineOnGoogleMap(map, googlePoints,
						Color.RED, 10);
				polylineAL.add(pl);

				b.putInt("onMsgClick", 1);// 1 means will pop up optionMenu
				b.putInt("NumberOfRoutes", row_to_optionMenu);

				b.putInt("distance_1", googleDistance);
				b.putInt("time_1", googleTime);
			}

			// camera
			if (cameraToStart)
				map.animateCamera(CameraUpdateFactory.newLatLng(fromPosition));
			else
				map.animateCamera(CameraUpdateFactory.newLatLng(toPosition));

			mMessageBar.show("Request route success!", "Select",
					R.drawable.ic_messagebar_undo, b);

		}
	}

}
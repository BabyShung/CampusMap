package com.example.campusmap.direction;

import java.util.ArrayList;
import java.util.Random;
import org.w3c.dom.Document;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.campusmap.MapActivity;
import com.example.campusmap.mapdrawing.PolylineDrawing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleRouteTask extends AsyncTask<Void, Void, ArrayList<LatLng>> {

	private MapActivity mContext;
	private PolylineOptions rectline;
	private GoogleMap map;
	private LatLng fromPosition;
	private LatLng toPosition;
	private Polyline drawnLine;

	// constructor
	public GoogleRouteTask(MapActivity homeActivity, GoogleMap map,
			LatLng fromPosition, LatLng toPosition) {
		this.mContext = homeActivity;
		this.map = map;
		this.fromPosition = fromPosition;
		this.toPosition = toPosition;
	}

	public Polyline getDrawnLine() {
		return drawnLine;
	}

	@Override
	protected ArrayList<LatLng> doInBackground(Void... params) {

		GMapV2Direction md = new GMapV2Direction();
		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_WALKING);
		if (doc != null) {
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			return directionPoint;
		} else
			return null;
	}

	@Override
	protected void onPostExecute(ArrayList<LatLng> result) {
		super.onPostExecute(result);

		PolylineDrawing pdrawing = new PolylineDrawing();
		
		drawnLine = pdrawing.drawLineOnGoogleMap(map, result, Color.MAGENTA,10);
 
		Toast.makeText(mContext, "Google direction",
				Toast.LENGTH_SHORT).show();
		map.animateCamera(CameraUpdateFactory.newLatLng(toPosition));

	}

}
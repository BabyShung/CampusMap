package com.example.campusmap.direction;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.example.campusmap.file.FileOperations;
import com.example.campusmap.mapdrawing.PolylineDrawing;
import com.example.campusmap.routefilter.Location_Hao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Location;

public class Route {
	private FileOperations fo;
	private boolean recordIsStarted = false;
	private BlockingQueue<Location_Hao> buffer;

	// constructor
	
	// A Route Object is associted with recording, so init queue as well
	public Route(FileOperations fo) {
		buffer = new ArrayBlockingQueue<Location_Hao>(20);
		this.fo = fo;
	}

	//test method, to show a specific route
	public Polyline showTestRoute(String justName, GoogleMap map, int color,boolean isOriginal) {
		String newS;
		if(isOriginal){
			newS = justName.replace(".txt", "");
		}else{
			newS = justName.replace(".txt", "_a");
		}
		ArrayList<LatLng> result = fo.readPointsFile(newS);
		
		LatLng first = result.get(0);
		
		PolylineDrawing pdrawing = new PolylineDrawing();
		
		Polyline pl = pdrawing.drawLineOnGoogleMap(map, result, color,8);
		
		map.animateCamera(CameraUpdateFactory.newLatLng(first));
 
		
		return pl;
	}

	//put data into the buffer
	public void bufferStore(Location_Hao location) {
		try {
			buffer.put(location);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	//take data from the buffer
	public void bufferTakeAndAddToFile() {
		Location_Hao tmp;
		try {
			tmp = buffer.take();
			// string that will be stored
			String dataString = tmp.getX() + "," + tmp.getY()
					+ "," + tmp.getTS() + ";";
			// append data to a file
			fo.appendDataIntoFile(dataString);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//put remaining elements from buffer if exists
	//and then close file writer
	public void checkRemainingElementsInBQandCloseBuffer(
			FileOperations deliverFile) {
		// put remaining ele in BQ
		while (!buffer.isEmpty()) {
			bufferTakeAndAddToFile();
		}
		// close bw
		deliverFile.closeBufferWriter();
		deliverFile.closeFileWriter();
	}
	
	public void toggleRecordState() {
		recordIsStarted = !recordIsStarted;
	}

	// if a route recording is started
	public boolean recordHasStarted() {
		return recordIsStarted;
	}

}

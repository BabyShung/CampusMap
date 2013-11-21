package com.example.campusmap;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import android.graphics.Color;
import android.location.Location;


public class Route {
	private FileOperations fo;
	private boolean recordIsStarted = false;
	private BlockingQueue<Location> buffer;

	
	public Route(FileOperations fo) {// constructor
		buffer = new ArrayBlockingQueue<Location>(20);
		this.fo = fo;
	}

	public void showTestRoute(String justName,GoogleMap map,int c) {

		String newS = justName.replace(".txt", "_a");
		ArrayList<LatLng> result = fo.readPointsFile(newS);
		PolylineOptions rectline;
		if (result != null) {
			rectline = new PolylineOptions().width(4).color(c);
			int i;
			for (i = 0; i < result.size(); i++){
				rectline.add(result.get(i));
			}
			map.addPolyline(rectline);
			System.out.println("*************Drawing********");
		}
	}

	public void bufferStore(Location location) {
		try {
			buffer.put(location);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void checkRemainingElementsInBQandCloseBuffer(FileOperations deliverFile) {
			// put remaining ele in BQ
			while (!buffer.isEmpty()) {
				bufferTakeAndAddToFile();
			}
			// close bw
			deliverFile.closeBufferWriter();
			deliverFile.closeFileWriter();
	}
	
	public void bufferTakeAndAddToFile() {
		Location tmp;
		try {
			tmp = buffer.take();
			// string that will be stored
			String dataString = tmp.getLatitude() + "," + tmp.getLongitude() + "," + tmp.getTime() + ";";
			// append data to a file
			fo.appendDataIntoFile(dataString);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void toggleRecordState() {
		recordIsStarted = !recordIsStarted;
	}

	public boolean recordHasStarted() {
		return recordIsStarted;
	}

}

package com.example.campusmap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Environment;

public class RouteRecord {
	
	private File path = null;
	private File fileName = null;
	private FileWriter fileWritter;
	private BufferedWriter bufferWritter;
	private String state;
	private boolean canW, canR;
	private boolean recordIsStarted = false;
	BlockingQueue<LatLng> buffer;
	public RouteRecord(){
		buffer = new ArrayBlockingQueue<LatLng>(20);;
	}
	
	public void toggleRecordState(){
		recordIsStarted = !recordIsStarted;
	}
	public boolean recordHasStarted(){
		return recordIsStarted;
	}
	
	public String getFileName(){
		return fileName.getName();
	}
	
	public void bufferStore(Location location){
		try {
			buffer.put(new LatLng(location.getLatitude(), location
					.getLongitude()));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void bufferTakeAndAddToFile(){
		LatLng tmp;
		try {
			tmp = buffer.take();
			// string that will be stored
			String dataString = "\"" + tmp.latitude + "\"," + "\""
					+ tmp.longitude + "\"\n";
			//append data to a file
			this.appendDataIntoFile(dataString);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public void checkRemainingElementsInBQandCloseBuffer(){
		
		try {
			//put remaining ele in BQ
			while(!buffer.isEmpty()){
				bufferTakeAndAddToFile();
			}
			
			//close bw
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean RWTrue(){
		return canW && canR;
	}
	
	public void appendDataIntoFile(String data) {
		try {
			// just execute writing
			bufferWritter.write(data);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkState() {
		// check the state of read/write
		state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			canW = canR = true;
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			canW = false;
			canR = true;
		} else {
			canW = false;
			canR = false;
		}
	}
	
	public void fileInitialization(String extension) {

		// set path, we are going to save the csv file into download folder
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		// if not exists, create path directory
		path.mkdirs();
		// check if we have access to write
		checkState();

		if (canW && canR) {
			try {

				// create a new file
				fileName = new File(path + "/" + "MyRoute1."+ extension);
				for (int i = 2; i < 100; i++) {
					if (fileName.exists()) { // if exist,then change name
						fileName = new File(path + "/" + "MyRoute" + i + "."+extension);
					} else { // file not exist,then create
						fileName.createNewFile();
						break;

					}
				}
				// write column first into the file
				String columnString = "\"Lat\",\"Lng\"\n";

				fileWritter = new FileWriter(fileName, true);// true:append file
				bufferWritter = new BufferedWriter(fileWritter);

				appendDataIntoFile(columnString);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

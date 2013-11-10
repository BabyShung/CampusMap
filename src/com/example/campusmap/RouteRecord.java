package com.example.campusmap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class RouteRecord {
	
	private File path = null;
	private File fileName = null;
	private FileWriter fileWritter;
	private BufferedWriter bufferWritter;
	private String state;
	private boolean canW, canR;
	private boolean recordIsStarted = false;
	public RouteRecord(){

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
	
	public void closeBuffer(){
		
		try {
			bufferWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	public void fileInitialization() {

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
				fileName = new File(path + "/" + "MyRoute1.csv");
				for (int i = 2; i < 100; i++) {
					if (fileName.exists()) { // if exist,then change name
						fileName = new File(path + "/" + "MyRoute" + i + ".csv");
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

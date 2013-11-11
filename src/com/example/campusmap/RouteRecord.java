package com.example.campusmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
	private String filePath = null;
	private FileWriter fileWritter;
	private BufferedWriter bufferWritter;
	private BufferedReader bufferReader;
	private String state;
	private boolean canW, canR;
	private boolean recordIsStarted = false;
	BlockingQueue<LatLng> buffer;

	public RouteRecord() {
		buffer = new ArrayBlockingQueue<LatLng>(20);
		;
	}

	public void toggleRecordState() {
		recordIsStarted = !recordIsStarted;
	}

	public boolean recordHasStarted() {
		return recordIsStarted;
	}

	public String getFileName() {
		return fileName.getName();
	}

	public void bufferStore(Location location) {
		try {
			buffer.put(new LatLng(location.getLatitude(), location
					.getLongitude()));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void bufferTakeAndAddToFile() {
		LatLng tmp;
		try {
			tmp = buffer.take();
			// string that will be stored
			String dataString = "\"" + tmp.latitude + "\"," + "\""
					+ tmp.longitude + "\"\n";
			// append data to a file
			this.appendDataIntoFile(dataString);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void checkRemainingElementsInBQandCloseBuffer() {

		try {
			// put remaining ele in BQ
			while (!buffer.isEmpty()) {
				bufferTakeAndAddToFile();
			}

			// close bw
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean RWTrue() {
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

	public void processRecord() {
		// delete consecutive same data
		// read file A -> delete consecutive same -> write to fileB

		try {
		    bufferReader = new BufferedReader(new FileReader(filePath));
			String line = bufferReader.readLine();
			String lastString = null;
			fileInitialization("txt",true);
			while (line != null) {
				if (!line.equals(lastString)) {//write in new file
					
					appendDataIntoFile(line+"\n");
					lastString = line;

				}
				//else not append and move onto next line
				line = bufferReader.readLine();
			}
			bufferReader.close();
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileInitialization(String extension,boolean processing) {

		// set path, we are going to save the txt file into download folder
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		// if not exists, create path directory
		path.mkdirs();
		// check if we have access to write
		checkState();

		if (RWTrue()) {
			try {
				String fileBeta = "";
				if(processing)
					fileBeta = "_p";
				// create a new file
				fileName = new File(path + "/" + "MyRoute1"+ fileBeta +"." + extension);
				for (int i = 2; i < 100; i++) {
					if (fileName.exists()) { // if exist,then change name
						fileName = new File(path + "/" + "MyRoute" + i + fileBeta + "."
								+ extension);
					} else { // file not exist,then create
						fileName.createNewFile();
						filePath = fileName.getPath();
						System.out.println("****Stored file path: " + filePath);
						break;

					}
				}

				fileWritter = new FileWriter(fileName, true);// true:append file
				bufferWritter = new BufferedWriter(fileWritter);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

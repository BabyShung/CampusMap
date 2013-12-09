package com.example.campusmap.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.Environment;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.db_object.DB_Route;
import com.example.campusmap.geometry.GoogleLatLngDistance;
import com.example.campusmap.routefilter.KalmanLatLong;
import com.example.campusmap.routefilter.Location_Hao;
import com.google.android.gms.maps.model.LatLng;

public class FileOperations {

	private File path = null;
	private File fileName = null;
	private File fileName_p = null;
	private String filePath = null;
	private String filePath_p = null;
	private FileWriter fileWritter;
	private FileWriter fileWritter_p;
	private BufferedWriter bufferWritter;
	private BufferedWriter bufferWritter_p;
	private BufferedReader bufferReader;
	private String state;
	private boolean canW, canR;

	public FileOperations() {

	}

	/**
	 * read a file
	 * 
	 * @param fn
	 * @return
	 */
	public ArrayList<LatLng> readPointsFile(String fn) {// for showing path on
														// map

		ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
		checkDownloadFolderExist();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/"
					+ fn + ".txt"));
			double lat, lng;
			String line = br.readLine();
			while (line != null) {
				String[] tmp = line.split(";");
				for (String tmpS : tmp) {
					String[] tmpTwo = tmpS.split(",");
					lat = Double.parseDouble(tmpTwo[0]);
					lng = Double.parseDouble(tmpTwo[1]);
					listOfPoints.add(new LatLng(lat, lng));
				}
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfPoints;
	}

	/**
	 * file init
	 * 
	 * @param extension
	 */
	public void fileInitialization(String extension) {
		checkDownloadFolderExist();
		// check if we have access to write
		checkState();
		if (RWTrue()) {
			try {
				// create a new file
				fileName = new File(path + "/" + "MyRoute1." + extension);
				for (int i = 2; i < 100; i++) {
					if (fileName.exists()) { // if exist,then change name
						fileName = new File(path + "/" + "MyRoute" + i + "."
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

	private void file_p_Initialization(String extension, File fn, String beta,
			boolean append) {
		checkDownloadFolderExist();
		System.out.println("****get file name: " + fn.getName());

		try {
			String tmp = fn.getName().replaceAll("." + extension,
					"_" + beta + "." + extension);
			System.out.println("****tmp: " + tmp);
			fileName_p = new File(path + "/" + tmp);
			fileName_p.createNewFile();
			filePath_p = fileName_p.getPath();
			System.out.println("****Stored processed file_p path: "
					+ filePath_p);
			fileWritter_p = new FileWriter(fileName_p, append);// true:append
																// file
			bufferWritter_p = new BufferedWriter(fileWritter_p);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * testing method
	 * 
	 * @param originalFileName
	 */
	public void TESTING(String originalFileName) {
		checkDownloadFolderExist();
		filePath = path + "/" + originalFileName + ".txt";
		fileName = new File(path + "/" + originalFileName + ".txt");
		processRecord_delete_consecutive();
		// for(int i=0;i<5;i++)
		// processRecord_delete_outliers("a");
		for (int i = 0; i < 100; i++)
			processRecord_kalman_filter("a");
	}

	/**
	 * process/improve/filter route_file methods
	 * 
	 * @author haozheng
	 */

	// in use
	public void processRecord_delete_consecutive() {
		// delete consecutive same data
		// read file A -> delete consecutive same -> write to fileB
		try {
			bufferReader = new BufferedReader(new FileReader(filePath));
			String line = bufferReader.readLine();
			file_p_Initialization("txt", fileName, "a", false);// save as
																// "xx_a.txt"
			while (line != null) {
				String[] tmp = line.split(";");

				Location_Hao lastL = new Location_Hao(tmp[0]);
				appendDataIntoFile_p(lastL.toString() + ";");

				for (int i = 1; i < tmp.length; i++) {
					Location_Hao current = new Location_Hao(tmp[i]);
					if (!lastL.LocationTheSame(current)) {
						appendDataIntoFile_p(current.toString() + ";");
						lastL = current;
					}
				}
				// else not append and move onto next line
				line = bufferReader.readLine();
			}
			System.out.println("prcessed 1st..");
			bufferReader.close();
			bufferWritter_p.close();
			fileWritter_p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// in use
	public void processRecord_kalman_filter(String beta) {// Kalman Filter
															// Process!!
		try {
			bufferReader = new BufferedReader(new FileReader(filePath_p));
			String line = bufferReader.readLine();
			file_p_Initialization("txt", fileName, beta, false);// save as
																// "xx_beta.txt"

			while (line != null) {
				String[] tmp = line.split(";");

				KalmanLatLong kf = new KalmanLatLong();
				Location_Hao current;

				for (int i = 0; i < tmp.length; i++) {
					current = new Location_Hao(tmp[i]);
					kf.Process(current.getX(), current.getY(), 1,
							current.getTS());
					appendDataIntoFile_p(kf.toString());
				}
				// else not append and move onto next line
				line = bufferReader.readLine();
			}
			System.out.println("prcessed Kalman Filter!!..");
			bufferReader.close();
			bufferWritter_p.close();
			fileWritter_p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DB_Route calculate_distance_time_andGet_StartEndLatLng() {
		DB_Route returnRouteObj = null;
		try {
			// read xx_a.txt file
			bufferReader = new BufferedReader(new FileReader(filePath_p));
			String line = bufferReader.readLine();

			while (line != null) {
				String[] tmp = line.split(";");

				if (tmp.length < 2) {
					// in this case, will not upload, will not store in db
					return null;
				}

				// take first one and last one
				String[] tmpFirst = tmp[0].split(",");
				String[] tmpLast = tmp[tmp.length - 1].split(",");
				double first_lat = Double.parseDouble(tmpFirst[0]);
				double first_lng = Double.parseDouble(tmpFirst[1]);
				double last_lat = Double.parseDouble(tmpLast[0]);
				double last_lng = Double.parseDouble(tmpLast[1]);

				// calculate time
				long first_time = Long.parseLong(tmpFirst[2]);
				long last_time = Long.parseLong(tmpLast[2]);
				long delta = last_time - first_time;

				//time,unit: second
				long takeTime = TimeUnit.MILLISECONDS.toSeconds(delta);
				
				// calculate distance
				Location_Hao current1;
				Location_Hao current2;
				double distance = 0;
				
				//distance between two LatLng points
				GoogleLatLngDistance glld = new GoogleLatLngDistance();
				
				for (int i = 0; i < tmp.length - 1; i++) {
					current1 = new Location_Hao(tmp[i]);
					current2 = new Location_Hao(tmp[i + 1]);

					distance += glld.GetDistance(current1.getX(), current1.getY(), current2.getX(), current2.getY());
				}
				// else not append and move onto next line
				line = bufferReader.readLine();

				returnRouteObj = new DB_Route(first_lat, first_lng, last_lat,
						last_lng, distance, takeTime);

				System.out.println("*first_lat: " + first_lat);
				System.out.println("*first_lng: " + first_lng);
				System.out.println("*last_lat: " + last_lat);
				System.out.println("*last_lng: " + last_lng);
				System.out.println("*distance: " + distance);
				System.out.println("*takeTime: " + takeTime);
			}
			System.out.println("Calculate a bunch of stuffs!");
			bufferReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnRouteObj;
	}

	// not in use
	public void processRecord_delete_outliers(String beta) {
		try {
			bufferReader = new BufferedReader(new FileReader(filePath_p));
			String line = bufferReader.readLine();
			file_p_Initialization("txt", fileName, beta, false);// save as
																// "xx_c.txt"
			while (line != null) {
				String[] tmp = line.split(";");
				Location_Hao first = new Location_Hao(tmp[0]);
				Location_Hao second = new Location_Hao(tmp[1]);
				Location_Hao Li;
				Location_Hao tmpP;
				for (int i = 2; i < tmp.length; i++) {
					Li = new Location_Hao(tmp[i]);
					if (first.checkNextPointInScope(second, Li)) {
						// true, do nothing
					} else {// false, replace second with mid
						tmpP = first.getMidPoint();
						if (tmpP != null)
							second = tmpP;
						tmp[i - 1] = second.toString();
					}
					first = second;
					second = Li;
				}
				// now can store tmp into file again
				for (String f : tmp) {
					appendDataIntoFile_p(f + ";");
				}
				line = bufferReader.readLine();
			}
			System.out.println("prcessed delete outliers..");
			bufferReader.close();
			bufferWritter_p.close();
			fileWritter_p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * file write
	 * 
	 * @param data
	 */
	public void appendDataIntoFile(String data) {
		try {
			// just execute writing
			bufferWritter.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendDataIntoFile_p(String data) {
		try {
			// just execute writing
			bufferWritter_p.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * file close
	 */
	public void closeBufferWriter() {
		try {
			// close bw
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeFileWriter() {
		try {
			// close bw
			fileWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * other methods
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName.getName();
	}

	public String getProcessedFileName() {
		return fileName_p.getName();
	}

	public boolean RWTrue() {
		return canW && canR;
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

	/**
	 * insert all the route info into device DB
	 */
	public void insertDataIntoDB(DB_Route dbr) {

		// dbr.taketime is long! how to solve?

		/**
		 * 1.fn 2.Startlat, Startlng 3.EndLat,EndLng 4.distance 5.route_time
		 */
 
		DB_Operations op = new DB_Operations();
		op.open();
		op.insertARoute(dbr, true);// need to add more attributes
		op.close();
	}

	public void checkDownloadFolderExist() {
		// set path, we are going to save the txt file into download folder
		path = Environment
				.getExternalStoragePublicDirectory("CampusMap/Routes");
		// if not exists, create path directory
		path.mkdirs();
	}
	// ---------------------------------------------------------
}

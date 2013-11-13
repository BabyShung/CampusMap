package com.example.campusmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.os.Environment;

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

	private void checkDownloadFolderExist() {
		// set path, we are going to save the txt file into download folder
		path = Environment
				.getExternalStoragePublicDirectory("CampusMap/Routes");
		// if not exists, create path directory
		path.mkdirs();
	}

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

	public ArrayList<LatLng> readPointsFile(String fn) {

		ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
		checkDownloadFolderExist();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/"
					+ fn + "_p.txt"));
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

	private Point getPoint(String s) {

		String[] tmpTwo = s.split(",");
		double x = Double.parseDouble(tmpTwo[0]);
		double y = Double.parseDouble(tmpTwo[1]);

		return new Point(x, y);
	}

	private void file_p_Initialization(String extension, File fn) {
		checkDownloadFolderExist();
		System.out.println("****get file name: " + fn.getName());

		try {
			String tmp = fn.getName().replaceAll("." + extension,
					"_p." + extension);
			System.out.println("****tmp: " + tmp);
			fileName_p = new File(path + "/" + tmp);
			fileName_p.createNewFile();
			filePath_p = fileName_p.getPath();
			System.out.println("****Stored processed file_p path: "
					+ filePath_p);

			fileWritter_p = new FileWriter(fileName_p, true);// true:append file
			bufferWritter_p = new BufferedWriter(fileWritter_p);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void processRecord() {
		// delete consecutive same data
		// read file A -> delete consecutive same -> write to fileB
		try {
			//filePath = "/storage/emulated/0/CampusMap/Routes/MyRoute1.txt";
			//fileName = new File("/storage/emulated/0/CampusMap/Routes/MyRoute1_p.txt");
			
			bufferReader = new BufferedReader(new FileReader(filePath));
			String line = bufferReader.readLine();
			file_p_Initialization("txt", fileName);// save as "xx_p.txt"
			while (line != null) {
				String[] tmp = line.split(";");
				int lastInt = 0;
				appendDataIntoFile_p(tmp[lastInt] + ";");
				for (int i = 1; i < tmp.length; i++) {
					if (!tmp[i].equals(tmp[lastInt])) {
						appendDataIntoFile_p(tmp[i] + ";");
						lastInt = i;
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

	public void processRecord_2() {
		// process 2nd time
		try {
			bufferReader = new BufferedReader(new FileReader(filePath_p));
			String line = bufferReader.readLine();
			file_p_Initialization("txt", fileName);// resave as "xx_p.txt"

			while (line != null) {
				String[] tmp = line.split(";");

				Point first = getPoint(tmp[0]);
				Point second = getPoint(tmp[1]);
				Point Pi;
				Point tmpP;
				for (int i = 2; i < tmp.length; i++) {

					Pi = getPoint(tmp[i]);

					if (first.checkNextPointInScope(second, Pi)) {
						// true, do nothing
					} else {// false, replace second with mid
						tmpP = first.getMidPoint();
						if (tmpP != null)
							second = tmpP;
						tmp[i - 1] = second.toString();
					}
					first = second;
					second = Pi;
					
				}

				// now can store tmp into file again
				for (String f : tmp) {
					appendDataIntoFile_p(f + ";");
				}

				line = bufferReader.readLine();
			}
			System.out.println("prcessed 2nd..");
			bufferReader.close();
			bufferWritter_p.close();
			fileWritter_p.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	public String getFileName() {
		return fileName.getName();
	}

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

}

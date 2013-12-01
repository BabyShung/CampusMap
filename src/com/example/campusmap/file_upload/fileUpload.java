package com.example.campusmap.file_upload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.campusmap.database.DB_Operations;

import android.os.Environment;

public class fileUpload {

	private int serverResponseCode = 0;
	private String ServerUri = null;

	/********** File Path *************/
	private File uploadFilePath;
	private final String uploadFileName;
	private boolean uploadSucceed;
	// = "service_lifecycle.png";

	public fileUpload(String fn) {

		uploadFileName = fn;

		uploadFilePath = checkDownloadFolderExist();

		ServerUri = "http://1.campusgps.sinaapp.com/post.php";

	}

	public void upload() {
		uploadFile(uploadFilePath + "/" + uploadFileName);
	}

	public boolean isUploadSucceed(){
		return uploadSucceed;
	}
	
	public File checkDownloadFolderExist() {
		File tmp;
		tmp = Environment.getExternalStoragePublicDirectory("CampusMap/Routes");
		// if not exists, create path directory
		tmp.mkdirs();
		return tmp;
	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			System.out.println("Source File not exist :" + uploadFilePath + ""
					+ uploadFileName);
			return 0;
		} else // file exists in the device
		{
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(ServerUri);
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				// $_POST["uploaded_file"] = filename (including path)
				// upl is corresponded to the input name, php server as well
				conn.setRequestProperty("upl", fileName);

				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='upl';filename='"
						+ fileName + "'" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				System.out.println("HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					System.out.println("Upload succeeded!");
					uploadSucceed = true;
				} else {

				
				}
				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				
				
				//upload failure, might be network problem
				//Thus store the route in DB
				insertRouteIntoHistoryTable(uploadFileName);
				System.out.println("Upload failed, insert into db");
				
				
			}
			return serverResponseCode;
		} // End else block
	}

	private void insertRouteIntoHistoryTable(String fileName) {
		DB_Operations op = new DB_Operations();
		op.open();
		op.insertARoute(fileName,false);//need to add more attributes
		op.close();
	}
}

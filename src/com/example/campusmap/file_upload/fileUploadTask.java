package com.example.campusmap.file_upload;
import android.os.AsyncTask;

//Async <inputtype, progresstype, returntype>
public class fileUploadTask extends AsyncTask<Void, Integer, String> {

	private String JustFileName;

	public fileUploadTask(String JustFileName) {
		this.JustFileName = JustFileName;
	}
	
	protected void onPreExecute(String b) {}

	@Override
	protected String doInBackground(Void... arg0) {
		
		System.out.println("-----------------------*********#####");
		
		fileUpload fu = new fileUpload(JustFileName);
		fu.upload();
		
		return null;
	}

	protected void onProgressUpdated(Integer... progress) {}

	protected void onPostExecute(String r) {}
}
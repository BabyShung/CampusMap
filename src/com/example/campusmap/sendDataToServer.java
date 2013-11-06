package com.example.campusmap;

import android.os.AsyncTask;

// Async <inputtype, progresstype, returntype>
public class sendDataToServer extends AsyncTask<String, Integer, String> {

	//new sendDataToServer().execute("haha");
	
	// pre execute
	protected void onPreExecute(String b) {

	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	protected void onProgressUpdated(Integer... progress) {

	}

	protected void onPostExecute(String r) {

	}
}
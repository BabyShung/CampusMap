package com.example.campusmap.location;

import android.os.AsyncTask;

//Async <inputtype, progresstype, returntype>
public class MyLocationTask extends AsyncTask<Void, Integer, String> {

	private MyLocation ml;

	
	public MyLocationTask(MyLocation ml,String destination) {
		this.ml = ml;
		this.ml.setDestination(destination);
	}

	protected void onPreExecute(String b) {
	}

	@Override
	protected String doInBackground(Void... arg0) {
		ml.beginRoute();
		return null;
	}
	protected void onProgressUpdated(Integer... progress) {}
	protected void onPostExecute(String r) {}
}
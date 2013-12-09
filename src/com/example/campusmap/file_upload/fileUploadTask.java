package com.example.campusmap.file_upload;
import com.example.campusmap.db_object.DB_Route;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

//Async <inputtype, progresstype, returntype>
public class fileUploadTask extends AsyncTask<Void, Integer, String> {

	private Context mContext;
	private DB_Route dbr;
	private fileUpload fu;
	
	public fileUploadTask(DB_Route dbr, Context mContext) {
		this.dbr = dbr;
		this.mContext = mContext;
	}
 
	@Override
	protected String doInBackground(Void... arg0) {
		fu = new fileUpload(dbr);
		fu.upload();
		return null;
	}
 
	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);
		System.out.println("post executed");
		if(fu.isUploadSucceed()){
			Toast.makeText(mContext, "Uploaded to server",
					Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(mContext, "No network. Will upload later.",
					Toast.LENGTH_LONG).show();
		}
	}
}
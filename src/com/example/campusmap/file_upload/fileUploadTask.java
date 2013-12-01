package com.example.campusmap.file_upload;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

//Async <inputtype, progresstype, returntype>
public class fileUploadTask extends AsyncTask<Void, Integer, String> {

	private Context mContext;
	private String JustFileName;
	private fileUpload fu;
	public fileUploadTask(String JustFileName, Context mContext) {
		this.JustFileName = JustFileName;
		this.mContext = mContext;
	}
	
	protected void onPreExecute(String b) {}

	@Override
	protected String doInBackground(Void... arg0) {
		fu = new fileUpload(JustFileName);
		fu.upload();
		return null;
	}

	protected void onProgressUpdated(Integer... progress) {}
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
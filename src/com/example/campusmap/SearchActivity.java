package com.example.campusmap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.db_object.DB_Building;

public class SearchActivity extends Activity {

	private InputMethodManager imm;
	private ArrayList<DB_Building> value;
	private ArrayList<String> buildingList;
	private DB_Operations datasource;
	private ListView LV;
	private AutoCompleteTextView ATV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		buildingList = new ArrayList<String>();
		

		initData();
		
		populateATV();

	}

	private void initData() {
		// read info from db , building object
		readBuildingObjectFromDatabase();
		getBuildingList();

		populateLV();
		clearBTN();
	}

	private void clearBTN() {
		Button clearBTN = (Button) findViewById(R.id.searchBTN);

		clearBTN.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ATV.setText("");
			}
		});
	}

	private void populateLV() {
		ArrayAdapter<DB_Building> adapter = new MyListAdapter();
		LV = (ListView) findViewById(R.id.searchLV);
		LV.setAdapter(adapter);
		LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lv, View view,
					int position, long id) {
				popDialog(value.get(position).getBuildingName());

			}
		});
	}

	private class MyListAdapter extends ArrayAdapter<DB_Building> {
		public MyListAdapter() {
			super(SearchActivity.this, R.layout.activity_search_listviewitem,
					value);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.activity_search_listviewitem, parent, false);
			}

			// Find building to work with
			DB_Building currentBuilding = value.get(position);

			// Fill the name
			TextView nameText = (TextView) itemView
					.findViewById(R.id.item_name);
			nameText.setText(currentBuilding.getBuildingName());
			// Fill the times
			TextView timesText = (TextView) itemView
					.findViewById(R.id.item_times);
			int query_time = currentBuilding.getQueryTime();
			if (query_time == 0)
				timesText.setText("");// howtoString?
			else if (query_time == 1)
				timesText.setText(query_time + " time "); // howtoString?
			else
				timesText.setText(query_time + " times");

			
			 //Fill the icons we don't have Icons for buildings in DB yet
			 ImageView imageView =(ImageView)itemView.findViewById(R.id.item_icon);
			imageView.setImageResource(currentBuilding.getBuildingIcon());  //int bi_1=0x7f020000;
			 
			return itemView;

		}

	}

	private void populateATV() {

		ATV = (AutoCompleteTextView) findViewById(R.id.searchACTV);
		ArrayAdapter<String> ATVadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, buildingList);
		ATV.setThreshold(1);
		ATV.setAdapter(ATVadapter);
		ATV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> ATV, View view,
					int position, long id) {

				String buildingName = ATV.getItemAtPosition(position)
						.toString();
				// pop up a dialog
				popDialog(buildingName);
			}
		});

	}

	private void getBuildingList() {
		for (DB_Building tmp : value) {
			buildingList.add(tmp.getBuildingName());
		}
	}

	private void broadcastMsg(String bn) {
		sendMessageToMainActivity();
		sendMessageToHomeActivity("SearchActivity",bn);
	}

	private void sendMessageToMainActivity() {
		Intent intent = new Intent("SetTab");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void sendMessageToHomeActivity(String activity,String bn) {
		Intent intent = new Intent("GetDirection");
		intent.putExtra("Activity", activity);
		intent.putExtra("BuildingName", bn);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void readBuildingObjectFromDatabase() {
		datasource = new DB_Operations(this);
		datasource.open();
		value = datasource.getBuildingOBJWithTimes();
		datasource.close();
	}

	private void updateQueryTimesIntoDatabase(String bn) {
		DB_Operations dbo = new DB_Operations();
		dbo.open();
		dbo.updateQueryTimesForABuilding(bn);
		dbo.close();
	}

	private void popDialog(final String bn) {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		alertDialog.setTitle(bn);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Go",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// broadcast send message
						broadcastMsg(bn);

						// hide keyboard
						imm.hideSoftInputFromWindow(ATV.getWindowToken(), 0);

						// update the times in db
						updateQueryTimesIntoDatabase(bn);

						// refresh the lists
						initData();
					}
				});

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Info",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// hide keyboard
						imm.hideSoftInputFromWindow(ATV.getWindowToken(), 0);

						// ----
					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// hide keyboard
						imm.hideSoftInputFromWindow(ATV.getWindowToken(), 0);
						// ---
					}
				});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();

	}

}
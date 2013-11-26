package com.example.campusmap;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.db_object.DB_Building;

public class SearchActivity extends Activity {

	private ArrayList<DB_Building> value;
	private ArrayList<String> buildingList;
	private ArrayList<String> querytimesList;
	private DB_Operations datasource;
	private ListView LV;
	private AutoCompleteTextView ATV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		buildingList = new ArrayList<String>();
		querytimesList = new ArrayList<String>();
		
		// read info from db , building names
		readBuildingNamesInfoFromDatabase();
		getBuildingList();
		getQueryTimesList();

		populateLV();
		populateATV();
		clearBTN();

	}

	private void clearBTN() {
		// TODO Auto-generated method stub
		final Button button = (Button) findViewById(R.id.searchBTN);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	ATV.setText("");
            }
        });
	}

	private void populateLV() {
		// TODO Auto-generated method stub
		ArrayAdapter<DB_Building> adapter = new MyListAdapter();
		LV = (ListView) findViewById(R.id.searchLV);
		LV.setAdapter(adapter);
		LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> ATV, View view,
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
			if (currentBuilding.getQueryTime() == 0)
				timesText.setText("");// howtoString?
			else
				timesText.setText("" + currentBuilding.getQueryTime()); // howtoString?

			/*
			 * //Fill the icons we don't have Icons for buildings in DB yet
			 * ImageView imageView =
			 * (ImageView)itemView.findViewById(R.id.item_icon);
			 * imageView.setImageResource(currentBuilding.getIconID());
			 */
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

	private void getQueryTimesList() {
		for (DB_Building tmp : value) {
			int times = tmp.getQueryTime();
			if (times != 0)
				querytimesList.add(times + "");
			else
				querytimesList.add("");
		}
	}

	private void broadcastMsg(String bn) {
		sendMessageToMainActivity();
		sendMessageToHomeActivity(bn);
	}

	private void sendMessageToMainActivity() {
		Intent intent = new Intent("SetTab");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void sendMessageToHomeActivity(String bn) {
		Intent intent = new Intent("GetGoogleDirection");
		intent.putExtra("BuildingName", bn);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void readBuildingNamesInfoFromDatabase() {
		datasource = new DB_Operations(this);
		datasource.open();
		value = datasource.getBuildingNamesWithTimes();
		datasource.close();
	}

	private void popDialog(final String bn) {

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(bn);
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Go",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// broadcast send message
						broadcastMsg(bn);

					}

				});

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Info",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						// ----
					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// ---
					}
				});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();

	}

}
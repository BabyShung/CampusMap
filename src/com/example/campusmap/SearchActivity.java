package com.example.campusmap;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;
import com.example.campusmap.db_object.DB_Building;

public class SearchActivity extends ListActivity implements TableDefinition {

	private ArrayList<DB_Building> value;
	private ArrayList<String> buildingList;
	private ArrayList<String> querytimesList;
	private DB_Operations datasource;
	private AutoCompleteTextView ATV;
	private ArrayAdapter<String> ATVadapter;

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

		ATV = (AutoCompleteTextView) findViewById(R.id.searchACTV);
		
		setListAdapter(new ArrayAdapter<String>(SearchActivity.this,
				android.R.layout.simple_list_item_1, buildingList));

		ATVadapter = new ArrayAdapter<String>(this,
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// pop up a dialog
		popDialog(value.get(position).getBuildingName());

	}

	private void getBuildingList() {
		for (DB_Building tmp : value) {
			buildingList.add(tmp.getBuildingName());
		}
	}
	
	private void getQueryTimesList() {
		for (DB_Building tmp : value) {
			int times = tmp.getQueryTime();
			if(times!=0)
				querytimesList.add(times+"");
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

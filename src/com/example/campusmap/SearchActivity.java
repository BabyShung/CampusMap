package com.example.campusmap;

import java.util.ArrayList;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class SearchActivity extends ListActivity implements TableDefinition {

	private static ArrayList<String> value = null;
	private DB_Operations datasource;
	private AutoCompleteTextView ATV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ATV = (AutoCompleteTextView) findViewById(R.id.searchACTV);

		//read info from db , building names
		readBuildingNamesInfoFromDatabase();

		
		setListAdapter(new ArrayAdapter<String>(SearchActivity.this,
				android.R.layout.simple_list_item_1, value));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, value);
		ATV.setThreshold(1);
		ATV.setAdapter(adapter);


	}

	private void readBuildingNamesInfoFromDatabase() {
		datasource = new DB_Operations(this);
		datasource.open();
		value = datasource.getBuildingNames();
		datasource.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		ATV.setText(value.get(position));

		//broadcast send message
		sendMessageToMainActivity();
		sendMessageToHomeActivity(value.get(position));
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

}
package com.example.campusmap;

import java.util.ArrayList;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;

import android.os.Bundle;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

		datasource = new DB_Operations(this);
		datasource.open();

		Cursor c = datasource.readData();
		c.moveToFirst();
		value = new ArrayList<String>();
		while (c.moveToNext()) {
			String Name = c.getString(0);
			value.add(Name);
		}

		setListAdapter(new ArrayAdapter<String>(SearchActivity.this,
				android.R.layout.simple_list_item_1, value));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, value);

		ATV.setThreshold(1);
		ATV.setAdapter(adapter);
		datasource.close();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		ATV.setText(value.get(position));

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

	@SuppressWarnings("unused")
	private SQLiteDatabase openDatabase(String string, int modePrivate,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}
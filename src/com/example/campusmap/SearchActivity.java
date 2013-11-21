package com.example.campusmap;

import java.util.ArrayList;
 
  
import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;
 
import android.os.Bundle;
 
import android.app.ListActivity; 
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
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

	}

	@SuppressWarnings("unused")
	private SQLiteDatabase openDatabase(String string, int modePrivate,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	// ???Get user input and search from database
	public void onClick_SEARCH(View v) {
		
		System.out.println("outoutout");
		
		String tfv = ATV.getText().toString();
		// if the textfield is not empty, do the search
		if (!tfv.equals("")) {

			
			
			Intent i = new Intent(getApplicationContext(),HomeActivity.class);
			i.putExtra("BN", tfv);
			startActivity(i);

			
			
		}

	}
	
//	private void CallDirection() { // Async task
//
//		new WebServiceTask(this, map, fromPosition, toPosition).execute();
//	}

}
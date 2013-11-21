package com.example.campusmap;

import java.util.ArrayList;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;


public class FutureActivity extends ListActivity implements TableDefinition {

	private static ArrayList<String> value = null;
	private DB_Operations datasource;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_future);	
			
			
			datasource = new DB_Operations(this);
			datasource.open();

	  

			setListAdapter(new ArrayAdapter<String>(FutureActivity.this,
					android.R.layout.simple_list_item_1,   datasource.getRouteInfo()));

//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_dropdown_item_1line, value); 


			datasource.close();
			
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
 

	}
	

}
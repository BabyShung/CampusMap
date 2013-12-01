package com.example.campusmap;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;

public class RoutesActivity extends ListActivity implements
		TableDefinition {

	private DB_Operations datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_future);

		//read info from db , route
		readRoutesInfoFromDatabase();


	}

	private void readRoutesInfoFromDatabase() {
//		datasource = new DB_Operations(this);
//		datasource.open();
//		setListAdapter(new ArrayAdapter<String>(RoutesActivity.this,
//				android.R.layout.simple_list_item_1, datasource.getRouteInfo()));
//		datasource.close();	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

	}

}
package com.example.campusmap;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.db_object.DB_Route;

public class RouteActivity extends Activity {

	private DB_Operations datasource;
	private ArrayList<DB_Route> routeList;
	private ListView LV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);

		//read info from db , route
		readRoutesInfoFromDatabase();
		populateLV();

	}
	
	private void populateLV() {
		ArrayAdapter<DB_Route> adapter = new MyListAdapter();
		LV = (ListView) findViewById(R.id.routeLV);
		LV.setAdapter(adapter);
		LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> ATV, View view,
					int position, long id) {
				//....click item event

			}
		});
	}

	private class MyListAdapter extends ArrayAdapter<DB_Route> {
		public MyListAdapter() {
			super(RouteActivity.this, R.layout.activity_route_listviewitem,
					routeList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.activity_route_listviewitem, parent, false);
			}

			// Find route to work with
			DB_Route currentRoute = routeList.get(position);

			// Fill the route name
			TextView nameText = (TextView) itemView
					.findViewById(R.id.route_item_name);
			nameText.setText(currentRoute.getRid());
			
			return itemView;

		}

	}
	

	
	private void readRoutesInfoFromDatabase() {
		datasource = new DB_Operations(this);
		datasource.open();
		routeList = datasource.getRouteInfo(true);
		//setListAdapter(new ArrayAdapter<String>(RoutesActivity.this,
		//		android.R.layout.simple_list_item_1, datasource.getRouteInfo()));
		datasource.close();	
	}



}
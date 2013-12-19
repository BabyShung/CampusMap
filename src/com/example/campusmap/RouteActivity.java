package com.example.campusmap;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
			public void onItemClick(AdapterView<?> lv, View view,
					int position, long id) {
				//....click item event
				String fileName = LV.getItemAtPosition(position)
						.toString();
				broadcastMsg(fileName);
			}
		});
	}

	private void broadcastMsg(String bn) {
		sendMessageToMainActivity();
		sendMessageToHomeActivity("RouteActivity",bn);
	}

	private void sendMessageToMainActivity() {
		Intent intent = new Intent("SetTab");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void sendMessageToHomeActivity(String activity,String bn) {
		Intent intent = new Intent("GetDirection");
		intent.putExtra("Activity", activity);
		intent.putExtra("FileName", bn);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
			nameText.setText(currentRoute.getRid()+".to:");
			
			//route destination
			TextView destinationText = (TextView) itemView
					.findViewById(R.id.route_item_destination);
			destinationText.setText(currentRoute.getDestination());
			
			//route distance and time
			TextView distanceText = (TextView) itemView
			.findViewById(R.id.route_item_distance);
			distanceText.setText(currentRoute.getDistance() + "m  "+ currentRoute.getTakeTime() + "'s");
	
			
			return itemView;

		}

	}
	

	
	private void readRoutesInfoFromDatabase() {
		datasource = new DB_Operations(this);
		datasource.open();
		routeList = datasource.getRouteInfo(true);
		
//		for(DB_Route tmp : routeList){
//			System.out.println(tmp.getRid());
//			System.out.println(tmp.getFileName());
//			System.out.println(tmp.getStarting_lat());
//			System.out.println(tmp.getStarting_lng());
//			System.out.println(tmp.getEnding_lat());
//			System.out.println(tmp.getEnding_lng());
//			System.out.println(tmp.getTakeTime());
//			System.out.println(tmp.getDistance());
//			System.out.println(tmp.getCreateTime());
//			System.out.println("^*****************^");
//		}
		
		
		//setListAdapter(new ArrayAdapter<String>(RoutesActivity.this,
		//		android.R.layout.simple_list_item_1, datasource.getRouteInfo()));
		datasource.close();	
	}



}
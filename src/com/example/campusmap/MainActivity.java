package com.example.campusmap;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.DatabaseEntry;

public class MainActivity extends Activity {
	// private DB_Operations datasource;
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private LocalActivityManager lam;

	private Intent homeIntent;
	private Intent searchIntent;
	private Intent settingIntent;
	private Intent futureIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		databaseInitCheck();
		// set up the entire UI framework
		lam = new LocalActivityManager(MainActivity.this, false);
		lam.dispatchCreate(savedInstanceState);
		setUpTabHost();
		setUpTabWiget();
	}

	private void setUpTabWiget() {
		// TODO Auto-generated method stub

		mTabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			/*
			 * //change Tabhost's size
			 * mTabWidget.getChildAt(i).getLayoutParams().height = 30;
			 * mTabWidget.getChildAt(i).getLayoutParams().width = 65;
			 */

			// change text font and position
			TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			tv.setTextSize(20);
			tv.setTextColor(0xffffc000); // BALCK:-16777216
			tv.setTypeface(null, Typeface.BOLD);

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); // cancel
																	// align
																	// bottom
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); // set
																					// in
																					// middle
		}

	}

	private void setUpTabHost() {

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(lam);

		TabSpec homeSpec = mTabHost.newTabSpec("map");
		homeSpec.setIndicator("MAP", null);
		homeIntent = new Intent(this, MapActivity.class);
		homeSpec.setContent(homeIntent);

		TabSpec searchSpec = mTabHost.newTabSpec("search");
		searchSpec.setIndicator("SEARCH", null);
		searchIntent = new Intent(this, SearchActivity.class);
		searchSpec.setContent(searchIntent);

		TabSpec settingSpec = mTabHost.newTabSpec("setting");
		settingSpec.setIndicator("SETTING", null);
		settingIntent = new Intent(this, SettingActivity.class);
		settingSpec.setContent(settingIntent);

		TabSpec futureSpec = mTabHost.newTabSpec("myroute");
		futureSpec.setIndicator("ROUTES", null);
		futureIntent = new Intent(this, RoutesActivity.class);
		futureSpec.setContent(futureIntent);

		mTabHost.addTab(homeSpec);
		mTabHost.addTab(searchSpec);
		mTabHost.addTab(settingSpec);
		mTabHost.addTab(futureSpec);

		setuplocalbroadcast();

	}

	// set up the broadcastmanager,when click a building in search, this
	// activity responds
	private void setuplocalbroadcast() {
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMessageReceiver, new IntentFilter("SetTab"));
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mTabHost.setCurrentTab(0);
		}
	};

	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		lam.dispatchPause(isFinishing());
		super.onPause();
	}

	@Override
	protected void onResume() {
		lam.dispatchResume();
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		

		return true;
	}

	private void databaseInitCheck() {
		// check tables,if not exists, create
		DatabaseEntry dbe = new DatabaseEntry(this);
		dbe.createTables();

		// check building_table has rows or not, if not exists, insert
		DB_Operations op = new DB_Operations(this);
		op.open();
		op.DB_init();
		op.uploadPreviousFailedRoute();
		op.getDBPath();
		//op.getCenterPointsFromBuildings();
		op.close();

	}

}

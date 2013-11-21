package com.example.campusmap;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.DatabaseEntry;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainActivity extends Activity{
	//private DB_Operations datasource;
	private TabHost mTabHost;
	private LocalActivityManager lam;

	private Intent homeIntent;
	private Intent searchIntent;
	private Intent settingIntent;
	private Intent futureIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set up the entire UI framework
		lam = new LocalActivityManager(MainActivity.this, false);
		lam.dispatchCreate(savedInstanceState);
		setUpTabHost();
	}
	

	
	
	private void setUpTabHost() {
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(lam);

		TabSpec homeSpec = mTabHost.newTabSpec("home");
		homeSpec.setIndicator("HOME",null);
		homeIntent = new Intent(this, HomeActivity.class);
		homeSpec.setContent(homeIntent);

		TabSpec searchSpec = mTabHost.newTabSpec("search");
		searchSpec.setIndicator("SEARCH",  null);
		searchIntent = new Intent(this, SearchActivity.class);
		searchSpec.setContent(searchIntent);

		TabSpec settingSpec = mTabHost.newTabSpec("setting");
		settingSpec.setIndicator("SETTING", null);
	    settingIntent = new Intent(this, SettingActivity.class);
		settingSpec.setContent(settingIntent);
		
		TabSpec futureSpec = mTabHost.newTabSpec("myroute");
		futureSpec.setIndicator("My Routes", null);
		futureIntent = new Intent(this, FutureActivity.class);
		futureSpec.setContent(futureIntent);
		
		mTabHost.addTab(homeSpec);
		mTabHost.addTab(searchSpec);
		mTabHost.addTab(settingSpec);
		mTabHost.addTab(futureSpec);
		
	 
		//mTabHost.setCurrentTab(1);
		
		//System.out.println("%%%%%"+mTabHost.getCurrentTab());

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
		
		
		
		
		//check tables
		DatabaseEntry dbe = new DatabaseEntry(this);
		dbe.createTables();
		
		//check building_table has rows or not
		DB_Operations op = new DB_Operations(this);
		op.open();
		op.DB_init();
		op.getDBPath();
		op.close();
		
		return true;
	}

	

}

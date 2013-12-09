package com.example.campusmap;

import java.util.ArrayList;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.db_object.DB_Building;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {

	private InputMethodManager imm;
	private ArrayList<DB_Building> value;
	private ArrayList<String> buildingList;
	private DB_Operations datasource;
/*	private ListView LV;
	private AutoCompleteTextView ATV;*/
	private Button button1;
	private Button button2;
	private Button button3;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		setOnButtonListener();

	}

	private void setOnButtonListener() {
		button1 = (Button) findViewById(R.id.setting_button1);
		button2 = (Button) findViewById(R.id.setting_button2);
		button3 = (Button) findViewById(R.id.setting_button3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);

	}

	@Override
	public void onClick(final View v) {

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setIcon(R.drawable.ic_launcher);
		setAlertDialogTitle(v);
		setAlerDiaglogButtonPositive(v);
		setAlerDiaglogButtonNegative(v);	
		alertDialog.show();
	}
	
	private void setAlertDialogTitle(View v) {
		switch (v.getId()) {
		case R.id.setting_button1:
			alertDialog.setTitle("Clear My Search Query History");
			clearSearchHistory();
			break;
		case R.id.setting_button2:
			alertDialog.setTitle("Delete All My Route History");
			clearAllRouteHistory();
			break;
		case R.id.setting_button3:
			alertDialog.setTitle("Switch Map Type");
			break;
		}
	}
	
	
	private void clearAllRouteHistory()
	{
		datasource = new DB_Operations(this);
		datasource.open();
		datasource.deleteAllRoute();
		datasource.close(); 
	}
	private void clearSearchHistory()
	{
		datasource = new DB_Operations(this);
		datasource.open();
		datasource.updateQueryTime_setToNULL();
		datasource.close(); 
	}
	private void setAlerDiaglogButtonPositive(final View v) {
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {					
						switch (v.getId()) {					
						case R.id.setting_button1:
							button1Event();   //unimplemented method is at bottom
							break;					
						case R.id.setting_button2:
							button2Event();//unimplemented method is at bottom
							break;					
						case R.id.setting_button3:
							button3Event();//unimplemented method is at bottom
							break;
						}
					}
				});		
	}



	private void setAlerDiaglogButtonNegative(View v) {
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

					}
				});	
	}

	// clear my search query history
	protected void button1Event() {
		Toast.makeText(SettingActivity.this, "Cleared history",
				Toast.LENGTH_LONG).show();
	}

	// delete all my routes history
	protected void button2Event() {
		Toast.makeText(SettingActivity.this, "Routed deleted",
				Toast.LENGTH_LONG).show();
	}
	// switch map type
	protected void button3Event() {
		Toast.makeText(SettingActivity.this, "Type Switched",
				Toast.LENGTH_LONG).show();
	}

	

}
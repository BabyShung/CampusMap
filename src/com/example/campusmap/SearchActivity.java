package com.example.campusmap;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

public class SearchActivity extends Activity implements TableDefinition {

	private DB_Operations datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		datasource = new DB_Operations(this);
		datasource.open();

		Cursor c = datasource.readData();
		c.moveToFirst();
		StringBuilder builder = new StringBuilder();
		while (c.moveToNext()) {
			String Name = c.getString(0);
			builder.append(Name).append("\n ");
		}
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(builder);

		datasource.close();

	}

	@SuppressWarnings("unused")
	private SQLiteDatabase openDatabase(String string, int modePrivate,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	// ???Get user input and search from database
	public void onClick_SEARCH(View v) {

	}
}
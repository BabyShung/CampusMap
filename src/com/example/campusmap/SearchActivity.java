package com.example.campusmap;

import java.util.ArrayList;
import java.util.List;

import com.example.campusmap.database.DB_Operations;
import com.example.campusmap.database.TableDefinition;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class SearchActivity extends Activity implements TableDefinition {

	private static ArrayList<String> value = null;
	private DB_Operations datasource;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		datasource = new DB_Operations(this);
		datasource.open();

		Cursor c = datasource.readData();
		c.moveToFirst();
		value = new ArrayList<String>();
		while (c.moveToNext()) {
			String Name = c.getString(0);
			value.add(Name);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, value);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.searchACTV);
        textView.setAdapter(adapter);

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
	
	/*public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
		private ArrayList<String> fullList;
		private ArrayList<String> filtredList;
		
		public AutoCompleteAdapter(Context context, int textViewResourceId) {
		    super(context, textViewResourceId);
		    filtredList = new ArrayList<String>();
		    fullList = new ArrayList<String>();
		    fullList.add("one");
		    fullList.add("oneee");
		    fullList.add("two");
		}

		@Override
		public int getCount() {
		    return filtredList.size();
		}

		@Override
		public String getItem(int index) {
		    return filtredList.get(index);
		}
		
		@Override
		public Filter getFilter() {
		    Filter myFilter = new Filter() {
		        @Override
		        protected FilterResults performFiltering(CharSequence constraint) {
		            List<String> resultsSuggestions = new ArrayList<String>();
		            if(constraint != null) {
		                for (int i = 0; i < fullList.size(); i++) {
		                    if(fullList.get(i).startsWith(constraint.toString())){
		                        resultsSuggestions.add(fullList.get(i));
		                    }
		                }
		            }
		            FilterResults results = new FilterResults();
		            results.values = resultsSuggestions;
		            results.count = resultsSuggestions.size();
		            filtredList = (ArrayList<String>) resultsSuggestions;
		            return results;
		        }

		        @SuppressWarnings("unchecked")
		        @Override
		        protected void publishResults(CharSequence contraint, FilterResults results) {
		            try {
		                ArrayList<String> newValues = (ArrayList<String>) results.values;
		                for (int i = 0; i < newValues.size(); i++) {
		                    add(newValues.get(i));
		                }

		                if(results != null) {
		                    notifyDataSetChanged();
		                }
		                else {
		                    notifyDataSetInvalidated();
		                }
		            } catch(Exception e) {
		                Log.v("Near ATM", "Exception ::" + e.getMessage());
		            }
		        }
		    };
		    return myFilter;
		}
	}*/
}
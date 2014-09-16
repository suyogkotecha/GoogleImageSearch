package com.example.googleimagesearcher.activities;

import com.example.googleimagesearcher.R;
import com.example.googleimagesearcher.R.id;
import com.example.googleimagesearcher.R.layout;
import com.example.googleimagesearcher.R.menu;
import com.example.googleimagesearcher.models.FilterParams;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterActivity extends Activity {

	Spinner spType;
	Spinner spColor;
	Spinner spSize;
	EditText etWeb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		spType = (Spinner) findViewById(R.id.spinner1);
		spColor =  (Spinner)findViewById(R.id.spColorSel);
		spSize =  (Spinner)findViewById(R.id.spSizeSel);
		etWeb = (EditText) findViewById(R.id.etWebFilter);
	}

	public void sendFilter(View v){
		//create an object and send it back to the parent on Intent

		FilterParams fp = new FilterParams(spType.getSelectedItem().toString(), spColor.getSelectedItem().toString(), spSize.getSelectedItem().toString(), etWeb.getText().toString().toString());
		Intent i = new Intent();
		i.putExtra("filter", fp);
		setResult(RESULT_OK, i);
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_filter,
					container, false);
			return rootView;
		}
	}

}

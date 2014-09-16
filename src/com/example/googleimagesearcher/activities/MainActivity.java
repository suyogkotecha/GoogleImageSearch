package com.example.googleimagesearcher.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.googleimagesearcher.R;
import com.example.googleimagesearcher.adapters.ImageResultsAdapter;
import com.example.googleimagesearcher.models.FilterParams;
import com.example.googleimagesearcher.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {

	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imgResults;
	public ImageResultsAdapter aImageResults;
	String filterQuery = null;
	int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		setUpViews();
		//Create data source
		imgResults = new ArrayList<ImageResult>();
		
		//Attach data source
		aImageResults = new ImageResultsAdapter(this, imgResults);
		
		//link the adapter to view
		gvResults.setAdapter(aImageResults);
	}

	private void customLoadMoreDataFromApi(int page) {
		// TODO Auto-generated method stub
		this.page = ((page-1)*8)+1;
		String query = etQuery.getText().toString();
		String url = null;
		if(filterQuery!=null){
			url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+this.page+"&"+filterQuery;
		}else{
			url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+this.page;
		}
		//Log.i("INFO", "fromurl:"+url);
		searchImage(query, url);
	}

	private void setUpViews() {
		// TODO Auto-generated method stub
		etQuery = (EditText)findViewById(R.id.etQuery);
		gvResults = (GridView)findViewById(R.id.gvResults);
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				customLoadMoreDataFromApi(page); 
			}

		});
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Launch image
				//create intent
				Intent i = new Intent(MainActivity.this, ImageDisplatyActivity.class);
				//get image result to display
				ImageResult result = imgResults.get(position);
				//pass image result to intent
				i.putExtra("result", result);
				//launch new activity
				startActivity(i);
			}
			
		});
	}


	public void onSetting(MenuItem mi){
		Intent i = new Intent(this, FilterActivity.class);
		startActivityForResult(i, 5);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 5){
			if(resultCode == RESULT_OK){
				FilterParams fp = (FilterParams) data.getSerializableExtra("filter");
				StringBuilder filter = null;
				filter = concatenateFilterParams(filter, "imgsz", fp.size);
				filter = concatenateFilterParams(filter, "imgtype", fp.type);
				filter = concatenateFilterParams(filter, "imgcolor", fp.color);
				filter = concatenateFilterParams(filter, "as_sitesearch", fp.filterWeb);
				filterQuery = filter.toString();
				String query = etQuery.getText().toString();
				String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+this.page+"&"+filterQuery;
				imgResults.clear();
				searchImage(query, url);
				//Toast.makeText(this, filterQuery, Toast.LENGTH_LONG).show();
			}
		}
	}

	public StringBuilder concatenateFilterParams(StringBuilder str, String type, String params){
		if(params!=null && params.length()>=0 && !params.equalsIgnoreCase("")){
			if(str!=null){
				str.append("&");
			}else{
				str = new StringBuilder();
			}
			return str.append(type+"="+params);
		}
		return str;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/**
	 * When search button clicked
	 * @param v
	 */
	public void onImgSearch(View v){
		String query = etQuery.getText().toString();
		String url = null;
		if(filterQuery!=null){
			url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+this.page+"&"+filterQuery;
		}else{
			url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query+"&rsz=8&start="+this.page;
		}

		searchImage(query, url);
	}

	public void searchImage(String query, String url){
		if(query == null || query.length() <=0 || query.equalsIgnoreCase("")){
			return;
		}
		Log.i("INFO", "url:"+url);
		AsyncHttpClient client = new AsyncHttpClient();
		//view-source:https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
		client.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				JSONArray imageResultsJson = null;
				try {
					imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
					//imgResults.clear();
					imgResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
					aImageResults.notifyDataSetChanged();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}

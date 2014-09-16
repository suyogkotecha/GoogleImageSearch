package com.example.googleimagesearcher.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ImageResult implements Serializable{

	public String fullUrl;
	public String tbUrl;
	public String title;
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getTbUrl() {
		return tbUrl;
	}
	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Create obj out of json block
	 * @param json
	 */
	
	public ImageResult(JSONObject json){
		try{
			setFullUrl(json.getString("url"));
			setTbUrl(json.getString("tbUrl"));
			setTitle(json.getString("title"));
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for(int i =0;i<array.length();i++){
			try {
				results.add(new ImageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return results;
	}
}

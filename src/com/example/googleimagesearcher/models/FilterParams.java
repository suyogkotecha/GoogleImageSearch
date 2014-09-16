package com.example.googleimagesearcher.models;

import java.io.Serializable;

public class FilterParams implements Serializable{

	public String type;
	public String color;
	public String size;
	public String filterWeb;

	public static final String ICON = "icon";
	public static final String MEDIUM = "medium";
	public static final String XXLARGE = "xxlarge";
	public FilterParams(String type, String color, String size, String filterWeb) {
		super();
		this.type = type;
		this.color = color;
		this.size = returnCorrectSizeParam(size);
		this.filterWeb = filterWeb;
	}
	
	public String returnCorrectSizeParam(String size){

		if("small".equalsIgnoreCase(size)){
			return ICON;
		}else if("medium".equalsIgnoreCase(size)){
			return MEDIUM;
		}else if("large".equalsIgnoreCase(size)){
			return XXLARGE;
		}
		return "";
	}
	
}

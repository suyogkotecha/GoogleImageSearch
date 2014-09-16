package com.example.googleimagesearcher.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.googleimagesearcher.R;
import com.example.googleimagesearcher.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context, 
			 List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		ImageResult imageInfo = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
		}
		ImageView iv = (ImageView) convertView.findViewById(R.id.ivImage);
		TextView tv = (TextView) convertView.findViewById(R.id.tvTitl);
		//clear out image from the last time
		iv.setImageResource(0);
		//set title
		tv.setText(Html.fromHtml(imageInfo.title));
		//remotely download image data in background
		Picasso.with(getContext()).load(imageInfo.tbUrl).into(iv);
		return convertView;
		
	}
}

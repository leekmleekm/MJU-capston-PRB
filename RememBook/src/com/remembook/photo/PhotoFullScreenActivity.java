package com.remembook.photo;

import com.remembook.R;
import com.remembook.photo.PhotoFullScreenAdapter;
import com.remembook.photo.PhotoGalleryUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PhotoFullScreenActivity extends Activity{

	private PhotoGalleryUtils utils;
	private PhotoFullScreenAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_view);
		
		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
        String path = i.getExtras().get("path").toString(); //gridviewimageadapter에서 title명 받아옴
        
		viewPager = (ViewPager) findViewById(R.id.image_pager);
		utils = new PhotoGalleryUtils(getApplicationContext());
	
		adapter = new PhotoFullScreenAdapter(PhotoFullScreenActivity.this, utils.getFilePaths(path));
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
	}
}
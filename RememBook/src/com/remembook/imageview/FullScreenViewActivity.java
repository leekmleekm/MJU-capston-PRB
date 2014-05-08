package com.remembook.imageview;

import com.remembook.R;
import com.remembook.imageview.FullScreenImageAdapter;
import com.remembook.imageview.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenViewActivity extends Activity{

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.imageview_view);
		viewPager = (ViewPager) findViewById(R.id.image_pager);
		utils = new Utils(getApplicationContext());
		
		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, utils.getFilePaths());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
	}
}
package com.remembook.photo;

import java.util.ArrayList;

import com.remembook.R;
import com.remembook.camera.CameraActivity;
import com.remembook.photo.PhotoGalleryAdapter;
import com.remembook.photo.PhotoGalleryReference;
import com.remembook.photo.PhotoGalleryUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class PhotoGalleryActivity extends Activity implements OnClickListener {

	private PhotoGalleryUtils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private PhotoGalleryAdapter adapter;
	private GridView gridView;
	private int columnWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_grid);

		Button button_camera = (Button) findViewById(R.id.image_button_camera);
		button_camera.setOnClickListener(this);

		Intent intent = getIntent();
		String path = intent.getExtras().get("path").toString(); // mainActivity에서 title명 받아옴

		gridView = (GridView) findViewById(R.id.image_grid_view);
		utils = new PhotoGalleryUtils(this);
		InitilizeGridLayout();
		imagePaths = utils.getFilePaths(path);
		adapter = new PhotoGalleryAdapter(PhotoGalleryActivity.this, imagePaths,
				columnWidth, path);
		gridView.setAdapter(adapter);

	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				PhotoGalleryReference.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((PhotoGalleryReference.NUM_OF_COLUMNS + 1) * padding)) / PhotoGalleryReference.NUM_OF_COLUMNS);

		gridView.setNumColumns(PhotoGalleryReference.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_button_camera:

			Intent intent = getIntent();
			String path = intent.getExtras().get("path").toString(); // mainActivity에서 title명 받아옴

			Intent intent_camera = new Intent(this, CameraActivity.class);
			intent_camera.putExtra("path", path); // title명 camera에게 보냄
			startActivity(intent_camera);
			break;
		}
	}

}

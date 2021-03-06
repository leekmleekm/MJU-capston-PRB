package com.remembook.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.remembook.photo.PhotoFullScreenActivity;
import com.remembook.sqlite.AppSqliteHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoGalleryAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;
	private String titlePath;

	public PhotoGalleryAdapter(Activity activity, ArrayList<String> filePaths,
			int imageWidth, String path) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.imageWidth = imageWidth;
		this.titlePath = path;
	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		imageView = new ImageView(_activity);

		/*
		 * if (convertView == null) {
		 * 
		 * } else imageView = (ImageView) convertView;
		 */

		// 그리드 사각형 안에 보여지는 이미지 조정
		Bitmap image = decodeFile(_filePaths.get(position), imageWidth, imageWidth);

		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
		imageView.setImageBitmap(image);

		imageView.setOnClickListener(new OnImageClickListener(position));

		return imageView;
	}

	class OnImageClickListener implements OnClickListener {
		int _postion;

		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			Intent i = new Intent(_activity, PhotoFullScreenActivity.class);
			i.putExtra("position", _postion);
			i.putExtra("path", titlePath);
			_activity.startActivity(i);
		}
	}

	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {
			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 2;

			while (o.outWidth / scale / 4 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 4 >= REQUIRED_HIGHT)
				scale *= 4;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

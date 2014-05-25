package com.remembook.main;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.remembook.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainCursor extends CursorAdapter {
	
	public MainCursor(Context context, Cursor c) {
		super(context, c);
	}

	 @Override
	 public void bindView(View view, Context context, Cursor cursor) {
		 final ImageView image = (ImageView)view.findViewById(R.id.main_image);
		 final TextView title = (TextView)view.findViewById(R.id.main_title);
		 final TextView author = (TextView)view.findViewById(R.id.main_author);
		 final TextView publisher = (TextView)view.findViewById(R.id.main_publisher);
		
		 title.setText(cursor.getString(cursor.getColumnIndex("_title")));
		 author.setText(cursor.getString(cursor.getColumnIndex("_author")));
		 publisher.setText(cursor.getString(cursor.getColumnIndex("_publisher")));   
		 
		 URL imageUrl;
		 try{
			imageUrl = new URL(cursor.getString(cursor.getColumnIndex("_image")));
			HttpURLConnection con = (HttpURLConnection)imageUrl.openConnection();
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream(), 10240);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bis.close();
			image.setImageBitmap(bm);
		 }
		 catch (MalformedURLException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
		 catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	 }

	 @Override
	 public View newView(Context context, Cursor cursor, ViewGroup parent) {  
		 LayoutInflater inflater = LayoutInflater.from(context);
		 View v = inflater.inflate(R.layout.main_list, parent, false);
		 return v;
	 }
}

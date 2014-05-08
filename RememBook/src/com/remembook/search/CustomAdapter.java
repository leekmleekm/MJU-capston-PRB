package com.remembook.search;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.remembook.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Object> {
	
	private ArrayList<BookData> data;
	View v;
	ImageView image;
	
	public CustomAdapter(Context context, int textViewResourceId, ArrayList items){
		super(context, textViewResourceId, items);
		this.data = items;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
			
		v = convertView;
			
		if( v == null){
			LayoutInflater vi=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.search_listitem, null);
		}
			
		BookData item = data.get(position);
		
		if(item != null){
			image = (ImageView)v.findViewById(R.id.search_image);
				
			TextView v_title = (TextView)v.findViewById(R.id.search_title);
			TextView v_author = (TextView)v.findViewById(R.id.search_author);
			TextView v_publisher = (TextView)v.findViewById(R.id.search_publisher);
				
			v_title.setText(item.title);
			v_author.setText("저자 : "+item.author);
			v_publisher.setText("출판사 : "+item.publisher);
				
			// 이미지 불러오기
			URL imageUrl;
			try{
				imageUrl = new URL(item.image);
				HttpURLConnection con = (HttpURLConnection)imageUrl.openConnection();
				BufferedInputStream bis = new BufferedInputStream(con.getInputStream(), 10240);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				image.setImageBitmap(bm);
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return v;
	}
}
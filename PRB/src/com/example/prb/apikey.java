package com.example.prb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class apikey extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apikey);
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("권태욱 api 페이지");
	}
}


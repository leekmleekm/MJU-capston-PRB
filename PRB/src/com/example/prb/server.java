package com.example.prb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class server extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("양준호 서버 페이지");
	}
}


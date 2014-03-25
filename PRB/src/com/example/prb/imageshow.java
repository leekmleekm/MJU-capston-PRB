package com.example.prb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class imageshow extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshow);
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("이경민 이미지 표현 페이지");
	}
}


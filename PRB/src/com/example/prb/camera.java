package com.example.prb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class camera extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("김경욱 카메라 페이지");
	}
}

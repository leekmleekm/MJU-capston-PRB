package com.example.prb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class list extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		TextView tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("장윤권 목록 페이지");
	}
}


package com.remembook.main;

import com.remembook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingSplash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_splash);

		Handler hd = new Handler();
		hd.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(LoadingSplash.this, MainActivity.class);                  
	            startActivity(i); // 메인 책 목록 실행
				finish(); // 3 초후 이미지를 닫아버림
			}
		}, 3000);

	}

}

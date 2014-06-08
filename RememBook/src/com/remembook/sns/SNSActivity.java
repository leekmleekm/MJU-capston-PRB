package com.remembook.sns;

import com.remembook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SNSActivity extends Activity implements OnClickListener{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sns_main);
		
		Button button_facebook = (Button) findViewById(R.id.sns_button_facebook);
		Button button_twitter = (Button) findViewById(R.id.sns_button_twitter);
		Button button_dropbox = (Button) findViewById(R.id.sns_button_dropbox);
		button_facebook.setOnClickListener(this);
		button_twitter.setOnClickListener(this);
		button_dropbox.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sns_button_facebook:
			Intent intent_facebook = new Intent(this, FacebookCon.class);
			startActivity(intent_facebook);
			break;
		
		case R.id.sns_button_twitter:
			//Intent intent_twitter = new Intent(this, FacebookCon.class);
			//startActivity(intent_twitter);
			break;
			
		case R.id.sns_button_dropbox:
			//Intent intent_dropbox = new Intent(this, FacebookCon.class);
			//startActivity(intent_dropbox);
			break;
		}
	}

}

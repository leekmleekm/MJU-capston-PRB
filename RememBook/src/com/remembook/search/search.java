package com.remembook.search;

import com.remembook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class search extends Activity {

	private EditText et;
	private Button bt;
	private String info;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search_main);
	    // TODO Auto-generated method stub
	    et=(EditText)findViewById(R.id.search_edit_text);
	    bt=(Button)findViewById(R.id.search_button);
	    
	    bt.setOnClickListener(new OnClickListener(){
	    	
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				info=et.getText().toString();
				
				Intent intent=new Intent(search.this,NaverBook.class);
				Bundle data=new Bundle();
				data.putString("key", info);
				intent.putExtras(data);
				startActivity(intent);
			}
	    });
	}

}

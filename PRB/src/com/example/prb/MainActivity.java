package com.example.prb;

import com.example.prb.ImageShow.GridViewActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button bt1 = (Button)findViewById(R.id.button1);
        Button bt2 = (Button)findViewById(R.id.button2);
        Button bt3 = (Button)findViewById(R.id.button3);
        Button bt4 = (Button)findViewById(R.id.button4);
        Button bt5 = (Button)findViewById(R.id.button5);
        
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
    }
    
    public void onClick(View v){
    	switch (v.getId()){
    	case R.id.button1:
    		Intent inten1 = new Intent(this, camera.class);
    		startActivity(inten1);
    		break;
    	case R.id.button2:
    		Intent inten2 = new Intent(this, GridViewActivity.class);
    		startActivity(inten2);
    		break;
    	case R.id.button3:
    		Intent inten3 = new Intent(this, apikey.class);
    		startActivity(inten3);
    		break;
    	case R.id.button4:
    		Intent inten4 = new Intent(this, list.class);
    		startActivity(inten4);
    		break;
    	case R.id.button5:
    		Intent inten5 = new Intent(this, server.class);
    		startActivity(inten5);
    		break;
    	}
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

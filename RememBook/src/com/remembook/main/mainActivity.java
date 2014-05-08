package com.remembook.main;

import com.remembook.R;
import com.remembook.database.MySQLiteHandler;
import com.remembook.search.*;
import com.remembook.imageview.*;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class mainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	
	final int MENU_delete = Menu.FIRST;    
	ListView list;
	Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button_search = (Button)findViewById(R.id.main_button_search);
		MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());

		list = (ListView)findViewById(R.id.main_list);
		cursor = handler.select();
		
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);;
		button_search.setOnClickListener(this);
		
		if(cursor.getCount() > 0 ){
			dbCursor dbAdapter = new dbCursor(this, cursor);
			list.setAdapter(dbAdapter);
	     }
		else if(cursor.getCount() == 0){
			Toast.makeText(getApplicationContext(), "책이 없습니다.\n책을 추가하세요.", Toast.LENGTH_LONG).show();
		}
	}
	
    public void onClick(View v){
    	switch (v.getId()){
    		case R.id.main_button_search:
    			Intent intent = new Intent(this, search.class);
    			startActivity(intent);
    			break;
    	}
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
    	Intent intent = new Intent(this, GridViewActivity.class);
		startActivity(intent);
    }
    
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
    	MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());

    	handler.delete(cursor.getString(cursor.getColumnIndex("_title")));
    	handler.close();
    	Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
    	cursor.requery(); 
    	
    	return true;
    }

    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		// TODO Auto-generated method stub
   		super.onCreateOptionsMenu(menu);
   		menu.add(0, MENU_delete, 0, "모 넣지?");
   		return true;
   	}
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_delete :
				
		}
		return super.onMenuItemSelected(featureId, item);
	}

}

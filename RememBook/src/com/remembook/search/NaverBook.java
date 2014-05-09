package com.remembook.search;

import java.io.File;
import java.util.ArrayList;

import com.remembook.R;
import com.remembook.database.MySQLiteHandler;
import com.remembook.main.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NaverBook extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */
	
	final int MENU_back = Menu.FIRST;    
	final int MENU_forward = Menu.FIRST + 1;
	private String key1="6fd12764eaf503ad4b16ecd1c5561bad";
	private ProgressDialog dialog;
	
	private ListView myList;
	private NaverParser Nparser;
	private CustomAdapter adapter;
	
	ArrayList<BookData> data;
	private String info;
	
	final int count = 11;
	int start = 1;

	
	private final Handler handler = new Handler(){
    	public void handleMessage(final Message msg){
    		dialog.dismiss();
    		
    		adapter = new CustomAdapter(NaverBook.this,R.layout.search_listitem,data);
    		myList.setAdapter(adapter);
    	}
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_booklist);
        
        Intent intent = getIntent();
	    Bundle myBundle = intent.getExtras();
		info = myBundle.getString("key");
		
		myList = (ListView)findViewById(R.id.search_listview);
		Nparser = new NaverParser(key1);
		
		getNewList(info, count, start);
		
		myList.setOnItemClickListener(this);
		
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_back, 0, "이전 페이지");
		menu.add(0, MENU_forward, 1, "다음 페이지");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_back :
				if(start <= 11){
					Toast.makeText(this, "첫 번째 페이지입니다.", Toast.LENGTH_SHORT).show();
				}
				else{
					start -= 11;
					getNewList(info, count, start);
				}
				break;

			case MENU_forward :
				start += 11;
				getNewList(info, count, start);
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
    public void getNewList(final String inform,final int count,final int starts){
		dialog = ProgressDialog.show(this, "알림", "목록 불러오는 중..",true,false);
		new Thread(){
			public void run(){
				data = Nparser.getBookData(inform, count,starts);
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
    	MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());
    	BookData item = data.get(position);
    	//테이블 추가
    	handler.insert(item.image, item.title, item.author, item.publisher);
    	handler.close();
    	//폴더 생성
    	File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/RememBook/" + item.title);
    	
    	boolean success = true;
    	if (!folder.exists()) {
    	    success = folder.mkdir();
    	}
    	if (success) {
    	    // Do something on success
    		alert("등록", item.title);
    		
    	} else {
    	    // Do something else on failure
    		Toast.makeText(this, "등록 실패", Toast.LENGTH_SHORT).show();
    	}
    }
    
    private void alert(String title, String message){
		// 체인형으로 메소드를 사용한다.
		new AlertDialog.Builder(this)/*.setTitle(title)*/.setMessage(message + " 등록하였습니다.")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					dialog.dismiss(); // 클릭되면 다이얼로그를 종료한다.
					Intent intent = new Intent(NaverBook.this, mainActivity.class);
		            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(intent);
				}
			}).show();
	}
}
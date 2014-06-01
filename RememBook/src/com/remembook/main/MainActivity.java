package com.remembook.main;

import java.io.File;
import java.util.ArrayList;

import com.remembook.R;
import com.remembook.photo.*;
import com.remembook.search.*;
import com.remembook.sqlite.AppSqliteHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	ListView list;
	Cursor cursor;
	ArrayList<SearchBookData> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 안드로이드 3.x 버전부터 ui thread에서 네트워크 처리가 안되어 sub thread를 이용해야하는데
		 * 책 이미지를 불러올 때 네트워크가 사용되기 때문에 이를 sub thread로 돌려야함
		 * StrictMode는 임시병편
		 */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Button button_search = (Button) findViewById(R.id.main_button_search);
		AppSqliteHandler handler = AppSqliteHandler.open(getApplicationContext());

		list = (ListView) findViewById(R.id.main_list);
		cursor = handler.select();

		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		;
		button_search.setOnClickListener(this);

		if (cursor.getCount() > 0) { // db에 책이 하나 이상 있을 경우 전부 읽어옴
			MainCursor dbAdapter = new MainCursor(this, cursor);
			list.setAdapter(dbAdapter);
		} else if (cursor.getCount() == 0) { // db에 책이 하나도 없을 경우
			Toast.makeText(getApplicationContext(), "책이 없습니다.\n책을 추가하세요.",
					Toast.LENGTH_LONG).show();
			
			File folder = new File(Environment
					.getExternalStorageDirectory() + "/DCIM/RememBook/");
			if (!folder.exists()) {
				folder.mkdir();
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_button_search: // 책 검색 버튼을 누르면 searchMain 인텐트로 이동
			Intent intent_search = new Intent(this, SearchActivity.class);
			startActivity(intent_search);
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(this, PhotoGalleryActivity.class);
		intent.putExtra("path",
				cursor.getString(cursor.getColumnIndex("_title"))); // 책 title명
																	// gridviewactivity에게
																	// 보냄
		startActivity(intent);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		alert("삭제", cursor.getString(cursor.getColumnIndex("_title")), position);
		return true;
	}

	private void alert(String title, String message, final int position) {
		// 체인형으로 메소드를 사용한다.
		new AlertDialog.Builder(this)
				/* .setTitle(title) */.setMessage(message + " 삭제하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppSqliteHandler handler = AppSqliteHandler
								.open(getApplicationContext());

						handler.delete(cursor.getString(cursor
								.getColumnIndex("_title"))); // db 데이터 삭제
						handler.close(); // db 사용 끝을 명시
						// 폴더 삭제
						File folder = new File(Environment
								.getExternalStorageDirectory()
								+ "/DCIM/RememBook/"
								+ cursor.getString(cursor
										.getColumnIndex("_title")));

						File[] childFileList = folder.listFiles();
						for (File childFile : childFileList) {
							if (childFile.isDirectory()) {
								// 하위 디렉토리 루프
							} else {
								childFile.delete(); // 하위 파일삭제
							}
						}
						folder.delete(); // root 삭제

						Toast.makeText(getApplicationContext(), "삭제하였습니다.",
								Toast.LENGTH_SHORT).show();
						cursor.requery(); // 리스트 갱신

						dialog.dismiss(); // 클릭되면 다이얼로그를 종료한다.

					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// dialog.dismiss(); // 클릭되면 다이얼로그를 종료한다.
					}
				}).show();
	}

}

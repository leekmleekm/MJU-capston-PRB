package com.remembook.search;

import java.io.File;
import java.util.ArrayList;

import com.remembook.R;
import com.remembook.main.*;
import com.remembook.sqlite.AppSqliteHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity implements OnItemClickListener,
		OnClickListener, OnEditorActionListener, OnScrollListener {

	private String key1 = "6fd12764eaf503ad4b16ecd1c5561bad";
	private ProgressDialog dialog;

	private ListView getList, selectList;
	private SearchParser Nparser;
	private SearchAdapter adapter;

	ArrayList<SearchBookData> data;
	private String info;

	int count = 11;
	int start = 1;

	private EditText editext;
	private Button button;
	private boolean mLockListView;

	Handler handler = new Handler(new IncomingHandelerCallback());

	class IncomingHandelerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			dialog.dismiss();

			adapter = new SearchAdapter(SearchActivity.this,
					R.layout.search_listitem, data);
			getList.setAdapter(adapter);

			return true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);

		mLockListView = false;
		

		selectList = (ListView) findViewById(R.id.search_listview);
		editext = (EditText) findViewById(R.id.search_edit_text);
		button = (Button) findViewById(R.id.search_button);

		button.setOnClickListener(this);
		selectList.setOnItemClickListener(this);
		editext.setOnEditorActionListener(this);
		selectList.setOnScrollListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.search_button_pre: if(start <= 11){ Toast.makeText(this,
		 * "첫 번째 페이지입니다.", Toast.LENGTH_SHORT).show(); } else{ start -= 11;
		 * getNewList(info, count, start); } break;
		 * 
		 * case R.id.search_button_for: start += 11; getNewList(info, count,
		 * start); break;
		 */

		case R.id.search_button:

			makeNewList();
			break;
		}
	}

	// 사용자 검색에 따른 결과 리스트를 만드는 신호를 보내는 메소드
	private void makeNewList() {
		// TODO Auto-generated method stub
		info = editext.getText().toString();
		getList = (ListView) findViewById(R.id.search_listview);
		Nparser = new SearchParser(key1);

		getNewList(info, count, start);

		// 검색 후 키보드가 그 자리에 남아있지 않게
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editext.getWindowToken(), 0);
	}

	// 사용자 값에 따른 결과를 가져옴
	private void getNewList(final String inform, final int count,
			final int starts) {
		dialog = ProgressDialog.show(this, "알림", "목록 불러오는 중..", true, false);
		new Thread() {
			public void run() {
				data = Nparser.getBookData(inform, count, starts);
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	// 검색어 입력란에서 입력 후 키보드란의 search 버튼을 눌렀을 경우, search 버튼은 search_main.xml에서 정의
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH
				|| actionId == EditorInfo.IME_ACTION_DONE) {
			// TODO Auto-generated method stub
			makeNewList();
		}
		return false;
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		SearchBookData item = data.get(position);
		alert("등록", item.title, position);
	}

	private void alert(String title, String message, final int position) {
		// 체인형으로 메소드를 사용한다.
		new AlertDialog.Builder(this)
				/* .setTitle(title) */.setMessage(message + " 등록하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppSqliteHandler handler = AppSqliteHandler
								.open(getApplicationContext());
						SearchBookData item = data.get(position);
						// 테이블 추가
						handler.insert(item.image, item.title, item.author,
								item.publisher, item.isbn);
						handler.close();
						// 폴더 생성
						File folder = new File(Environment
								.getExternalStorageDirectory()
								+ "/DCIM/RememBook/" + item.title);

						boolean success = true;
						if (!folder.exists()) {
							success = folder.mkdir();
						}
						if (success) {
							// 폴더 생성이 성공했을 경우
						} else {
							// 폴더 생성이 실패했을 경우
							// Toast.makeText(this,
							// "책 등록 실패".LENGTH_SHORT).show();
						}

						dialog.dismiss(); // 클릭되면 다이얼로그를 종료한다.

						Intent intent = new Intent(SearchActivity.this,
								MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss(); // 클릭되면 다이얼로그를 종료한다.
					}
				}).show();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		int count = totalItemCount - visibleItemCount;

		if (firstVisibleItem >= count && totalItemCount != 0
				&& mLockListView == false) {

			addItem();

		}

	}

	private void addItem() {
		// TODO Auto-generated method stub
		mLockListView = true;

		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				count += 11;

				getNewList(editext.getText().toString(), count, start);

				mLockListView = false;

			}

		};
		Handler handler = new Handler();
		handler.postDelayed(run, 500);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
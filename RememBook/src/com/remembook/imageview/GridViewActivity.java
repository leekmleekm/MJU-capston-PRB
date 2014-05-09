package com.remembook.imageview;

import java.util.ArrayList;

import com.remembook.R;
import com.remembook.imageview.AppConstant;
import com.remembook.imageview.GridViewImageAdapter;
import com.remembook.imageview.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.GridView;
import android.widget.Toast;
 
public class GridViewActivity extends Activity {
 
    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mainActivity에서 title명 받아옴
        Intent intent = getIntent();
        String path = intent.getExtras().get("path").toString();
        
        setContentView(R.layout.imageview_grid);
        gridView = (GridView) findViewById(R.id.image_grid_view);
        utils = new Utils(this);
        InitilizeGridLayout();
        imagePaths = utils.getFilePaths(path);
        adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths, columnWidth, path);
        gridView.setAdapter(adapter);

    }
 
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.GRID_PADDING, r.getDisplayMetrics());
        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);
 
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
}

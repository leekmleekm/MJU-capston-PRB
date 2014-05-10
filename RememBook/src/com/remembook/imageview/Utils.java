package com.remembook.imageview;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
 

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;
 
public class Utils { 
    private Context _context;
    
    public Utils(Context context) {
        this._context = context;
    }
    // sd카드로 부터 읽어 드리는 경로
    public ArrayList<String> getFilePaths(String path) {
        ArrayList<String> filePaths = new ArrayList<String>();
        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator
        		+ "DCIM/RememBook/" + path/*AppConstant.PHOTO_ALBUM*/);
        // 디렉토리 체크
        if (directory.isDirectory()) {
            File[] listFiles = directory.listFiles();
 
            if (listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    String filePath = listFiles[i].getAbsolutePath();
 
                    if (IsSupportedFile(filePath)) {
                        filePaths.add(filePath);
                    }
                }
            }        
            else
                Toast.makeText(_context, path/*AppConstant.PHOTO_ALBUM*/ + " 폴더에\n사진이 없습니다.", Toast.LENGTH_LONG).show();
        }  
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("에러");
            alert.setMessage( "지정된 폴더는 " + path/*AppConstant.PHOTO_ALBUM*/ + " 입니다. 현재 SD카드에 디렉토리가 존재하지 않습니다.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
 
        return filePaths;
    }
 
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());
 
        if (AppConstant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }
 
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try {
            display.getSize(point);
        }
        catch (java.lang.NoSuchMethodError ignore) { // ������ ����̽�
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
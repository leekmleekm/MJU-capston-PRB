package com.example.prb.ImageShow;

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
    // 구조체
    public Utils(Context context) {
        this._context = context;
    }
    // sd카드로 부터 읽어 드리는 경로
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();
        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTO_ALBUM);
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
                Toast.makeText(_context, AppConstant.PHOTO_ALBUM + " 폴더안에 이미지 파일이 하나도 없습니다.", Toast.LENGTH_LONG).show();
        } 
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("에러");
            alert.setMessage( "지정된 디렉토리는 " + AppConstant.PHOTO_ALBUM + " 입니다. 현재 SD카드에 디렉토리가 존재하지 않습니다.");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
 
        return filePaths;
    }
 
    // 지원하는 파일 확장자 확인
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());
 
        if (AppConstant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }
 
    // 기기마다 폭이 다르기 때문에 기기 폭 계산 (이 계산을 하지 않으면 기기마다 한줄에 출력되는 목록 수가 달라짐)
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try {
            display.getSize(point);
        }
        catch (java.lang.NoSuchMethodError ignore) { // 오래된 디바이스
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
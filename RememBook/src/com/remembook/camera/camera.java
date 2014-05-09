package com.remembook.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.remembook.R;
import com.remembook.main.mainActivity;
import com.remembook.search.NaverBook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class camera extends Activity {
	
	//액티비티 요청 코드
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;

	//디렉토리 이름
	private static final String IMAGE_DIRECTORY_NAME = "RememBook";
	private Uri fileUri; //저장하는 장소 uri

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(camera.this, mainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		captureImage();
	}

  
 	//Capturing Camera Image will launch camera app request image capture
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE); 
	}

   /*
    * Here we store the file url as it will be null after returning from camera
    * app
    */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
   }

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				// previewCapturedImage();
			}
			else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),"사용자가 사진 촬영을 취소하였습니다.", Toast.LENGTH_SHORT)
				.show();
			}
			else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),"사진 촬영을 실패하였습니다.", Toast.LENGTH_SHORT)
                .show();
			}
		} 
   }
   
   /**
    * ------------ Helper Methods ---------------------- 
    * */

   /*
    * Creating file uri to store image/video
    */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

   /*
    * returning image / video
    */
	private static File getOutputMediaFile(int type) {

		// External SD card location
		File mediaStorageDir = new File( Environment
                  .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, ">>>>>>>>>>>>>>>>폴더 생성에 실패" + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
      
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
		}
		else {
			return null;
		}

		return mediaFile;
   }
}
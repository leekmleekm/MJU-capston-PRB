package com.remembook.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.remembook.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraActivity extends Activity implements OnClickListener,
		SurfaceHolder.Callback, Camera.PictureCallback {

	private static final String IMAGE_DIRECTORY_NAME = "RememBook";
	private Uri fileUri; // 저장하는 장소
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

	SurfaceView cameraView;
	SurfaceHolder surfaceHolder;
	Camera camera;
	Button takePictureBtn;
	ImageButton flashBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.camera_main);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		cameraView = (SurfaceView) this.findViewById(R.id.CameraView);
		surfaceHolder = cameraView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		takePictureBtn = (Button) findViewById(R.id.camera_takePictureBtn);
		takePictureBtn.setOnClickListener(this);

		flashBtn = (ImageButton) findViewById(R.id.camera_flashBtn);
		flashBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flashOnOff();
			}
		});

		cameraView.setFocusable(true);
		cameraView.setFocusableInTouchMode(true);
		cameraView.setClickable(true);

		cameraView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						// TODO Auto-generated method stub
					}
				});
			}
		});
	}

	public void onClick(View v) {
		camera.takePicture(null, null, this);
	}

	public void onPictureTaken(byte[] data, Camera camera) {
		fileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		Intent intent = new Intent();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		Matrix matrix = new Matrix();
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		matrix.setRotate(90);
		Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

		try {
			OutputStream imageFileOS = getContentResolver().openOutputStream(fileUri);
			adjustedBitmap.compress(CompressFormat.JPEG, 100, imageFileOS);
			imageFileOS.close();

		} catch (FileNotFoundException e) {
			Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
			t.show();
		} catch (IOException e) {
			Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
			t.show();
		}

		ShowSaveDailog();
		camera.startPreview();
	}

	// 플래시 on/off
	public void flashOnOff() {
		Camera.Parameters cameraParameter = camera.getParameters();
		String state = cameraParameter.getFlashMode();
		if (state.equals("off")) {
			cameraParameter.setFlashMode("torch");
			camera.setParameters(cameraParameter);
			flashBtn.setSelected(true); // 플래시버튼 on
		} else if (state.equals("torch")) {
			cameraParameter.setFlashMode("off");
			camera.setParameters(cameraParameter);
			flashBtn.setSelected(false); // 플래시버튼 off
		}
	}

	// 사진찍기 성공시 메세지
	private void ShowSaveDailog() {
		Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		camera.startPreview();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters = camera.getParameters();

			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				parameters.set("orientation", "portrait");
				camera.setDisplayOrientation(90);
			}

			//End Effects for Android Version 2.0 and higher
			List<String> colorEffects = parameters.getSupportedColorEffects();
			Iterator<String> cei = colorEffects.iterator();
			while (cei.hasNext()) {
				String currentEffect = cei.next();
				if (currentEffect.equals(Camera.Parameters.EFFECT_NONE)) {
					parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
					break;
				}
			}

			camera.setParameters(parameters);
		} catch (IOException exception) {
			camera.release();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

	
	
	/**        보조 메소드             */

	//저장할 이미지 파일 생성
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	// 이미지 반환
	public File getOutputMediaFile(int type) {

		// GridViewActivity에서 title명 받아옴
		Intent intent = getIntent();
		String title_path = intent.getExtras().get("path").toString();

		// 외장 sd카드 경로
		File mediaStorageDir = new File( Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), IMAGE_DIRECTORY_NAME);

		// 디렉토리가 존재하지 않을 경우 생성
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, ">>>>>>>>>>폴더 생성에 실패" + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// 이밎 파일명 생성
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;

		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ title_path + File.separator + "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}
}

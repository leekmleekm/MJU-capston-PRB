package com.example.db_test_7.sqlite.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.db_test_7.sqlite.model.Album;
import com.example.db_test_7.sqlite.model.Photo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	// Logcat album
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Table Names
	private static final String TABLE_PHOTO = "photos";
	private static final String TABLE_ALBUM = "albums";
	private static final String TABLE_PHOTO_ALBUM = "photo_albums";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// Photos Table - column names
	private static final String KEY_PHOTO = "photo";
	private static final String KEY_STATUS = "status";

	// ALBUMS Table - column names
	private static final String KEY_ALBUM_NAME = "album_name";

	// PHOTO_ALBUMS Table - column names
	private static final String KEY_PHOTO_ID = "photo_id";
	private static final String KEY_ALBUM_ID = "album_id";

	// Table Create Statements
	// Photo table create statement

	private static final String CREATE_TABLE_PHOTO = "CREATE TABLE " + TABLE_PHOTO + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," 
			+ KEY_PHOTO + " TEXT," 
			+ KEY_STATUS + " INTEGER," 
			+ KEY_CREATED_AT + " DATETIME" 
			+ ")";

	// Photo table create statement
	private static final String CREATE_TABLE_ALBUM = "CREATE TABLE " + TABLE_ALBUM + "(" 
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_ALBUM_NAME + " TEXT,"			
			+ KEY_CREATED_AT + " DATETIME" 
			+ ")";

	// photo_album table create statement
	private static final String CREATE_TABLE_PHOTO_ALBUM = "CREATE TABLE " + TABLE_PHOTO_ALBUM + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PHOTO_ID + " INTEGER," 
			+ KEY_ALBUM_ID + " INTEGER,"
			+ KEY_CREATED_AT + " DATETIME" 
			+ ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_PHOTO);
		db.execSQL(CREATE_TABLE_ALBUM);
		db.execSQL(CREATE_TABLE_PHOTO_ALBUM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_ALBUM);

		// create new tables
		onCreate(db);
	}

	// ------------------------ "photos" table methods ----------------//

	/*
	 * Creating a photo
	 */
	public long createPhoto(Photo photo, long[] album_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHOTO, photo.getNote());
		values.put(KEY_STATUS, photo.getStatus());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long photo_id = db.insert(TABLE_PHOTO, null, values);

		// insert album_ids
		for (long album_id : album_ids) {
			createPhotoAlbum(photo_id, album_id);
		}

		return photo_id;
	}

	/*
	 * get single photo
	 */
	public Photo getPhoto(long photo_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_PHOTO + " WHERE "
				+ KEY_ID + " = " + photo_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Photo ph = new Photo();
		ph.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		ph.setNote((c.getString(c.getColumnIndex(KEY_PHOTO))));
		ph.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

		return ph;
	}

	/**
	 * getting all photos
	 * */
	public List<Photo> getAllPhotos() {
		List<Photo> photos = new ArrayList<Photo>();
		String selectQuery = "SELECT  * FROM " + TABLE_PHOTO;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Photo ph = new Photo();
				ph.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				ph.setNote((c.getString(c.getColumnIndex(KEY_PHOTO))));
				ph.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to photo list
				photos.add(ph);
			} while (c.moveToNext());
		}

		return photos;
	}

	/**
	 * getting all photos under single album
	 * */
	public List<Photo> getAllPhotoByAlbum(String album_name) {
		List<Photo> photos = new ArrayList<Photo>();

		String selectQuery = "SELECT  * FROM " + TABLE_PHOTO + " ph, "
				+ TABLE_ALBUM + " ab, " + TABLE_PHOTO_ALBUM + " photoalbum WHERE ab."
				+ KEY_ALBUM_NAME + " = '" + album_name + "'" + " AND ab." + KEY_ID
				+ " = " + "photoalbum." + KEY_ALBUM_ID + " AND ph." + KEY_ID + " = "
				+ "photoalbum." + KEY_PHOTO_ID;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Photo ph = new Photo();
				ph.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				ph.setNote((c.getString(c.getColumnIndex(KEY_PHOTO))));
				ph.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to photo list
				photos.add(ph);
			} while (c.moveToNext());
		}

		return photos;
	}

	/*
	 * getting photo count
	 */
	public int getPhotoCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PHOTO;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a photo
	 */
	public int updatePhoto(Photo photo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHOTO, photo.getNote());
		values.put(KEY_STATUS, photo.getStatus());

		// updating row
		return db.update(TABLE_PHOTO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(photo.getId()) });
	}

	/*
	 * Deleting a photo
	 */
	public void deletePhoto(long photo_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PHOTO, KEY_ID + " = ?",
				new String[] { String.valueOf(photo_id) });
	}

	// ------------------------ "albums" table methods ----------------//

	/*
	 * Creating album
	 */
	public long createAlbum(Album album) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ALBUM_NAME, album.getAlbumName());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long album_id = db.insert(TABLE_ALBUM, null, values);

		return album_id;
	}

	/**
	 * getting all albums
	 * */
	public List<Album> getAllAlbums() {
		List<Album> albums = new ArrayList<Album>();
		String selectQuery = "SELECT  * FROM " + TABLE_ALBUM;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Album t = new Album();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				t.setAlbumName(c.getString(c.getColumnIndex(KEY_ALBUM_NAME)));

				// adding to albums list
				albums.add(t);
			} while (c.moveToNext());
		}
		return albums;
	}

	/*
	 * Updating a album
	 */
	public int updateAlbum(Album album) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ALBUM_NAME, album.getAlbumName());

		// updating row
		return db.update(TABLE_ALBUM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(album.getId()) });
	}

	/*
	 * Deleting a album
	 */
	public void deleteAlbum(Album album, boolean should_delete_all_album_photos) {
		SQLiteDatabase db = this.getWritableDatabase();

		// before deleting album
		// check if photos under this album should also be deleted
		if (should_delete_all_album_photos) {
			// get all photos under this album
			List<Photo> allAlbumPhotos = getAllPhotoByAlbum(album.getAlbumName());

			// delete all photos
			for (Photo photo : allAlbumPhotos) {
				// delete photo
				deletePhoto(photo.getId());
			}
		}

		// now delete the album
		db.delete(TABLE_ALBUM, KEY_ID + " = ?",
				new String[] { String.valueOf(album.getId()) });
	}

	// ------------------------ "photo_albums" table methods ----------------//

	/*
	 * Creating photo_album
	 */
	public long createPhotoAlbum(long photo_id, long album_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHOTO_ID, photo_id);
		values.put(KEY_ALBUM_ID, album_id);
		values.put(KEY_CREATED_AT, getDateTime());

		long id = db.insert(TABLE_PHOTO_ALBUM, null, values);

		return id;
	}

	/*
	 * Updating a photo album
	 */
	public int updatePhotoAlbum(long id, long album_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ALBUM_ID, album_id);

		// updating row
		return db.update(TABLE_PHOTO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	/*
	 * Deleting a photo album
	 */
	public void deletePhotoAlbum(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PHOTO, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}

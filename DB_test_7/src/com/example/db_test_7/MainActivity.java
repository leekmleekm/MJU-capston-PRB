package com.example.db_test_7;

import java.util.List;

import com.example.db_test_7.sqlite.helper.DatabaseHelper;
import com.example.db_test_7.sqlite.model.Album;
import com.example.db_test_7.sqlite.model.Photo;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	// Database Helper
	DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		db = new DatabaseHelper(getApplicationContext());

		// Creating albums
		Album album1 = new Album("Shopping");
		Album album2 = new Album("Important");
		Album album3 = new Album("Watchlist");
		Album album4 = new Album("Hive");

		// Inserting albums in db
		long album1_id = db.createAlbum(album1);
		long album2_id = db.createAlbum(album2);
		long album3_id = db.createAlbum(album3);
		long album4_id = db.createAlbum(album4);

		Log.d("Album Count", "Album Count: " + db.getAllAlbums().size());

		// Creating Photos
		Photo photo1 = new Photo("iPhone 5S", 0);
		Photo photo2 = new Photo("Galaxy Note II", 0);
		Photo photo3 = new Photo("Whiteboard", 0);

		Photo photo4 = new Photo("Riddick", 0);
		Photo photo5 = new Photo("Prisoners", 0);
		Photo photo6 = new Photo("The Croods", 0);
		Photo photo7 = new Photo("Insidious: Chapter 2", 0);

		Photo photo8 = new Photo("Don't forget to call MOM", 0);
		Photo photo9 = new Photo("Collect money from John", 0);

		Photo photo10 = new Photo("Post new Article", 0);
		Photo photo11 = new Photo("Take database backup", 0);

		// Inserting photos in db
		// Inserting photos under "Shopping" Album
		long photo1_id = db.createPhoto(photo1, new long[] { album1_id });
		long photo2_id = db.createPhoto(photo2, new long[] { album1_id });
		long photo3_id = db.createPhoto(photo3, new long[] { album1_id });

		// Inserting photos under "Watchlist" Album
		long photo4_id = db.createPhoto(photo4, new long[] { album3_id });
		long photo5_id = db.createPhoto(photo5, new long[] { album3_id });
		long photo6_id = db.createPhoto(photo6, new long[] { album3_id });
		long photo7_id = db.createPhoto(photo7, new long[] { album3_id });

		// Inserting photos under "Important" Album
		long photo8_id = db.createPhoto(photo8, new long[] { album2_id });
		long photo9_id = db.createPhoto(photo9, new long[] { album2_id });

		// Inserting photos under "hive" Album
		long photo10_id = db.createPhoto(photo10, new long[] { album4_id });
		long photo11_id = db.createPhoto(photo11, new long[] { album4_id });

		Log.e("Photo Count", "Photo count: " + db.getPhotoCount());

		// "Post new Article" - assigning this under "Important" Album
		// Now this will have - "hive" and "Important" Albums
		db.createPhotoAlbum(photo10_id, album2_id);

		// Getting all album names
		Log.d("Get Albums", "Getting All Albums");

		List<Album> allAlbums = db.getAllAlbums();
		for (Album album : allAlbums) {
			Log.d("Album Name", album.getAlbumName());
		}

		// Getting all Photos
		Log.d("Get Photos", "Getting All Photos");

		List<Photo> allPhotos = db.getAllPhotos();
		for (Photo photo : allPhotos) {
			Log.d("Photo", photo.getNote());
		}

		// Getting photos under "Watchlist" album name
		Log.d("Photo", "Get photos under single Album name");

		List<Photo> albumsWatchList = db.getAllPhotoByAlbum(album3.getAlbumName());
		for (Photo photo : albumsWatchList) {
			Log.d("Photo Watchlist", photo.getNote());
		}

		// Deleting a Photo
		Log.d("Delete Photo", "Deleting a Photo");
		Log.d("Album Count", "Album Count Before Deleting: " + db.getPhotoCount());

		db.deletePhoto(photo8_id);

		Log.d("Album Count", "Album Count After Deleting: " + db.getPhotoCount());

		// Deleting all Photos under "Shopping" album
		Log.d("Album Count",
				"Album Count Before Deleting 'Shopping' Photos: "
						+ db.getPhotoCount());

		db.deleteAlbum(album1, true);

		Log.d("Album Count",
				"Album Count After Deleting 'Shopping' Photos: "
						+ db.getPhotoCount());

		// Updating album name
		album3.setAlbumName("Movies to watch");
		db.updateAlbum(album3);

		// Don't forget to close database connection
		db.closeDB();

        //	-------------
        
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}

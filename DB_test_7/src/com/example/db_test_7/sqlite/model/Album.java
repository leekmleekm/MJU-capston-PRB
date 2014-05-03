package com.example.db_test_7.sqlite.model;

public class Album {
	
	int id;
	String album_name;

	// constructors
	public Album() {

	}

	public Album(String album_name) {
		this.album_name = album_name;
	}

	public Album(int id, String album_name) {
		this.id = id;
		this.album_name = album_name;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setAlbumName(String album_name) {
		this.album_name = album_name;
	}

	// getter
	public int getId() {
		return this.id;
	}

	public String getAlbumName() {
		return this.album_name;
	}

}

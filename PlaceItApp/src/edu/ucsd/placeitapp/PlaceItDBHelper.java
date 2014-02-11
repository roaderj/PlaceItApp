package edu.ucsd.placeitapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaceItDBHelper extends SQLiteOpenHelper {
	private static PlaceItDBHelper instance;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "PlaceIt.db";

	private PlaceItDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	public static PlaceItDBHelper getInstance() {
		return instance;
	}

	public static void setInstance(Context context) {
		instance = new PlaceItDBHelper(context);
	}
}

package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class PlaceItDBHelper extends SQLiteOpenHelper {
	private static PlaceItDBHelper instance;

	public static final String PLACEIT_TABLE_NAME = "placeits";
	public static final String PLACEIT_ID_COLUMN_NAME = "id";
	public static final String PLACEIT_TITLE_COLUMN_NAME = "title";
	public static final String PLACEIT_DESCRIPTION_COLUMN_NAME = "description";
	public static final String PLACEIT_LATITUDE_COLUMN_NAME = "latitude";
	public static final String PLACEIT_LONGITUDE_COLUMN_NAME = "longitude";
	public static final String PLACEIT_START_TIME_COLUMN_NAME = "startTime";
	public static final String PLACEIT_IS_ENABLED_COLUMN_NAME = "isEnabled";
	public static final String PLACEIT_IS_RECURRING_COLUMN_NAME = "isRecurring";
	public static final String PLACEIT_RECURRING_INTERVAL_WEEKS_COLUMN_NAME = "recurringIntervalWeeks";

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "PlaceIt.db";

	private PlaceItDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + PLACEIT_TABLE_NAME + " ("
				+ PLACEIT_ID_COLUMN_NAME + " INTEGER PRIMARY KEY,"
				+ PLACEIT_TITLE_COLUMN_NAME + " TEXT,"
				+ PLACEIT_DESCRIPTION_COLUMN_NAME + " TEXT,"
				+ PLACEIT_LATITUDE_COLUMN_NAME + " REAL,"
				+ PLACEIT_LONGITUDE_COLUMN_NAME + " REAL,"
				+ PLACEIT_START_TIME_COLUMN_NAME + " INTEGER,"
				+ PLACEIT_IS_ENABLED_COLUMN_NAME + " BOOLEAN,"
				+ PLACEIT_IS_RECURRING_COLUMN_NAME + " BOOLEAN,"
				+ PLACEIT_RECURRING_INTERVAL_WEEKS_COLUMN_NAME + " INTEGER"
				+ ");");
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

	private PlaceIt createPlaceItFromRow(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(PLACEIT_ID_COLUMN_NAME));

		String title = cursor.getString(cursor
				.getColumnIndex(PLACEIT_TITLE_COLUMN_NAME));
		String description = cursor.getString(cursor
				.getColumnIndex(PLACEIT_DESCRIPTION_COLUMN_NAME));

		double latitude = cursor.getDouble(cursor
				.getColumnIndex(PLACEIT_LATITUDE_COLUMN_NAME));
		double longitude = cursor.getDouble(cursor
				.getColumnIndex(PLACEIT_LONGITUDE_COLUMN_NAME));
		Location location = new Location("network"); // I have no idea why I
														// need to provide a
														// string
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		Timestamp startTime = new Timestamp(cursor.getLong(cursor
				.getColumnIndex(PLACEIT_START_TIME_COLUMN_NAME)));
		boolean isEnabled = cursor.getInt(cursor
				.getColumnIndex(PLACEIT_IS_ENABLED_COLUMN_NAME)) == 1;
		boolean isRecurring = cursor.getInt(cursor
				.getColumnIndex(PLACEIT_IS_RECURRING_COLUMN_NAME)) == 1;
		int recurringIntervalWeeks = cursor.getInt(cursor
				.getColumnIndex(PLACEIT_RECURRING_INTERVAL_WEEKS_COLUMN_NAME));

		return new PlaceIt(id, title, description, location, startTime,
				isRecurring, recurringIntervalWeeks, isEnabled);
	}

	public boolean exists(int id) {
		return find(id) != null;
	}

	public PlaceIt find(int id) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(PLACEIT_TABLE_NAME, null,
				PLACEIT_ID_COLUMN_NAME + "=" + Integer.toString(id), null,
				null, null, null);

		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return createPlaceItFromRow(cursor);
		}
	}

	public List<PlaceIt> all() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(PLACEIT_TABLE_NAME, null, null, null, null,
				null, null);

		// there's probably a more Android way of doing this but the docs for
		// Cursor sucks
		List<PlaceIt> placeIts = new ArrayList<PlaceIt>();
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			placeIts.add(createPlaceItFromRow(cursor));
			cursor.moveToNext();
		}

		return placeIts;
	}

	public boolean delete(PlaceIt placeIt) {
		SQLiteDatabase db = getWritableDatabase();
		int rowsAffected = db.delete(PLACEIT_TABLE_NAME, 
				PLACEIT_ID_COLUMN_NAME + "=?", 
				new String[] { Integer.toString(placeIt.getId()) });
		return (rowsAffected == 1);
	}

	public int save(PlaceIt placeIt) {
		ContentValues placeItValues = new ContentValues();
		placeItValues.put(PLACEIT_TITLE_COLUMN_NAME, placeIt.getTitle());
		placeItValues.put(PLACEIT_DESCRIPTION_COLUMN_NAME,
				placeIt.getDescription());
		placeItValues.put(PLACEIT_LATITUDE_COLUMN_NAME, placeIt.getLocation()
				.getLatitude());
		placeItValues.put(PLACEIT_LONGITUDE_COLUMN_NAME, placeIt.getLocation()
				.getLongitude());
		placeItValues.put(PLACEIT_START_TIME_COLUMN_NAME, placeIt
				.getStartTime().getTime());
		placeItValues.put(PLACEIT_IS_ENABLED_COLUMN_NAME, placeIt.isEnabled());
		placeItValues.put(PLACEIT_IS_RECURRING_COLUMN_NAME,
				placeIt.isRecurring());
		placeItValues.put(PLACEIT_RECURRING_INTERVAL_WEEKS_COLUMN_NAME,
				placeIt.getRecurringIntervalWeeks());

		SQLiteDatabase db = getWritableDatabase();
		int id = placeIt.getId();
		if (exists(id) == true) {
			db = getWritableDatabase();
			int rowsAffected = db.update(PLACEIT_TABLE_NAME, placeItValues,
					PLACEIT_ID_COLUMN_NAME + "=?",
					new String[] { Integer.toString(id) });
			if (rowsAffected != 1) {
				id /= 0;
			}
		} else {
			id = (int) db.insert(PLACEIT_TABLE_NAME, null, placeItValues);
		}

		return id;
	}

	public static PlaceItDBHelper getInstance() {
		return instance;
	}

	public static void setInstance(Context context) {
		instance = new PlaceItDBHelper(context);
	}
}

package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class EntityDb extends SQLiteOpenHelper {
	private static EntityDb instance;

	public static final String ENTITY_TABLE_NAME = "entity";
	public static final String ENTITY_ID_COLUMN_NAME = "id";
	public static final String ENTITY_KEY_COLUMN_NAME = "_key";
	public static final String ENTITY_VALUE_COLUMN_NAME = "value";

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Entity.db";

	private EntityDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ENTITY_TABLE_NAME + " ("
				+ ENTITY_ID_COLUMN_NAME + " INTEGER PRIMARY KEY,"
				+ ENTITY_KEY_COLUMN_NAME + " TEXT," + ENTITY_VALUE_COLUMN_NAME
				+ " TEXT" + ");");
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

	public boolean exists(int id) {
		return find(id) != null;
	}

	private Cursor find(int id) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(ENTITY_TABLE_NAME, null, ENTITY_ID_COLUMN_NAME
				+ "=" + Integer.toString(id), null, null, null, null);

		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			return cursor;
		}
	}

	public String getValue(int id) {
		Cursor cursor = find(id);
		if (cursor == null) {
			return null;
		} else {
			return cursor.getString(cursor
					.getColumnIndex(ENTITY_VALUE_COLUMN_NAME));
		}
	}

	public List<String> all(String key) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(ENTITY_TABLE_NAME, null, null, null, null,
				null, null);

		// there's probably a more Android way of doing this but the docs for
		// Cursor sucks
		List<String> values = new ArrayList<String>();
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); ++i) {
			if (key != null) {
				String currentKey = cursor.getString(cursor
						.getColumnIndex(ENTITY_KEY_COLUMN_NAME));
				if (currentKey != key) {
					continue;
				}
			}
			values.add(cursor.getString(cursor
					.getColumnIndex(ENTITY_VALUE_COLUMN_NAME)));
			cursor.moveToNext();
		}

		return values;
	}

	public List<String> all() {
		return all(null);
	}

	public boolean delete(int id) {
		SQLiteDatabase db = getWritableDatabase();
		int rowsAffected = db.delete(ENTITY_TABLE_NAME, ENTITY_ID_COLUMN_NAME
				+ "=?", new String[] { Integer.toString(id) });
		return (rowsAffected == 1);
	}

	public int save(String key, String value) {
		return save(-1, key, value);
	}

	public int save(int id, String key, String value) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ENTITY_KEY_COLUMN_NAME, key);
		contentValues.put(ENTITY_VALUE_COLUMN_NAME, value);

		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = find(id);
		if (cursor == null) {
			id = (int) db.insert(ENTITY_TABLE_NAME, null, contentValues);
		} else {
			int rowsAffected = db.update(ENTITY_TABLE_NAME, contentValues,
					ENTITY_ID_COLUMN_NAME + "=?",
					new String[] { Integer.toString(id) });
			if (rowsAffected != 1) {
				rowsAffected /= 0;
			}
		}

		return id;
	}

	public static EntityDb getInstance() {
		return instance;
	}

	public static void setInstance(Context context) {
		instance = new EntityDb(context);
	}
}

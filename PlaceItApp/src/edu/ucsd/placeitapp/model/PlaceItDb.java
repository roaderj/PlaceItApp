package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class PlaceItDb {
	private static PlaceItDb instance;

	private PlaceItDb() {
	}
	
	private PlaceIt deserialize(String json) {
		// hack city; make it check for PlaceIt key instead in the future possibly
		PlaceIt placeIt = null;
		Gson gson = new Gson();
		if (json.contains("mElapsedRealtimeNanos")) {
			placeIt = gson.fromJson(json, LocationPlaceIt.class);
		} else {
			placeIt = gson.fromJson(json, CategoricalPlaceIt.class);
		}
		
		return placeIt;
	}
	
	private String serialize(PlaceIt placeIt) {
		Gson gson = new Gson();
		return gson.toJson(placeIt);
	}

	public boolean exists(int id) {
		return find(id) != null;
	}

	public PlaceIt find(int id) {
		String value = EntityDb.getInstance().getValue(id);
		if (value == null) {
			return null;
		} else {
			return deserialize(value);
		}
	}
	
	public List<PlaceIt> all(String key) {
		List<PlaceIt> placeIts = new ArrayList<PlaceIt>();
		List<String> placeItValues = EntityDb.getInstance().all(key);
		for (String placeItValue : placeItValues) {
			placeIts.add(deserialize(placeItValue));
		}
		
		return placeIts;
	}

	public List<PlaceIt> all() {
		return all(null);
	}

	public boolean delete(PlaceIt placeIt) {
		return EntityDb.getInstance().delete(placeIt.getId());
	}

	public int save(PlaceIt placeIt) {
		String key = placeIt.getKey();
		String value = serialize(placeIt);
		return EntityDb.getInstance().save(placeIt.getId(), key, value);
	}

	public static PlaceItDb getInstance() {
		if (instance == null) {
			instance = new PlaceItDb();
		}
		
		return instance;
	}
}

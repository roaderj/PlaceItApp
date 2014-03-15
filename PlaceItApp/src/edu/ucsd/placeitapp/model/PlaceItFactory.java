package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import android.location.Location;
import android.os.Bundle;

public class PlaceItFactory {
	public static final String PLACEIT_ID = "id";
	public static final String PLACEIT_TITLE = "title";
	public static final String PLACEIT_DESCRIPTION = "description";
	public static final String PLACEIT_LATITUDE = "latitude";
	public static final String PLACEIT_LONGITUDE = "longitude";
	public static final String PLACEIT_START_TIME = "startTime";
	public static final String PLACEIT_IS_ENABLED = "isEnabled";
	public static final String PLACEIT_IS_RECURRING = "isRecurring";
	public static final String PLACEIT_RECURRING_INTERVAL = "recurringInterval";
	public static final String PLACEIT_TAGS = "tags";

	public PlaceIt create(PlaceIts p, Bundle data) {
		int pID = data.getInt(PLACEIT_ID, -1);
		String title = data.getString(PLACEIT_TITLE);
		String description = data.getString(PLACEIT_DESCRIPTION);
		int recurringInterval = data.getInt(PLACEIT_RECURRING_INTERVAL, -1);
		boolean isRecurring = data.getBoolean(PLACEIT_IS_RECURRING, false);
		boolean isEnabled = data.getBoolean(PLACEIT_IS_ENABLED, false);
		Timestamp startTime = new Timestamp(data.getLong(PLACEIT_START_TIME,
				new Date().getTime()));

		switch (p) {
		case LOCATION:
		case LOCATION_RECURRING:
			Location loc = new Location("network");
			loc.setLatitude(data.getDouble(PLACEIT_LATITUDE, -1));
			loc.setLongitude(data.getDouble(PLACEIT_LONGITUDE, -1));
			return new LocationPlaceIt(pID, title, description, loc, startTime,
					isRecurring, recurringInterval, isEnabled);
		case CATEGORICAL:
		case CATEGORICAL_RECURRING:
			String[] tags = data.getStringArray(PLACEIT_TAGS);
			return new CategoricalPlaceIt(pID, title, description, Arrays.asList(tags),
					startTime, isRecurring, recurringInterval, isEnabled);
		default:
			return null;
		}

	}
}

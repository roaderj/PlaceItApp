package edu.ucsd.placeitapp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import android.location.Location;

public class PlaceIt {
	private int id;
	private String title;
	private String description;
	private Location location;
	private Timestamp startTime;
	private boolean isRecurring;
	private int recurringIntervalWeeks;
	private boolean isEnabled;

	public PlaceIt(String title, String description, Location location,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		this.id = -1;
		this.title = title;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.isEnabled = isEnabled;
		this.isRecurring = isRecurring;
		this.recurringIntervalWeeks = recurringIntervalWeeks;
	}

	public PlaceIt(String title, String description, Location location,
			Timestamp startTime, boolean isRecurring, int recurringIntervalWeeks) {
		this(title, description, location, startTime, isRecurring,
				recurringIntervalWeeks, false);
	}

	public PlaceIt(String title, String description, Location location) {
		this(title, description, location, new Timestamp(new Date().getTime()),
				false, -1);
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStarttime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public boolean isRecurring() {
		return this.isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public void setRecurringIntervalWeeks(int recurringIntervalWeeks) {
		this.recurringIntervalWeeks = recurringIntervalWeeks;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void save() {
		
		// TODO
	}

	public void delete() {
		// TODO
	}

	public static PlaceIt find(int id) {
		// TODO
		return null;
	}

	public static List<PlaceIt> all() {
		// TODO
		return null;
	}
}

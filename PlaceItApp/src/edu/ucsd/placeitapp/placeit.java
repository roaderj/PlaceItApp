package edu.ucsd.placeitapp;

import java.sql.Date;
import java.util.List;

import android.R.string;
import android.location.Location;

public class placeit {
	private int id;
	private string title;
	private string description;
	private Location location;
	private Date starttime;
	private boolean isRecurring;
	private boolean isEnabled;
	private int recurringIntervalWeek;
	
	public placeit(string tit, string des, Location loc) {
		title = tit;
		description = des;
		location = loc;	
		//TODO
	}
	public int getId() {
		return id;
	}
	public void setId(int x) {
		id = x;
	}
	public string getTitle() {
		return title;
	}
	public void setTitle(string tit) {
		title = tit;
	}
	public string getDescription() {
		return description;
	}
	public void setDescription(string des) {
		description = des;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location loc) {
		location = loc;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date st) {
		starttime = st;
	}
	public boolean isRecurring() {
		return isRecurring;
	}
	public void setRecurring(int riw) {
		isRecurring = true;
		recurringIntervalWeek = riw;
	}
	public boolean isEnabled() {
		//TODO
		return isEnabled;
	}
	public void save() {
		//TODO
	}
	public static placeit find(int id) {
		//TODO
		return null;
	}
	public static List<placeit> all() {
		//TODO
		return null;
	}
	public void repost() {
		//TODO
	}
	public void discard() {
		//TODO
	}
	public void setEnabled() {
		//TODO
	}
}

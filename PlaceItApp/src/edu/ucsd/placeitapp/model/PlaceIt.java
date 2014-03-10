package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import edu.ucsd.placeitapp.AlarmReceiver;
import edu.ucsd.placeitapp.MainActivity;
import edu.ucsd.placeitapp.ProximityAlertReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class PlaceIt {
	private int id;
	private String title;
	private String description;
	private Location location;
	private Timestamp startTime;
	private boolean isRecurring;
	private int recurringIntervalWeeks; // should be minutes for testing
	private boolean isEnabled;

	public final static long REPOST_WAIT_TIME = 10000; // 10 seconds
	public final static long RECURRING_INTERVAL = 60000; // 1 minutes
	public final static long RADIUS = 804; // 804m -> .5 miles

	public PlaceIt(int id, String title, String description, Location location,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.isEnabled = isEnabled;
		this.isRecurring = isRecurring;
		this.recurringIntervalWeeks = recurringIntervalWeeks;

	}

	public PlaceIt(String title, String description, Location location,
			boolean isRecurring, int recurringIntervalWeeks) {

		this(-1, title, description, location, new Timestamp(
				new Date().getTime()), isRecurring, recurringIntervalWeeks,
				false);
	}

	public PlaceIt(String title, String description, Location location) {
		this(title, description, location, false, -1);
	}

	public void setId(int id) {
		this.id = id;
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

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public boolean isRecurring() {
		return this.isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;

	}

	public int getRecurringIntervalWeeks() {
		return this.recurringIntervalWeeks;
	}

	public void setRecurringIntervalWeeks(int recurringIntervalWeeks) {
		this.recurringIntervalWeeks = recurringIntervalWeeks;

	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		PlaceItList.save(this);
	}

	public boolean isActive() {
		Date currTime = new Date();
		if (this.isEnabled())
			if (currTime.getTime() >= this.getStartTime().getTime())
				return true;
		return false;
	}

	public void repost(Context context) {
		this.setStartTime(new Timestamp(new Date().getTime() + REPOST_WAIT_TIME));
		
		PlaceItList.save(this); 
	}

	public void discard(Context context) {
		if (this.isRecurring)
			this.recur(context);
		else
			PlaceItList.delete(this);
	}

	public void setAlarm(Context context, boolean enable) {
		int pID = this.getId();

		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, AlarmReceiver.class).putExtra(
				MainActivity.PLACEIT_ID, pID);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, pID,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (enable)
			aManager.set(AlarmManager.RTC, this.getStartTime().getTime(), pIntent);
		else 
			aManager.cancel(pIntent);
	}

	public void trackLocation(Context context, boolean enable) {
		int pID = this.getId();
		Intent locationIntent = new Intent(context,
				ProximityAlertReceiver.class).putExtra(MainActivity.PLACEIT_ID,
				pID);

		LocationManager locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		PendingIntent pIntent = PendingIntent.getBroadcast(context, pID,
				locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (enable) {
			locManager.addProximityAlert(this.getLocation().getLatitude(), this
					.getLocation().getLongitude(), PlaceIt.RADIUS, -1, pIntent); 
		} else  {
			locManager.removeProximityAlert(pIntent);
		}

	}

	public void recur(Context context) {
		if (this.isRecurring()) {
			long newTime = this.getStartTime().getTime()
					+ (this.getRecurringIntervalWeeks() * PlaceIt.RECURRING_INTERVAL);

			this.setStartTime(new Timestamp(newTime));
			this.setAlarm(context, true); 
			
			PlaceItList.save(this); 
		}
	}
	
	public static PlaceIt find(int id) {
		return PlaceItDBHelper.getInstance().find(id);
	}
	
	public static List<PlaceIt> all() {
		return PlaceItDBHelper.getInstance().all();
	}

}

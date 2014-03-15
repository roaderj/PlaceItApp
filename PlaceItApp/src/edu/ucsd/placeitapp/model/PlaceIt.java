package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import edu.ucsd.placeitapp.AlarmReceiver;
import edu.ucsd.placeitapp.MainActivity;

public abstract class PlaceIt {
	private int id;
	private String title;
	private String description;
	private Timestamp startTime;
	private boolean isRecurring;
	private int recurringIntervalWeeks; // should be minutes for testing
	private boolean isEnabled;
	protected String key;

	public final static long REPOST_WAIT_TIME = 10000; // 10 seconds
	public final static long RECURRING_INTERVAL = 60000; // 1 minutes
	public final static long RADIUS = 804; // 804m -> .5 miles

	public PlaceIt(int id, String title, String description,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.isEnabled = isEnabled;
		this.isRecurring = isRecurring;
		this.recurringIntervalWeeks = recurringIntervalWeeks;
	}

	public PlaceIt(String title, String description,
			boolean isRecurring, int recurringIntervalWeeks) {

		this(-1, title, description, new Timestamp(
				new Date().getTime()), isRecurring, recurringIntervalWeeks,
				false);
	}

	public PlaceIt(String title, String description) {
		this(title, description, false, -1);
	}
	
	public PlaceIt() {
		this("", "");
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


	public void recur(Context context) {
		if (this.isRecurring()) {
			long newTime = this.getStartTime().getTime()
					+ (this.getRecurringIntervalWeeks() * PlaceIt.RECURRING_INTERVAL);

			this.setStartTime(new Timestamp(newTime));
			this.setAlarm(context, true); 
			
			PlaceItList.save(this); 
		}
	}
	
	public String getKey() {
		return this.key;
	}
	
	public static PlaceIt find(int id) {
		return PlaceItDb.getInstance().find(id);
	}
	
	public static List<PlaceIt> all() {
		return PlaceItDb.getInstance().all();
	}
	
	public String toString() {
		String text = this.getDescription();
		if (this.isRecurring())
			text += "\nRecurring every " + this.getRecurringIntervalWeeks() + " mins";
		else 
			text += "\nNot Recurring";
		return (text);
	}

	public abstract void trackLocation(Context context, boolean enable); 

}

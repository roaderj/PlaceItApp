package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.Date;

import edu.ucsd.placeitapp.MainActivity;
import edu.ucsd.placeitapp.ProximityAlertReceiver;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

/* Needs PlaceItDBHelper to be fully functional */ 
public class LocationPlaceIt extends PlaceIt {

	private double latitude;
	private double longitude; 
	public static final String KEY = "LocationPlaceIt";

	public LocationPlaceIt(int id, String title, String description, Location location,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		
		//super needs to be changed in the future
		super(id, title, description, startTime, isRecurring, recurringIntervalWeeks, isEnabled);
		latitude = location.getLatitude(); 
		longitude = location.getLongitude(); 
		this.key = KEY;
	}
	
	public LocationPlaceIt() {
		this(0, "", "", new Location(""), null, false, 0, false);
	}

	public Location getLocation() {
		Location location = new Location(""); 
		location.setLatitude(latitude); 
		location.setLongitude(longitude); 
		return location; 
	}

	public void setLocation(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude(); 

	}

	@Override
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
		} else {
			locManager.removeProximityAlert(pIntent);
		}

	}

	public String toString() {
		String fullDescription = new String(super.toString() 
				+ "\n\n\nLocation: " 
				+ "\n" + String.format("%.4f", getLocation().getLatitude()) 
				+ ", " + String.format("%.4f", getLocation().getLongitude()));
		return fullDescription;
	}

}

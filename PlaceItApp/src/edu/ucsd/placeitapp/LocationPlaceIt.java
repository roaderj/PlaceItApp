package edu.ucsd.placeitapp;

import java.sql.Timestamp;
import java.util.Date;

import android.content.Context;
import android.location.Location;

public class LocationPlaceIt { //extends PlaceIt {
	
	private Location location;

	public LocationPlaceIt(String title, String description, Location location,
			boolean isRecurring, int recurringIntervalWeeks) {
		//TODO
	}

	public LocationPlaceIt(String title, String description, Location location) {
		this(title, description, location, false, -1);
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;

	}
	
	//@Override
	public void trackLocation(Context context, boolean enable) {
		
		/* Directly copied from PlaceIt */
		
//		int pID = this.getId();
//		Intent locationIntent = new Intent(context,
//				ProximityAlertReceiver.class).putExtra(MainActivity.PLACEIT_ID,
//				pID);
//
//		LocationManager locManager = (LocationManager) context
//				.getSystemService(Context.LOCATION_SERVICE);
//
//		PendingIntent pIntent = PendingIntent.getBroadcast(context, pID,
//				locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		if (enable) {
//			locManager.addProximityAlert(this.getLocation().getLatitude(), this
//					.getLocation().getLongitude(), PlaceIt.RADIUS, -1, pIntent);
//		} else {
//			locManager.removeProximityAlert(pIntent);
//		}

		
	}


	public String toString() {
		//TODO
		return super.toString(); 
	}

}

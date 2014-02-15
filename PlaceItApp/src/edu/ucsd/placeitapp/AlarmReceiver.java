package edu.ucsd.placeitapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/* 
 * For issuing AlarmManager, insert this snippet of code (needs to be modularized somewhere): 
 * 
 * AlarmManager aManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
 * Intent alarmIntent = new Intent(context, AlarmReceiver.class).putExtra(MainActivity.PLACEIT_ID, placeIt.getId()); 
 * aManager.set(AlarmManager.RTC, placeIt.getStartTime(), PendingIntent.getBroadcast(context, requestCode, locationIntent, flags)); 
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		Bundle extras = intent.getExtras();

		int pID = extras.getInt(MainActivity.PLACEIT_ID, -1);
		if (pID == -1)
			throw new RuntimeException("AlarmReceiver used incorrectly.");

		Log.w("AlarmReceiver", new String("Placeit #" + pID + " is now enabled."));

		PlaceIt placeIt = PlaceIt.find(pID);
		// TODO: Modify placeIt accordingly

		Intent locationIntent = new Intent(context, ProximityAlertReceiver.class)
			.putExtra(MainActivity.PLACEIT_ID, pID);

		LocationManager locManager = (LocationManager) 
				context.getSystemService(Context.LOCATION_SERVICE);
		
		int requestCode = 0; // Not sure what this is, used by PendingIntent
		int radius = 1000; // 1000m
		locManager.addProximityAlert(placeIt.getLocation().getLatitude(),
				placeIt.getLocation().getLongitude(), 
				radius, // Defined in AlarmReceiver, should probably be defined in PlaceIt. 
				-1, // No expiration time
				PendingIntent.getBroadcast(context, requestCode,
						locationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		
	}

}

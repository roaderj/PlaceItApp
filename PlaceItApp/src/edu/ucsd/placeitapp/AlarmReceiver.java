package edu.ucsd.placeitapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int requestCode = 0; //Not sure what this is
		Bundle extras = intent.getExtras(); 
		
		int pID = extras.getInt(MainActivity.PLACEIT_ID, -1); 
		if (pID == -1) {
			throw new RuntimeException("AlarmReceiver used incorrectly."); 
		}
		Log.w("AlarmReceiver", new String("Placeit #" + pID + " is now enabled."));
		
		
		PlaceIt placeit = PlaceIt.find(pID); 
		// TODO: Modify placeit startTime 
		
		LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		Intent locationIntent = new Intent(context, ProximityAlertReceiver.class); 
		locationIntent.putExtra(MainActivity.PLACEIT_ID, pID); 
		PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, locationIntent, 0); 
		//locManager.addProximityAlert(latitude, longitude, radius, expiration, intent); 
	}
	
}

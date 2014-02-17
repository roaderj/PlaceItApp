package edu.ucsd.placeitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProximityAlertReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle extras = intent.getExtras(); 
		int pID = extras.getInt(MainActivity.PLACEIT_ID, -1); 
		if (pID == -1) {
			throw new RuntimeException("ProximityAlertReceiver used incorrectly."); 
		}
		Log.w("ProximityAlertReceiver", new String("Placeit #" + pID + " was triggered.")); 

		PlaceIt p = PlaceItList.find(pID);
		p.setEnabled(false); 
		if (p.isRecurring()) {
			p.recur();
			p.setAlarm(context, true);
		}
		PlaceItList.save(p); 
		PlaceItNotification.notify(context, pID); 
	}
}

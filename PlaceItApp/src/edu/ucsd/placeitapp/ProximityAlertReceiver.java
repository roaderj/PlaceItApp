package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
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
		p.recur(context); // Will not recur if isRecurring is set to false
		
		PlaceItList.save(p); 
		PlaceItNotification.notify(context, pID); 
	}
}

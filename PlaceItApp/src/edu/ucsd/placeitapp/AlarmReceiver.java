package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/* 
 * This class what should happen if an alarm goes off. 
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	/*
	 * Upon receiving an alarm, determines which PlaceItset the alarm, 
	 * enables that placeit, and enables location tracking for that placeit. 
	 */
	public void onReceive(Context context, Intent intent) {


		Bundle extras = intent.getExtras();

		int pID = extras.getInt(MainActivity.PLACEIT_ID, -1);
		if (pID == -1)
			throw new RuntimeException("AlarmReceiver used incorrectly.");

		PlaceIt placeIt = PlaceItList.find(pID);
		placeIt.setEnabled(true);
		PlaceItList.save(placeIt); 
		
		Toast.makeText(context, "Place-it " + pID + " is now enabled.", 
				Toast.LENGTH_SHORT).show();

		placeIt.trackLocation(context, true);

		
	}

}

package edu.ucsd.placeitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/* 
 * For issuing an alarm, see setAlarm in PlaceIt. 
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
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

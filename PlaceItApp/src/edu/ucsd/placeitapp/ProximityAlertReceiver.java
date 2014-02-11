package edu.ucsd.placeitapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

		PlaceIt placeit = PlaceIt.find(pID); 
		PlaceItNotification.notify(context, placeit.getTitle(), placeit.getDescription(), pID); 
		/* 
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(context); 
		builder.setSmallIcon(R.drawable.ic_launcher); 
		builder.setContentTitle("Hello World"); 
		builder.setContentText("Some text"); 
		
		NotificationManager manager = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE); 
		
		*/ 
		
		// Notification notification = new Notification(); 
		// manager.notify(id, notification); 
	}
}

package edu.ucsd.placeitapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int id = 0; // Placeholder 
		
		Bundle extras = intent.getExtras(); 
		// Need to check that this was called from 
		// notification manager via extras.
		
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(context); 
		builder.setSmallIcon(R.drawable.ic_launcher); 
		builder.setContentTitle("Hello World"); 
		builder.setContentText("Some text"); 
		
		NotificationManager manager = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE); 
		
		manager.notify(id, builder.build()); 
	}
}

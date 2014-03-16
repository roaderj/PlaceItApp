package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.CategoricalPlaceIt;
import edu.ucsd.placeitapp.model.LocationPlaceIt;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Class for showing/canceling notifications, and handling managing the actions
 * specified by the notification including
 * 
 * 1) clicking the notification 2) clicking the repost action 3) clicking the
 * discard action 4) clearing the notification
 */
public class PlaceItNotification extends BroadcastReceiver {
	/** The unique identifier for this type of notification. */
	public static final String NOTIFICATION_TAG = "edu.ucsd.PlaceItApp.PlaceIt";

	/** Identifier for notification action extras. */
	private static final String BUTTON_TAG = "edu.ucsd.PlaceItApp.Button";

	/**
	 * Shows the notification with repost and discard options. Multiple
	 * Place-its listed as separate notifications.
	 */
	public static void notify(final Context context, final int pID) {
		final Resources res = context.getResources();

		PlaceIt placeIt = PlaceItList.find(pID);
		final String title = placeIt.getTitle();
		//address for Categorical, Location cordinate for regular
		String address = placeIt.getDescription();
		if(placeIt.getClass() == CategoricalPlaceIt.class){
			address = ((CategoricalPlaceIt) placeIt).getAddress();
		}
		else{
			address = "" +  ((LocationPlaceIt) placeIt).getLocation().getLatitude() + ((LocationPlaceIt) placeIt).getLocation().getLongitude();
		}

		final String fullTitle = res.getString(
				R.string.place_it_notification_title_template, title);
		final String fullAddress = res.getString(
				R.string.place_it_notification_text_template, address);

		final NotificationCompat.Builder notification = new NotificationCompat.Builder(
				context);

		// Set appropriate defaults for the notification light, sound,
		// and vibration.
		notification.setDefaults(Notification.DEFAULT_ALL);

		// required values
		notification.setSmallIcon(R.drawable.ic_launcher);
		notification.setContentTitle(fullTitle);
		notification.setContentText(fullAddress);

		// Set preview information for this notification.
		notification.setTicker(title);

		// On click action go to DescriptionActivity
		Intent descriptionIntent = new Intent(context,
				DescriptionActivity.class).putExtra(MainActivity.PLACEIT_ID,
				pID);
		notification.setContentIntent(PendingIntent.getActivity(context, pID,
				descriptionIntent, PendingIntent.FLAG_UPDATE_CURRENT));

		// Repost action
		Intent repostIntent = new Intent(context, PlaceItNotification.class)
				.putExtra(BUTTON_TAG, R.string.notification_repost).putExtra(
						MainActivity.PLACEIT_ID, pID);
		notification.addAction(R.drawable.ic_stat_repost, res
				.getString(R.string.notification_repost), PendingIntent
				.getBroadcast(context, 0, repostIntent,
						PendingIntent.FLAG_UPDATE_CURRENT));

		// Discard action
		Intent discardIntent = new Intent(context, PlaceItNotification.class)
				.putExtra(BUTTON_TAG, R.string.notification_discard).putExtra(
						MainActivity.PLACEIT_ID, pID);

		notification.addAction(R.drawable.ic_stat_discard, res
				.getString(R.string.notification_discard), PendingIntent
				.getBroadcast(context, 0, discardIntent,
						PendingIntent.FLAG_UPDATE_CURRENT));

		// Automatically dismiss the notification when it is touched.
		notification.setAutoCancel(true);

		notify(context, notification.build(), pID);
	}

	private static void notify(final Context context,
			final Notification notification, int pID) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(NOTIFICATION_TAG, pID, notification);

	}

	public static void cancel(final Context context, int pID) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_TAG, pID);

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		int pID;
		if ((pID = bundle.getInt(MainActivity.PLACEIT_ID, -1)) == -1)
			throw new RuntimeException(
					"Notification not used correctly: pID not found");

		PlaceItNotification.cancel(context, pID);

		PlaceIt placeIt = PlaceItList.find(pID);
		if (bundle.get(BUTTON_TAG).equals(R.string.notification_discard)) {
			placeIt.discard(context);

			Toast.makeText(context, "Place-it discarded.", Toast.LENGTH_LONG)
					.show();
		} else if (bundle.get(BUTTON_TAG).equals(R.string.notification_repost)) {
			placeIt.repost(context);

			Toast.makeText(context, "Place-it reposted.", Toast.LENGTH_LONG)
					.show();
		} else
			throw new RuntimeException("Notification not used correctly");
	}

}
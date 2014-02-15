package edu.ucsd.placeitapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
	private static final String NOTIFICATION_TAG = "edu.ucsd.PlaceItApp.PlaceIt";

	/** Identifier for notification action extras. */
	private static final String BUTTON_TAG = "edu.ucsd.PlaceItApp.Button";

	/**
	 * Shows the notification with repost and discard options. Multiple
	 * Place-its listed as separate notifications.
	 */
	public static void notify(final Context context, final int pID) {
		final Resources res = context.getResources();

		PlaceIt placeIt = PlaceIt.find(pID);
		final String title = placeIt.getTitle();
		final String description = placeIt.getDescription();

		final String fullTitle = res.getString(
				R.string.place_it_notification_title_template, title);
		final String fullDescription = res.getString(
				R.string.place_it_notification_text_template, description);

		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)

				// Set appropriate defaults for the notification light, sound,
				// and vibration.
				.setDefaults(Notification.DEFAULT_ALL)

				// required values
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(fullTitle).setContentText(fullDescription)

				// Set preview information for this notification.
				.setTicker(title)

				// On click action go to DescriptionActivity
				.setContentIntent(
						PendingIntent.getActivity(context, 0, new Intent(
								context, DescriptionActivity.class).putExtra(
								MainActivity.PLACEIT_ID, pID),
								PendingIntent.FLAG_UPDATE_CURRENT))

				// Repost action
				.addAction(
						R.drawable.ic_stat_repost,
						res.getString(R.string.notification_repost),
						PendingIntent
								.getBroadcast(
										context,
										0,
										new Intent(context,
												PlaceItNotification.class)
												.putExtra(
														BUTTON_TAG,
														R.string.notification_repost)
												.putExtra(
														MainActivity.PLACEIT_ID,
														pID),
										PendingIntent.FLAG_UPDATE_CURRENT))

				// Discard action
				.addAction(
						R.drawable.ic_stat_discard,
						res.getString(R.string.notification_discard),
						PendingIntent
								.getBroadcast(
										context,
										0,
										new Intent(context,
												PlaceItNotification.class)
												.putExtra(
														BUTTON_TAG,
														R.string.notification_discard)
												.putExtra(
														MainActivity.PLACEIT_ID,
														pID),
										PendingIntent.FLAG_UPDATE_CURRENT))

				// Automatically dismiss the notification when it is touched.
				.setAutoCancel(true);

		notify(context, builder.build(), pID);
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private static void notify(final Context context,
			final Notification notification, int pID) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.notify(NOTIFICATION_TAG, pID, notification);
		} else {
			nm.notify(NOTIFICATION_TAG.hashCode(), notification);
		}
	}

	/**
	 * Cancels any notifications of this type previously shown using
	 * {@link #notify(Context, String, int)}.
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void cancel(final Context context, int pID) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.cancel(NOTIFICATION_TAG, pID);
		} else {
			nm.cancel(NOTIFICATION_TAG.hashCode());
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		int pID;
		if ((pID = bundle.getInt(MainActivity.PLACEIT_ID, -1)) == -1) 
			//pID extra not found
			throw new RuntimeException("Notification not used correctly");

		if (bundle.get(BUTTON_TAG).equals(R.string.notification_discard))
			discardAction(context, pID);
		else if (bundle.get(BUTTON_TAG).equals(R.string.notification_repost))
			repostAction(context, pID);
		else
			throw new RuntimeException("Notification not used correctly");
	}

	private void discardAction(Context context, int pID) {
		PlaceItNotification.cancel(context, pID);
		Log.w("Notification", "Place it discarded");
		Toast.makeText(context, "Place-it discarded.", Toast.LENGTH_LONG).show();

		// TODO: Actually discarding Place-it
	}

	private void repostAction(Context context, int pID) {
		PlaceItNotification.cancel(context, pID);
		Log.w("Notification", "Place it discarded");
		Toast.makeText(context, "Place-it reposted.", Toast.LENGTH_LONG).show();
		
		// TODO: Actually reposting Place-it
	}

}
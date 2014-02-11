package edu.ucsd.placeitapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Helper class for showing and canceling place it notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class PlaceItNotification {
	/**
	 * The unique identifier for this type of notification.
	 */
	private static final String NOTIFICATION_TAG = "PlaceIt";

	/**
	 * Shows the notification, or updates a previously shown notification of
	 * this type, with the given parameters.

	 * TODO: Customize the contents of this method to tweak the behavior and
	 * presentation of place it notifications. Make sure to follow the <a
	 * href="https://developer.android.com/design/patterns/notifications.html">
	 * Notification design guidelines</a> when doing so.
	 * 
	 */
	public static void notify(final Context context, final int pID) {
		final Resources res = context.getResources();

		//PlaceIt placeit = PlaceIt.find(pID); 
		//final String name = placeit.getTitle().toString(); 
		//final String description = placeit.getDescription().toString();
		
		final String title = "Go to market"; //temp 
		final String description = "Pick up groceries."; // temp
		
		final String fullTitle = res.getString(
				R.string.place_it_notification_title_template, title);
		final String fullDescription = res.getString(
				R.string.place_it_notification_text_template,
				description);

		final NotificationManager manager = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE); 
		
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)

				// Set appropriate defaults for the notification light, sound,
				// and vibration.
				.setDefaults(Notification.DEFAULT_ALL)
				
				// required values
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(fullTitle)
				.setContentText(fullDescription)

				.setPriority(NotificationCompat.PRIORITY_DEFAULT)

				// Set preview information for this notification.
				.setTicker(title)

				// Show a number. 
				.setNumber(pID) //temp

				// On click action
				.setContentIntent(
						PendingIntent.getActivity(
								context,
								0,
								new Intent(context, DescriptionActivity.class)
									.putExtra(MainActivity.PLACEIT_ID, pID),
								PendingIntent.FLAG_UPDATE_CURRENT))

				// Repost action
				.addAction(R.drawable.ic_stat_repost,
						res.getString(R.string.notification_repost), null)
					
				// Discard action
				.addAction(R.drawable.ic_stat_discard,
						res.getString(R.string.notification_discard), null)

				// Automatically dismiss the notification when it is touched.
				.setAutoCancel(true);

		notify(context, builder.build());
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private static void notify(final Context context,
			final Notification notification) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.notify(NOTIFICATION_TAG, 0, notification);
		} else {
			nm.notify(NOTIFICATION_TAG.hashCode(), notification);
		}
	}

	/**
	 * Cancels any notifications of this type previously shown using
	 * {@link #notify(Context, String, int)}.
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void cancel(final Context context) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.cancel(NOTIFICATION_TAG, 0);
		} else {
			nm.cancel(NOTIFICATION_TAG.hashCode());
		}
	}
}
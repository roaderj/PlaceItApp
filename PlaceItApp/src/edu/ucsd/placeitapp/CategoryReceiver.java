package edu.ucsd.placeitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import edu.ucsd.placeitapp.model.CategoricalPlaceIt;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;

/*
 * Receiver to send notification if Category Place It is found near the user.
 */
public class CategoryReceiver {

	public static void Found(Context context ,CategoricalPlaceIt p) {
		 
		p.setEnabled(false); 
		int pID = p.getId();
		PlaceItList.save(p); 
		PlaceItNotification.notify(context, pID); 
	}
}
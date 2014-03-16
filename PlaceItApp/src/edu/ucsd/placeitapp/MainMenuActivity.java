package edu.ucsd.placeitapp;


import edu.ucsd.placeitapp.model.CategoryChecker;
import java.util.List;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItDb;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/* 
 * Main window for going to list, map, logging out, or creating categorical place-it. 
 */
public class MainMenuActivity extends Activity {

	Intent checker;
	Intent syncing; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checker = new Intent(this, PlaceItCheckService.class);
		startService(checker);
		syncing = new Intent(this, PlaceItSyncingService.class); 
		startService(syncing); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Map button click action
	 */
	public void goToMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

	/*
	 * List button click action
	 */
	public void goToList(View view) {
		Intent intent = new Intent(this, ListViewActivity.class);
		startActivity(intent);
	}
	
	/*
	 * Create categorical placeit button click action
	 */
	public void createCategorical(View view) {
		Intent intent = new Intent(this, NewCatPlaceitActivity.class);
		startActivity(intent);
	}
	
	/*
	 * Logout button click action
	 */
	public void logOut(View v) {
		PlaceItDb instance = PlaceItDb.getInstance();
		List<PlaceIt> placeIts = PlaceItList.all(); 
		for (PlaceIt placeIt : placeIts) {
			//disable all trackLocations
			placeIt.trackLocation(this, false);
			//disable all alarms
			placeIt.setAlarm(this, false);
			//drop the current table
			//instance.delete(placeIt);
		}
		//disable the current user
		SyncClient.logOut();
		//stop checking for Categorical
		stopService(checker);
		//stop syncing
		stopService(syncing); 
		//return to login window
		Intent returnSignUp = new Intent(this, MainActivity.class); 
		startActivity(returnSignUp); 
		finish(); 
	}

}

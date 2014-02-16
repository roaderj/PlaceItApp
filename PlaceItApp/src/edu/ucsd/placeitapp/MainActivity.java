package edu.ucsd.placeitapp;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	public final static String PLACEIT_ID = "edu.ucsd.placeitapp.PLACEIT_ID"; 
	public final static long REPOST_WAIT_TIME = 2700000; // millis in 45 minutes
	public final static long WEEK_INTERVAL = 604800000; // millis in 1 week
	public final static long MIN_INTERVAL = 60000; // millis in 1 minutes

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PlaceItDBHelper.setInstance(this.getApplicationContext());
		
		PlaceIt loadedPlaceIt = PlaceIt.find(5);
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		if (loadedPlaceIt == null) {

			dlgAlert.setMessage("PlaceIt not found. Creating place it...: ");
			dlgAlert.setTitle("App Title");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			

			Location location = new Location("network");
			location.setLatitude(100);
			location.setLongitude(100);
			PlaceIt somePlaceIt = new PlaceIt("someName", "someDescription", location);
			somePlaceIt.save();
		} else {
			dlgAlert.setMessage("PlaceIt instantiated: " + loadedPlaceIt.getTitle() + " with id: " + loadedPlaceIt.getId());
			dlgAlert.setTitle("App Title2");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void goToMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

	public void goToList(View view) {
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
	}
}

package edu.ucsd.placeitapp;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewPlaceitActivity extends Activity {

	private Location location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_placeit);
		// get the location
		Intent intent = getIntent();
		location = new Location("map");
		location.setLatitude(intent.getDoubleExtra("latitude", 0));
		location.setLongitude(intent.getDoubleExtra("longitude", 0));
		Log.d("",""+location.getLatitude()+","+location.getLongitude());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_placeit, menu);
		return true;
	}
	// Go back to the former activity
	public void back(View view) {
		finish();
	}
	// Create a new place-it with the info entered
	public void createPlaceIt(View view) {
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxSch);
		EditText editName = (EditText) findViewById(R.id.editName);
		EditText editDes = (EditText) findViewById(R.id.editDes);
		EditText editTime = (EditText) findViewById(R.id.editTime);
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		// name empty error
		if (editName.getText().length() == 0) {
			dlgAlert.setMessage("Please enter a name");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			return;
		}
		String name = editName.getText().toString();
		String des = editDes.getText().toString();
		// time empty error
		int time = 0;
		if (check.isChecked()) {
			if (editTime.getText().length() == 0) {
				dlgAlert.setMessage("Please enter a recurring time");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				return;
			}
			time = Integer.parseInt(editTime.getText().toString());
			// time equal 0 error
			if (time == 0) {
				dlgAlert.setMessage("Time can not be 0");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				return;
			}
		}
		
		PlaceIt placeit; 
		if (check.isChecked())
			placeit = new PlaceIt(name, des, location, true, time); 
		else
			placeit = new PlaceIt(name, des, location); 
		
		PlaceItList.save(placeit); 
		placeit.setAlarm(this.getApplicationContext(), true);
		finish();
	}
}

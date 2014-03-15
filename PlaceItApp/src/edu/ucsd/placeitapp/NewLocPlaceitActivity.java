package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItFactory;
import edu.ucsd.placeitapp.model.PlaceItList;
import edu.ucsd.placeitapp.model.PlaceIts;
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

/* 
 * Activity for creating a location place-it. 
 */
public class NewLocPlaceitActivity extends Activity {

	private Location location;
	//Factory to handle PlaceIt creation
	PlaceItFactory factory; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_location_placeit);
		// get the location
		Intent intent = getIntent();
		location = new Location("map");
		location.setLatitude(intent.getDoubleExtra("latitude", 0));
		location.setLongitude(intent.getDoubleExtra("longitude", 0));
		Log.d("",""+location.getLatitude()+","+location.getLongitude());
		
		factory = new PlaceItFactory(); 
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
		
		Bundle factoryData = new Bundle(); 
		
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
		int time = -1;
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
		
		//Bundle data and create
		factoryData.putString(PlaceItFactory.PLACEIT_TITLE, name); 
		factoryData.putString(PlaceItFactory.PLACEIT_DESCRIPTION, des); 
		factoryData.putBoolean(PlaceItFactory.PLACEIT_IS_RECURRING, check.isChecked());
		factoryData.putInt(PlaceItFactory.PLACEIT_RECURRING_INTERVAL, time);
		factoryData.putDouble(PlaceItFactory.PLACEIT_LONGITUDE, location.getLongitude()); 
		factoryData.putDouble(PlaceItFactory.PLACEIT_LATITUDE, location.getLatitude()); 

		PlaceIt placeit = factory.create(PlaceIts.LOCATION, factoryData); 

		PlaceItList.save(placeit); 
		PlaceItList.save(placeit); 
		placeit.setAlarm(this.getApplicationContext(), true);

		finish();
	}
}

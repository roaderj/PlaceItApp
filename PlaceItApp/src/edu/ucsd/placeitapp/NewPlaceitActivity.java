package edu.ucsd.placeitapp;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		
		Intent intent = getIntent();
		location = (Location)intent.getSerializableExtra("location");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_placeit, menu);
		return true;
	}
	
	public void back(View view) {
		finish();
	}
	
	public void createPlaceIt(View view) {
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxSch);
		EditText editName = (EditText) findViewById(R.id.editName);
		EditText editDes = (EditText) findViewById(R.id.editDes);
		EditText editTime = (EditText) findViewById(R.id.editTime);
		String name = editName.getText().toString();
		String des = editDes.getText().toString();
		int time = Integer.parseInt(editTime.getText().toString());
		PlaceIt placeit = new PlaceIt(name,des,location);
		if (check.isChecked()) {
			placeit.setRecurring(true);
			placeit.setRecurringIntervalWeeks(time);
		}
		placeit.save();
		finish();
	}

}

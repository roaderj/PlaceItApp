package edu.ucsd.placeitapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DescriptionActivity extends Activity {
	
	private PlaceIt placeit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description);
		// get the info about the place-it
		Intent intent = getIntent();
		placeit = (PlaceIt) intent.getSerializableExtra("placeit");
		// set name
		TextView nameTextView = (TextView)findViewById(R.id.textViewName);
		nameTextView.setText(placeit.getTitle());
		// set description
		TextView desTextView = (TextView)findViewById(R.id.textShowDes);
		desTextView.setText(placeit.getDescription());
		// set recurring info
		TextView schTextView = (TextView)findViewById(R.id.textShowSch);
		if (placeit.isRecurring() == false)
			schTextView.setText("Not Recurring");
		else {
			int time = placeit.getRecurringIntervalWeeks();
			if (time <= 1)
				schTextView.setText(time + " week");
			else
				schTextView.setText(time + " weeks");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}
	// Delete the current place-it
	public void deletePlaceIt(View view) {
		placeit.delete();
		finish();
	}
	// Repost the place-it
	public void repostPlaceIt(View view) {
		//TODO put alarm here
		finish();
	}
	// Go back to the former activity
	public void back(View view) {
		finish();
	}
}

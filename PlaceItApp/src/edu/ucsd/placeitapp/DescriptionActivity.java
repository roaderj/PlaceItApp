package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DescriptionActivity extends Activity {

	private PlaceIt placeit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description);
		// get the info about the place-it
		Intent intent = getIntent();
		int pID = intent.getIntExtra(MainActivity.PLACEIT_ID, -1);
		placeit = PlaceItList.find(pID);
		// set name
		TextView nameTextView = (TextView) findViewById(R.id.textViewName);
		nameTextView.setText(placeit.getTitle());
		// set description
		TextView desTextView = (TextView) findViewById(R.id.textShowDes);
		desTextView.setText(placeit.toString());
		Button repost = (Button) findViewById(R.id.buttonRep);
		if (placeit.isEnabled()) {
			repost.setEnabled(false);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

	// Delete the current place-it
	public void deletePlaceIt(View view) {
		PlaceItList.delete(placeit);
		
		Toast.makeText(this, "Place-it was deleted.", Toast.LENGTH_LONG).show();
		
		finish();
	}

	// Repost the place-it
	public void repostPlaceIt(View view) {

		placeit.repost(this);
		placeit.setAlarm(this.getApplicationContext(), true);
		
		Toast.makeText(this, "Place-it will be reposted in 10 seconds.",
				Toast.LENGTH_LONG).show();

		finish();
	}

	// Go back to the former activity
	public void back(View view) {
		finish();
	}
}

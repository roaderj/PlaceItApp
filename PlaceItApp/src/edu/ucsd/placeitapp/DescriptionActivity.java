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
		
		Intent intent = getIntent();
		placeit = (PlaceIt) intent.getSerializableExtra("placeit");
		TextView nameTextView = (TextView)findViewById(R.id.textViewName);
		nameTextView.setText(placeit.getTitle());
		TextView desTextView = (TextView)findViewById(R.id.textViewDes);
		desTextView.setText(placeit.getDescription());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

	public void deletePlaceIt(View view) {
		placeit.delete();
		finish();
	}
	
	public void repostPlaceIt(View view) {
		//TODO
		finish();
	}
	
	public void back(View view) {
		finish();
	}
}

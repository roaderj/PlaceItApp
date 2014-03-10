package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceItDBHelper;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenuActivity extends Activity {
	public final static String PLACEIT_ID = "edu.ucsd.placeitapp.PLACEIT_ID";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PlaceItDBHelper.setInstance(this.getApplicationContext());
		PlaceItList.setInstance(this.getApplicationContext()); 
		
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
		Intent intent = new Intent(this, ListViewActivity.class);
		startActivity(intent);
	}

}

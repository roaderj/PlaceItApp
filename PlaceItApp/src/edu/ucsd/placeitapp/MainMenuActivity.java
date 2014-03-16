package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.CategoryChecker;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//EntityDb.setInstance(this.getApplicationContext()); Moved to MainActivity
		PlaceItList.setInstance(this.getApplicationContext());
		checker = new Intent(this, PlaceItCheckService.class);
		startService(checker);
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
		SyncClient.logOut();
		stopService(checker);
		//return to login window
		Intent returnSignUp = new Intent(this, MainActivity.class); 
		startActivity(returnSignUp); 
		finish(); 
	}

}

package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.PlaceItList;
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

		//EntityDb.setInstance(this.getApplicationContext()); Moved to MainActivity
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
	
	public void createCategorical(View view) {
		Intent intent = new Intent(this, NewCatPlaceitActivity.class);
		startActivity(intent);
	}
	
	public void logOut(View v) {
		SyncClient.logOut(); 
		Intent returnSignUp = new Intent(this, MainActivity.class); 
		startActivity(returnSignUp); 
		finish(); 
	}

}

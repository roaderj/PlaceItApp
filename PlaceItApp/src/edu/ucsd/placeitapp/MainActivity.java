package edu.ucsd.placeitapp;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
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
	
	public void testClick(View view) {
		Location x = new Location("stuff"); 
		x.setLatitude(33); 
		x.setLongitude(-100); 
		PlaceIt p = new PlaceIt("some text", "title", x);
		p.setRecurring(true); 
		p.setRecurringIntervalWeeks(2); 
		PlaceItList.save(p); 
		
		PlaceItNotification.notify(this.getApplicationContext(), p.getId());
	}

}

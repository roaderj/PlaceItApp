package edu.ucsd.placeitapp;

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
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void logIn(View v) {
		//TODO
		Intent intent = new Intent(this, MainMenuActivity.class); 
		startActivity(intent); 
	}

}

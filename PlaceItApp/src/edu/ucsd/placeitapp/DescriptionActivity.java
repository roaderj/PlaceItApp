package edu.ucsd.placeitapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class DescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

	public void back(View view) {
		finish();
	}
}

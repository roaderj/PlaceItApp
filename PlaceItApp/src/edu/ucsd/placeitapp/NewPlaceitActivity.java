package edu.ucsd.placeitapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewPlaceitActivity extends Activity {

	private CheckBox check;
	private EditText editName,editDes,editTime;
	private String name,des,time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_placeit);
		
		
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
		check = (CheckBox) findViewById(R.id.checkBox1);
		editName = (EditText) findViewById(R.id.editName);
		editDes = (EditText) findViewById(R.id.editDes);
		editTime = (EditText) findViewById(R.id.editTime);
		name = editName.getText().toString();
		des = editDes.getText().toString();
		time = editTime.getText().toString();
		//TODO
		finish();
	}

}

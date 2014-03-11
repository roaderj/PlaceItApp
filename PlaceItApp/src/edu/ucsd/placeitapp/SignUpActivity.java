package edu.ucsd.placeitapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends Activity {

	public final static int MINIMUM_LENGTH = 4; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	
	public void signUp(View v) {
		String username = ((EditText) this.findViewById(R.id.userBox)).getText().toString();
		String password = ((EditText) this.findViewById(R.id.passwordBox)).getText().toString(); 
		String confirmPassword = ((EditText) this.findViewById(R.id.confirmBox)).getText().toString(); 
	
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		if (username.length() < MINIMUM_LENGTH) {

			dlgAlert.setTitle("Invalid User");
			dlgAlert.setMessage("Username must be at least " + MINIMUM_LENGTH + " characters long.");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			
		} else if (password.length() < MINIMUM_LENGTH) {
			
			dlgAlert.setTitle("Invalid Password");
			dlgAlert.setMessage("Passwords must be at least " + MINIMUM_LENGTH + " characters long.");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			
		} else if (!password.equals(confirmPassword)) {
			
			dlgAlert.setTitle("Invalid Password");
			dlgAlert.setMessage("Passwords do not match");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			
		} else {
			//TODO: Create User
			finish(); 
		}
		
	}

}

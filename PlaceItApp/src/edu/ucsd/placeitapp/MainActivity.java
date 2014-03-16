package edu.ucsd.placeitapp;

import edu.ucsd.placeitapp.model.EntityDb;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* 
 * Log-in Window. Default launcher. 
 */
public class MainActivity extends Activity {
	//Used for intent bundles
	public final static String PLACEIT_ID = "edu.ucsd.placeitapp.PLACEIT_ID";
		
	public static final String TAG = "MainActivity Debug";
	
	private Activity currentActivity; 
	private EditText userField; 
	private EditText passField; 
	
	private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_login);

		userField = (EditText) findViewById(R.id.IDBox); 
		passField = (EditText) findViewById(R.id.PWBox); 
		currentActivity = this; 
		
		//Initialize db for users and placeits
		EntityDb.setInstance(this.getApplicationContext());
		
		//If a user is logged-in, go to Main Menu Window
		if (EntityDb.getInstance().getLoggedInUser() != null) {
			PlaceItList.setInstance(currentActivity);
			SyncClient.sync(currentActivity); 
			
			Intent cachedUser = new Intent(this, MainMenuActivity.class); 
			startActivity(cachedUser); 
			finish(); 
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
	 * Handle sign-up button action
	 */
	public void signUp(View v) {
		Intent signUp = new Intent(this, SignUpActivity.class); 
		startActivity(signUp); 
	}
	
	/*
	 * Handle log-in button action
	 */
	public void logIn(View v) {

		String username = userField.getText().toString();
		String password = passField.getText().toString();
		
		//Check the user validation and show a progress dialog to block UI interaction.
		progressDialog = ProgressDialog.show(this, "Logging in...", "Please wait...", false);
		new ValidateUserTask().execute(username, password); 
	}		
	
	/* 
	 * Handles user validation asynchronously
	 */
	private class ValidateUserTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return SyncClient.validateUser(params[0], params[1]);
		}


		//Check the return value and handle accordingly.
		protected void onPostExecute(Boolean isValid) {
			progressDialog.dismiss();
			if (isValid) {
				Toast.makeText(currentActivity, "Welcome!", Toast.LENGTH_SHORT).show();
				EntityDb.getInstance().resetDB(); 
				PlaceItList.setInstance(currentActivity);
				
				Intent intent = new Intent(currentActivity, MainMenuActivity.class); 
				startActivity(intent); 
				finish(); 
			} else {
				Toast.makeText(currentActivity, "Invalid credentials.", Toast.LENGTH_SHORT).show(); 
			}

		}
	}

}

package edu.ucsd.placeitapp;

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

public class MainActivity extends Activity {
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	public void signUp(View v) {
		Intent signUp = new Intent(this, SignUpActivity.class); 
		startActivity(signUp); 
	}
	
	public void logIn(View v) {

		String username = userField.getText().toString();
		String password = passField.getText().toString();
		
		progressDialog = ProgressDialog.show(this, "Logging in...", "Please wait...", false);
		new ValidateUserTask().execute(username, password); 
	}		
	
	private class ValidateUserTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return SyncClient.validateUser(params[0], params[1]);
		}

		protected void onPostExecute(Boolean isValid) {
			progressDialog.dismiss();
			if (isValid) {
				Toast.makeText(currentActivity, "Welcome!", Toast.LENGTH_SHORT).show(); 
				Intent intent = new Intent(currentActivity, MainMenuActivity.class); 
				startActivity(intent); 
				finish(); 
			} else {
				Toast.makeText(currentActivity, "Invalid credentials.", Toast.LENGTH_SHORT).show(); 
			}

		}
	}

}

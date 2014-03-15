package edu.ucsd.placeitapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends Activity {

	public static final String TAG = "SIGNUP_DEBUG";
	public final static int MIN_FIELD_LENGTH = 4;

	private Activity currentActivity;
	private AlertDialog.Builder alertDialog;
	private ProgressDialog progressDialog;
	private EditText userField;
	private EditText passField;
	private EditText confirmField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		userField = (EditText) findViewById(R.id.userBox);
		passField = (EditText) findViewById(R.id.passwordBox);
		confirmField = (EditText) findViewById(R.id.confirmBox);

		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setPositiveButton("OK", null);
		alertDialog.setCancelable(true);

		currentActivity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	public void signUp(View v) {
		String username = userField.getText().toString();
		String password = passField.getText().toString();
		String confirmPassword = confirmField.getText().toString();

		if (username.length() < MIN_FIELD_LENGTH) {

			alertDialog.setTitle("Invalid User");
			alertDialog.setMessage("Username must be at least "
					+ MIN_FIELD_LENGTH + " characters long.");
			alertDialog.create().show();

		} else if (password.length() < MIN_FIELD_LENGTH) {

			alertDialog.setTitle("Invalid Password");
			alertDialog.setMessage("Passwords must be at least "
					+ MIN_FIELD_LENGTH + " characters long.");
			alertDialog.create().show();

		} else if (!password.equals(confirmPassword)) {

			alertDialog.setTitle("Invalid Password");
			alertDialog.setMessage("Passwords do not match");
			alertDialog.create().show();

		} else {
			progressDialog = ProgressDialog.show(this, "Creating user...",
					"Please wait...", false);
			progressDialog.show();
			new CreateUserTask().execute(username, password);
		}

	}

	private class CreateUserTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return SyncClient.createUser(params[0], params[1]);
		}

		protected void onPostExecute(Boolean isValid) {
			progressDialog.dismiss();
			if (isValid) {
				currentActivity.finish(); 
			} else {
				alertDialog.setTitle("Invalid Username");
				alertDialog.setMessage("Username already taken.");
				alertDialog.create().show();
			}

		}
	}

}

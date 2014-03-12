package edu.ucsd.placeitapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends Activity {

	public static final String TAG = "SIGNUP_DEBUG";

	public final static int MINIMUM_LENGTH = 4;
	Context activityContext; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		activityContext = this; 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	public void signUp(View v) {
		String username = ((EditText) this.findViewById(R.id.userBox))
				.getText().toString();
		String password = ((EditText) this.findViewById(R.id.passwordBox))
				.getText().toString();
		String confirmPassword = ((EditText) this.findViewById(R.id.confirmBox))
				.getText().toString();

		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		if (username.length() < MINIMUM_LENGTH) {

			dlgAlert.setTitle("Invalid User");
			dlgAlert.setMessage("Username must be at least " + MINIMUM_LENGTH
					+ " characters long.");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();

		} else if (password.length() < MINIMUM_LENGTH) {

			dlgAlert.setTitle("Invalid Password");
			dlgAlert.setMessage("Passwords must be at least " + MINIMUM_LENGTH
					+ " characters long.");
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
			createUser(username, password);
		}

	}

	public void createUser(final String user, final String password) {
		// Gotta hash that shiz, but I'm lazy
		Runnable thread = new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				
				String usersURI = MainActivity.GAE_BASE + MainActivity.USER_SUFFIX; 
				HttpPost post = new HttpPost(usersURI); 
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

				nameValuePairs.add(new BasicNameValuePair("name", user));
				nameValuePairs.add(new BasicNameValuePair("password", password)); 
				nameValuePairs.add(new BasicNameValuePair("action", "put"));
				 
				try {
					HttpGet request = new HttpGet(usersURI + MainActivity.USER_QUERY + user);
					HttpResponse getResponse = client.execute(request);
					HttpEntity entity = getResponse.getEntity(); 
					String data = EntityUtils.toString(entity); 
					if (data.isEmpty()) {
						post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						client.execute(post);		
						
						finish();
					} else {
//						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activityContext); 
//						dlgAlert.setTitle("Invalid username");
//						dlgAlert.setMessage("Username has already been taken. Please try another.");
//						dlgAlert.setPositiveButton("OK", null);
//						dlgAlert.setCancelable(true);
//						dlgAlert.create().show();
						Log.d(TAG, "Username taken.");
						Log.d(TAG, data); 
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread(thread).start();

	}

}

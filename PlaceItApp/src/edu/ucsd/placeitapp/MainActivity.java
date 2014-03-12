package edu.ucsd.placeitapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String PLACEIT_ID = "edu.ucsd.placeitapp.PLACEIT_ID";
	
	public static final String GAE_BASE = "http://placeits33.appspot.com/";
	
	public static final String USER_SUFFIX = "user"; 
	public static final String USER_QUERY = "?username="; 
	
	public static final String TAG = "MAIN_ACTIVITY_DEBUG"; 

	private boolean validUser = false; 

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
		
	public void signUp(View v) {
		Intent signUp = new Intent(this, SignUpActivity.class); 
		startActivity(signUp); 
	}
	
	public void logIn(View v) {

		String username = ((EditText) this.findViewById(R.id.IDBox))
				.getText().toString();
		String password = ((EditText) this.findViewById(R.id.PWBox))
				.getText().toString();
		
		if (validateUser(username, password)) {
			Intent intent = new Intent(this, MainMenuActivity.class); 
			startActivity(intent); 
		} else {
			Toast.makeText(this, "Invalid username.", Toast.LENGTH_SHORT).show(); 
		}

	}		
	
	public boolean validateUser(final String username, final String password) {
		validUser = false;  
		Runnable thread = new Runnable() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				try {
					
					String usersURI = MainActivity.GAE_BASE + MainActivity.USER_SUFFIX; 
					HttpGet request = new HttpGet(usersURI + MainActivity.USER_QUERY + username);
					HttpResponse getResponse = client.execute(request);
					
					String data = EntityUtils.toString(getResponse.getEntity()); 
					if (data.isEmpty()) {
						Log.d(TAG, "Username not found"); 
						return; 
					}
					
					JSONObject user = new JSONObject(data).getJSONArray("data").getJSONObject(0); 
					
					if (user.getString("name").equals(username) 
							&& user.getString("password").equals(password)) {
						Log.d(TAG, "Valid User"); 
						validUser = true; 
					} else {
						Log.d(TAG, "invalid credentials"); 
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		Thread t = new Thread(thread);
		t.start(); 
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		return validUser; 
	}



}

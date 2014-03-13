package edu.ucsd.placeitapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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

import android.util.Log;

public class SyncClient {

	public static final String TAG = "SyncClient";

	public static final String GAE_BASE = "http://placeits33.appspot.com/";

	public static final String USER_URI = GAE_BASE + "user";
	public static final String USER_QUERY = "?username=";

	public static boolean createUser(final String username,
			final String password) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(USER_URI);
		HttpGet request = new HttpGet(USER_URI + USER_QUERY + username);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("name", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("action", "put"));

		try {
			HttpEntity entity = client.execute(request).getEntity();

			String data = EntityUtils.toString(entity);
			if (data.isEmpty()) {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				client.execute(post);
				return true;
			} else {
				Log.d(TAG, "Username taken.");
				Log.d(TAG, data);
				return false;

			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean validateUser(String username, String password) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(USER_URI + USER_QUERY + username);
		try {

			HttpEntity entity = client.execute(request).getEntity(); 

			String data = EntityUtils.toString(entity);
			if (data.isEmpty()) {
				Log.d(TAG, "Username not found");
				return false;
			}

			JSONObject user = new JSONObject(data).getJSONArray("data")
					.getJSONObject(0);

			if (user.getString("name").equals(username)
					&& user.getString("password").equals(password)) {
				Log.d(TAG, "Valid User");
				return true; 
			} else {
				Log.d(TAG, "invalid credentials");
				return false; 
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false; 
		} catch (JSONException e) {
			e.printStackTrace();
			return false; 
		}

	}

}

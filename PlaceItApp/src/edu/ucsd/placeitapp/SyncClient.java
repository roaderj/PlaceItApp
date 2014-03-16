package edu.ucsd.placeitapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.net.URLDecoder;

import edu.ucsd.placeitapp.model.EntityDb;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItDb;
import edu.ucsd.placeitapp.model.PlaceItList;

import android.content.Context;
import android.util.Log;

public class SyncClient {

	public static final String TAG = "SyncClient";

	public static final String GAE_BASE = "http://placeits33.appspot.com/";
	public static final String USER_URI = GAE_BASE + "user";
	public static final String PLACEIT_URI = GAE_BASE + "placeit";

	public static final String USER_QUERY = "?username=";
	public static final String ID_QUERY = "?identifier=";

	public static boolean createUser(final String username,
			final String password) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(USER_URI);
		HttpGet request = new HttpGet(USER_URI + USER_QUERY + username);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
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
				// Cache new user
				EntityDb.getInstance().insertUser(username, password, true);
				return true;
			} else {
				Log.d(TAG, "invalid credentials");
				return false;
			}

		} catch (IOException e) {
			// Check cached database if network errors
			return EntityDb.getInstance().validateUser(username, password);
		} catch (JSONException e) {
			e.printStackTrace();
			// If any other kind of error
			return EntityDb.getInstance().validateUser(username, password);
		}

	}

	public static void logOut() {
		EntityDb.getInstance().logOut();
	}

	public static void pushPlaceIt(final PlaceIt p) {
		Thread t = new Thread() {
			public void run() {
				String user = EntityDb.getInstance().getLoggedInUser();
				String data = "";
				try {
					data = URLEncoder.encode(
							PlaceItDb.getInstance().serialize(p), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				Log.d(TAG, data);
				String identifier = user + Integer.toString(p.getId()); // TODO:
																		// This
																		// needs
																		// to be
																		// changed

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(PLACEIT_URI);
				HttpGet request = new HttpGet(PLACEIT_URI + ID_QUERY
						+ identifier);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						4);
				nameValuePairs.add(new BasicNameValuePair("identifier",
						identifier));
				nameValuePairs.add(new BasicNameValuePair("User", user));
				nameValuePairs.add(new BasicNameValuePair("Data", data));
				nameValuePairs.add(new BasicNameValuePair("action", "put"));

				try {
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					client.execute(post);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deletePlaceIt(final PlaceIt p) {
		Thread t = new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(PLACEIT_URI);

				String user = EntityDb.getInstance().getLoggedInUser();
				String identifier = new String(user + p.getId());

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("identifier",
						identifier));
				nameValuePairs.add(new BasicNameValuePair("action", "delete"));

				try {
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					client.execute(post);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static List<PlaceIt> pullPlaceIts() {

		List<PlaceIt> pulledPlaceIts = new ArrayList<PlaceIt>();
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(PLACEIT_URI + USER_QUERY
				+ EntityDb.getInstance().getLoggedInUser());
		try {

			HttpEntity entity = client.execute(request).getEntity();

			JSONArray userData = new JSONObject(EntityUtils.toString(entity))
					.getJSONArray("data");

			// Log.d(TAG, EntityDb.getInstance().getLoggedInUser());
			// Log.d(TAG, userData.toString());
			for (int i = 0; i < userData.length(); ++i) {
				JSONObject storedPlaceIt = (JSONObject) userData.get(i);
				String data = URLDecoder.decode(
						(String) storedPlaceIt.get("Data"), "UTF-8");
				// Log.d(TAG, data);
				PlaceIt p = PlaceItDb.getInstance().deserialize(data);
				pulledPlaceIts.add(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return pulledPlaceIts;
	}

	/* Very hacky right now */
	public static void sync(final Context context) {
		Thread t = new Thread() {
			public void run() {
				List<PlaceIt> fromUpStream = pullPlaceIts();
				List<PlaceIt> currentList = PlaceItList.all();
				List<Integer> idValues = new ArrayList<Integer>();
				List<String> descriptions = new ArrayList<String>(); 
				if (fromUpStream == null) {
					return;
				}

				for (PlaceIt p : fromUpStream) {
					idValues.add(p.getId()); 
					//descriptions.add(p.toString()); 
				}
				for (PlaceIt p : currentList) {
					descriptions.add(p.toString()); 
				}
				
				// Merge fromUpStream and current db
				for (PlaceIt p : currentList) {
					if (!idValues.contains(p.getId())) {
						PlaceItList.delete(p);
						Log.d("Tag", "Placeit deleted");
					}
				}

				for (PlaceIt p : fromUpStream) {
					if (!descriptions.contains(p.toString())) {
						//Log.d("Tag", "Placeit fetched from upstream");
						PlaceItList.save(p);
						if (p.isEnabled())
							p.setAlarm(context, true);
						if (p.isRecurring())
							p.recur(context);
					}

				}
//				for (PlaceIt i : fromUpStream) {
//					for (PlaceIt j : currentList)
//						if (i.equals(j))
//							Log.d(TAG, "A Match!"); 
//				}
			}
		};
		t.start();
	}
}

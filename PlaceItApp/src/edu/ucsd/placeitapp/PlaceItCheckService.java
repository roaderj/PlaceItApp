package edu.ucsd.placeitapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import edu.ucsd.placeitapp.model.CategoricalPlaceIt;
import edu.ucsd.placeitapp.model.CategoryChecker;
import edu.ucsd.placeitapp.model.Place;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/*
 * This is background service which will check whether Categorical Place it is near.
 * Utilizes Google Places API.
 * 
 * Refernece: http://karnshah8890.blogspot.com/2013/03/google-places-api-tutorial.html
 */
public class PlaceItCheckService extends Service implements Observer,
		LocationListener {

	private String spec; // tag
	private boolean flag = false;
	private Context context;
	private String address;
	private Location loc;

	private long prevTime = System.currentTimeMillis();
	private final long INTERVAL = 15000; // 15000ms
	private List<CategoricalPlaceIt> checkList;
	private List<PlaceIt> list;
	private LocationManager manager;
	private String provider;
	Thread check;

	//variable to stop the infinite loop for the thread in service
	private static boolean stop;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// sets the list to check
	public void setList() {
		list = PlaceItList.getInstance().placeIts;
		checkList = new ArrayList<CategoricalPlaceIt>();
		for (PlaceIt p : list) {
			if (p.getClass() == CategoricalPlaceIt.class) {
				if (((CategoricalPlaceIt) p).isEnabled())
					checkList.add((CategoricalPlaceIt) p);
			}
		}
	}

	/*
	 * Update location when it changes
	 */
	@Override
	public void onLocationChanged(Location location) {
		loc = location;
		Log.d("tag", "" + loc.getLatitude() + "," + loc.getLongitude());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("tag", "onCreate");
		// check status of google play service
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// When google play services is not available
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;

		}
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		//get provider (gps)
		provider = manager.getBestProvider(criteria, false);
		loc = manager.getLastKnownLocation(provider);
		Log.d("checker", provider);
		stop = false;
		context = this;
		setList();
		//add it to Place-It List so that news update of Categorical place Its can be received.
		PlaceItList.getInstance().addObserver(this);
		CheckLoop loop = new CheckLoop();
		check = new Thread(loop);
		check.start();
	}

	public void kill() {
		stop = true;
		stopSelf();
	}

	/*
	 * A separate thread running in this service to check for nearby Categorical
	 * Place-Its Every "INTERVAL" ms.
	 */
	private class CheckLoop implements Runnable {

		@Override
		public void run() {
			while (!stop) {
				if ((System.currentTimeMillis() - prevTime) > INTERVAL) {
					prevTime = System.currentTimeMillis();
					//checks for all categorical place-its that are active.
					for (CategoricalPlaceIt p : checkList) {
						String spec = "";
						//create the types of places to check according to tags 
						List<String> tags = p.getTags();
						Log.d("checker", "" + tags.size());
						for (int i = 0; i < tags.size(); i++) {
							Log.d("checker", tags.get(i));
							if (tags.get(i).length() != 0) {
								spec = spec.concat(tags.get(i));
								spec = spec.concat("|");
							}
						}
						// take off last |
						Log.d("checerk", spec);
						spec = spec.substring(0, spec.length() - 1);
						Log.d("checerk", spec);
						GetPlaces task = new GetPlaces(context, spec);
						task.execute();
						try {
							ArrayList<Place> placeList = task.get();
							Log.d("checker", "" + placeList.size());
							//check only if such places exists near by.
							if (placeList != null && placeList.size() != 0) {
								Log.d("checker", placeList.get(0).getVicinity());
								p.setAddress(placeList.get(0).getVicinity());
								CategoryReceiver.Found(context, p);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/*
	 * ASynctask which runs on separate thread to check for all places match
	 * Categorical Place-it's tags
	 */
	private class GetPlaces extends
			AsyncTask<Void, ArrayList<Place>, ArrayList<Place>> {

		private Context context;
		private String places;

		public GetPlaces(Context context, String places) {
			this.context = context;
			this.places = places;

		}

		@Override
		protected ArrayList<Place> doInBackground(Void... arg0) {

			CategoryChecker service = new CategoryChecker(
					getString(R.string.PlacesKey));
			Log.d("getPlaces",
					"" + loc.getLatitude() + "," + loc.getLongitude());
			ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(),
					loc.getLongitude(), places);

			return findPlaces;

		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			super.onPostExecute(result);
		}

	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		setList();
	}

}
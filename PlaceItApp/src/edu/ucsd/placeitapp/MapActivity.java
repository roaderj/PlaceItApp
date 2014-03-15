package edu.ucsd.placeitapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ucsd.placeitapp.model.LocationPlaceIt;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends Activity implements
		OnMyLocationChangeListener, OnInfoWindowClickListener {

	// latitude and longitude of UCSD in case for no gps
	private final static double UCSD_LATITUDE = 32.8741609;
	private final static double UCSD_LONGITUDE = -117.2357297;

	// list of place-its
	private ArrayList<LocationPlaceIt> placeItList;
	private ArrayList<Marker> placeItMarkers;
	private ArrayList<Marker> searchMarkers;

	// Google map for the user
	private GoogleMap map;

	// GUI components
	private Button backBtn;
	private Button findBtn;
	private Button listBtn;
	private EditText searchBar;
	// context of map activity
	private Context mapActivityContext;
	private Geocoder geocoder;
	private LatLng currentLocation;
	// check for first launch to move camera to current location
	private boolean firstLaunch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		// retrieve gui components
		findBtn = (Button) findViewById(R.id.findBtn);
		backBtn = (Button) findViewById(R.id.BackBtn_Map);
		listBtn = (Button) findViewById(R.id.ToListBtn);
		searchBar = (EditText) findViewById(R.id.SearchBar);
		// set up geocoder to English
		geocoder = new Geocoder(this, Locale.ENGLISH);
		// initialize markers
		searchMarkers = new ArrayList<Marker>();
		placeItMarkers = new ArrayList<Marker>();

		mapActivityContext = this;

		map.setOnMapClickListener(new myMapClickListener());
		// set onclick behaviors to gui components
		findBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String location = searchBar.getText().toString();
				String[] coordination = location.split(",");

				// search by coordination
				if (checkForCoordination(coordination)) {

					for (int i = 0; i < searchMarkers.size(); i++) {
						searchMarkers.get(i).remove();
					}
					searchMarkers = new ArrayList<Marker>();

					double latitude = Double.parseDouble(coordination[0]);
					double longitude = Double.parseDouble(coordination[1]);
					LatLng loc = new LatLng(latitude, longitude);
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(loc);
					markerOptions.title("" + latitude + ", " + longitude);

					searchMarkers.add(map.addMarker(markerOptions));
					map.animateCamera(CameraUpdateFactory
							.newLatLngZoom(loc, 13));
				}
				// search by name
				else
					new GeocoderTask().execute(location);

			}

			// check whether given string array is in coordination format
			private boolean checkForCoordination(String[] coord) {
				try {
					if (coord.length == 2) {
						Double.parseDouble(coord[0]);
						Double.parseDouble(coord[1]);
						return true;
					}
				} catch (NumberFormatException e) {
					return false;
				}
				return false;
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mapActivityContext,
						ListViewActivity.class);
				startActivity(intent);
			}
		});
		// check status of google play service
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// When google play services is not available
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		}
		// Google Play Service is available
		else {

			// enable my location.
			map.setMyLocationEnabled(true);

			// set behavior if my location changes
			map.setOnMyLocationChangeListener(this);

			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
					UCSD_LATITUDE, UCSD_LONGITUDE)));
			map.animateCamera(CameraUpdateFactory.zoomTo(15));
		}
		map.setOnInfoWindowClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// get all placeits
		placeItList = new ArrayList<LocationPlaceIt>();
		for (PlaceIt placeIt : PlaceItList.all(LocationPlaceIt.KEY)) {
			placeItList.add((LocationPlaceIt)placeIt);
		}
		// put all placeIts on the map
		loadMarkers();
		// set first launch to true so that current location will be centered
		firstLaunch = true;
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	/*
	 * Method for getting the locations from the name. Get up to 15 locations.
	 * Move to the first location on the list
	 */
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			List<Address> addresses = null;

			try {
				addresses = geocoder.getFromLocationName(locationName[0], 15);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
				return;
			}

			// Clears all the existing markers on the map
			for (int i = 0; i < searchMarkers.size(); i++) {
				searchMarkers.get(i).remove();
			}
			searchMarkers = new ArrayList<Marker>();

			// add markers on the map
			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				// get the coordinates of the location for the map
				LatLng latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				searchMarkers.add(map.addMarker(markerOptions));

				// move to first location on the list
				if (i == 0)
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
							14));
			}
		}

	}

	/*
	 * load placeIt markers on the map
	 */
	private void loadMarkers() {
		erasePlaceItMarkers();
		placeItMarkers = new ArrayList<Marker>();
		for (LocationPlaceIt p : placeItList) {
//			if (!(p instanceof LocationPlaceIt))
//				continue; 
//			
//			Location loc = ((LocationPlaceIt) p).getLocation();
			Location loc = p.getLocation();
			String title = p.getTitle();
			MarkerOptions marker = new MarkerOptions();
			marker.position(new LatLng(loc.getLatitude(), loc.getLongitude()));
			marker.title(title);
			marker.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.marker_img));

			placeItMarkers.add(map.addMarker(marker));
		}
	}

	/*
	 * erase placeit markers on the map
	 */
	private void erasePlaceItMarkers() {
		for (int i = 0; i < placeItMarkers.size(); i++) {
			placeItMarkers.get(i).remove();
		}
	}

	/*
	 * Listener for clicking on the map. creates new placeIt
	 */
	private class myMapClickListener implements OnMapClickListener {

		@Override
		public void onMapClick(LatLng loc) {
			Intent newPlaceItIntent = new Intent(mapActivityContext,
					NewLocPlaceitActivity.class);
			newPlaceItIntent.putExtra("latitude", loc.latitude);
			newPlaceItIntent.putExtra("longitude", loc.longitude);
			startActivity(newPlaceItIntent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	/* 
	 * behavior for change in my location
	 * if first launch, including resume, move camera to current location
	 */
	@Override
	public void onMyLocationChange(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		currentLocation = new LatLng(latitude, longitude);
		if (firstLaunch) {
			map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
			firstLaunch = false;
		}

	}
	/*
	 * when infowindow for marker is clicked
	 * if marker from search, create new PlaceIt
	 * if marker from PlaceIt, go to description page
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		if (searchMarkers.contains(marker)) {
			Intent newPlaceItIntent = new Intent(mapActivityContext,
					NewLocPlaceitActivity.class);
			LatLng loc = marker.getPosition();
			newPlaceItIntent.putExtra("latitude", loc.latitude);
			newPlaceItIntent.putExtra("longitude", loc.longitude);
			startActivity(newPlaceItIntent);
			marker.remove();
		} else if (placeItMarkers.contains(marker)) {
			Intent viewDescription = new Intent(mapActivityContext,
					DescriptionActivity.class);
			int pnt = placeItMarkers.indexOf(marker);
			viewDescription.putExtra(MainActivity.PLACEIT_ID,
					placeItList.get(pnt).getId());
			startActivity(viewDescription);

		}
	}

}

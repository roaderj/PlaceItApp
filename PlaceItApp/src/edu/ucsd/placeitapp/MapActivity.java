package edu.ucsd.placeitapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends Activity implements OnMyLocationChangeListener {

	private final static double UCSD_LATITUDE = 32.8741609;
	private final static double UCSD_LONGITUDE = -117.2357297;

	private ArrayList<PlaceIt> placeItList;
	private ArrayList<Marker> placeItMarkers;
	private Button findBtn;
	private GoogleMap map;
	private EditText searchBar;
	private Geocoder geocoder;
	private Context mapActivityContext;
	private ArrayList<Marker> searchMarkers;
	private Button backBtn;
	private myMapClickListener mapListener;
	private LocationManager myLocManager;
	private String locationProvider;
	private Location currentLocation;
	// erase
	private LatLng latLng;
	private MarkerOptions markerOptions;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		placeItList = (ArrayList<PlaceIt>) PlaceIt.all();
		loadMarkers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mapListener = new myMapClickListener();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		geocoder = new Geocoder(this, Locale.ENGLISH);
		findBtn = (Button) findViewById(R.id.findBtn);
		searchMarkers = new ArrayList<Marker>();
		placeItMarkers = new ArrayList<Marker>();
		searchBar = (EditText) findViewById(R.id.SearchBar);
		backBtn = (Button) findViewById(R.id.BackBtn_Map);
		mapActivityContext = this;


		map.setOnMapClickListener(mapListener);
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
					markerOptions = new MarkerOptions();
					markerOptions.position(loc);
					markerOptions.title("" + latitude + ", " + longitude);

					searchMarkers.add(map.addMarker(markerOptions));
					map.animateCamera(CameraUpdateFactory
							.newLatLngZoom(loc, 13));
				}
				new GeocoderTask().execute(location);

			}

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
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

            // Enabling MyLocation Layer of Google Map
            map.setMyLocationEnabled(true);
 
            // Setting event handler for location change
            map.setOnMyLocationChangeListener(this);
            
    		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(UCSD_LATITUDE,
    				UCSD_LONGITUDE)));
    		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		}


	}

	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;

			try {
				addresses = geocoder.getFromLocationName(locationName[0], 10);
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
			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				searchMarkers.add(map.addMarker(markerOptions));

				if (i == 0)
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
							14));
			}
		}

	}

	private void loadMarkers() {
		erasePlaceItMarkers();
		placeItMarkers = new ArrayList<Marker>();
		for(int i = 0; i < placeItList.size();i++){
			Location loc = placeItList.get(i).getLocation();
			String title = placeItList.get(i).getTitle();
			MarkerOptions marker = new MarkerOptions();
			marker.position(new LatLng(loc.getLatitude(), loc.getLongitude()));
			marker.title(title);
			
			placeItMarkers.add(map.addMarker(marker));
		}
	}
	private void erasePlaceItMarkers(){
		for(int i = 0; i<placeItMarkers.size(); i++){
			placeItMarkers.get(i).remove();
		}
	}

	private class myMapClickListener implements OnMapClickListener {

		@Override
		public void onMapClick(LatLng loc) {
			Intent newPlaceItIntent = new Intent(mapActivityContext,
					NewPlaceitActivity.class);
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

	@Override
	public void onMyLocationChange(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		LatLng latLng = new LatLng(latitude, longitude);

		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		

		
	}

}

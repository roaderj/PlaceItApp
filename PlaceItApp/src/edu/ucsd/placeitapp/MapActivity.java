package edu.ucsd.placeitapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends Activity {

	private ArrayList<PlaceIt> placeItList;
	private Button findBtn;
	private GoogleMap map;
	private EditText searchBar;
	private Geocoder geocoder;
	private Context mapActivityContext;
	// erase
	private LatLng latLng;
	private MarkerOptions markerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		placeItList = (ArrayList<PlaceIt>) PlaceIt.all();
		geocoder = new Geocoder(this, Locale.ENGLISH);
		findBtn = (Button) findViewById(R.id.findBtn);

		searchBar = (EditText) findViewById(R.id.SearchBar);
		mapActivityContext = this;
		findBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String location = searchBar.getText().toString();

				if (location == null || location == "") {
					Toast.makeText(getBaseContext(),
							"Please enter a location to search",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					new GeocoderTask().execute(location);
				}

			}
		});

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
			}

			// Clears all the existing markers on the map
			map.clear();

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

				map.addMarker(markerOptions);

				if (i == 0)
					map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		}

	}

	private void loadMarkers() {

	}

	private class myMapClickListener implements OnMapClickListener {

		@Override
		public void onMapClick(LatLng loc) {
			Location location = new Location("map");
			location.setLatitude(loc.latitude);
			location.setLongitude(loc.longitude);
			Intent newPlaceItIntent = new Intent(mapActivityContext,
					NewPlaceitActivity.class);
			newPlaceItIntent.putExtra("location", location);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
}

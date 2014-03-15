package edu.ucsd.placeitapp;

import java.util.ArrayList;

import edu.ucsd.placeitapp.model.CategoryChecker;
import edu.ucsd.placeitapp.model.Place;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class PlaceItCheckService extends IntentService {

	private String spec;
	private boolean flag = false;
	
	public PlaceItCheckService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
	}

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
			ArrayList<Place> findPlaces = service.findPlaces(32.8741609,
					-117.2357297, places);
			for (int i = 0; i < findPlaces.size(); i++) {

				Place placeDetail = findPlaces.get(i);
			}
			return findPlaces;

		}

		@Override
		protected void onPostExecute(ArrayList<Place> result) {
			super.onPostExecute(result);
			flag = true;
		}

	}
}

package edu.ucsd.placeitapp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListTabFragment extends Fragment {

	List<PlaceIt> placeIts;

	ListView view;
	ArrayAdapter<PlaceIt> aAdapter;

	public ListTabFragment() {
		super();
	}

	public ListTabFragment(List<PlaceIt> data) {
		super();
		placeIts = data;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onStart() {
		view = (ListView) this.getView().findViewById(R.id.activeView);

		aAdapter = new PlaceItAdapter(this.getActivity(), placeIts);
		super.onStart();
		view.setAdapter(aAdapter);
		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {
				Intent viewDescription = new Intent(getActivity(), DescriptionActivity.class);
				viewDescription.putExtra(MainActivity.PLACEIT_ID, 
						aAdapter.getItem(position).getId());
				startActivity(viewDescription); 
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		return rootView;
	}

	private class PlaceItAdapter extends ArrayAdapter<PlaceIt> {
		Context context;
		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		int layout = R.layout.place_it_entry;

		public PlaceItAdapter(Context context, List<PlaceIt> objects) {
			super(context, R.layout.place_it_entry, objects);

			for (PlaceIt placeIt : objects)
				mIdMap.put(placeIt.getTitle(), placeIt.getId());

			this.layout = R.layout.place_it_entry;
			this.context = context;
		}

		@Override
		public long getItemId(int position) {
			PlaceIt item = getItem(position);
			return mIdMap.get(item.getTitle());
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(layout, parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.entryTitle);
			textView.setText(getItem(position).getTitle());

			return rowView;
		}
	}
}

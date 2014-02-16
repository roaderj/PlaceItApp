package edu.ucsd.placeitapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ListViewActivity extends FragmentActivity implements ActionBar.TabListener {

	List<PlaceIt> placeItList = new ArrayList<PlaceIt>();
	List<PlaceIt> activeList = new ArrayList<PlaceIt>();
	List<PlaceIt> pulledDownList = new ArrayList<PlaceIt>();
	Iterator<PlaceIt> pIterator;
	ListView activeView;
	ListView pulledDownView;

	private ViewPager viewPager;
	private ListViewAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Active ", "Pulled Down" };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new ListViewAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

		});
		

		for (int i = 0; i < 20; ++i) {
			PlaceIt x = new PlaceIt(i, new String("" + i), new String("" + i),
					null, null, false, -1, (i % 2 == 0));
			placeItList.add(x);
		}
		
		pIterator = placeItList.iterator();
		
		
		PlaceIt x;
		while (pIterator.hasNext()){
			
			x= pIterator.next();
			if (x.isEnabled()){
				activeList.add(x);
			}else{
				pulledDownList.add(x);
			}
		}
//		final ArrayAdapter<String> activeAdapter = new ArrayAdapter<String>(this,
//				R.layout.place_it_entry, xtra);
//
//		final ArrayAdapter<String> pulledDownAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, pulledDownListString);
//
//		
		//activeView.setAdapter(activeAdapter);
//		pulledDownView.setAdapter(pulledDownAdapter);
		
		
		
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	public void goToMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}

}


//NEW CLASS 
class PlaceItAdapter extends ArrayAdapter<String> {

	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	public PlaceItAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			mIdMap.put(objects.get(i), i);
		}
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	
}





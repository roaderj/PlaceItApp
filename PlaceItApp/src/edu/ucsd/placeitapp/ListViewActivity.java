package edu.ucsd.placeitapp;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;


public class ListViewActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private ListViewAdapter mAdapter;
	private ActionBar actionBar;
		
	List<PlaceIt> placeIts; 
	
	
	
	// Tab titles
	private String[] tabs = { "Active", "Pulled Down" };
	
	public  static final String FRAGMENT_ID = "edu.ucsd.placeitapp.fragment_id"; 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		
//		values = new ArrayList<PlaceIt>(); 
//		for (int i = 0; i < values1.length; ++i) {
//			PlaceIt x = new PlaceIt(values1[i], null, null); 
//			if ((i % 2) == 0 ) {
//				x.setEnabled(true);
//			}
//			x.
//			values.add(x); 
//			
//		}
		placeIts = PlaceIt.all(); 
		
		mAdapter = new ListViewAdapter(getSupportFragmentManager(), placeIts);

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		
		// Adding Tabs
		for (String tab : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab)
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
		
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//activeView.setAdapter(aAdapter); 
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}


	public void goToMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
	
	public void goBack(View view) {
		finish(); 
	}
	

}






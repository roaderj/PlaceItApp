package edu.ucsd.placeitapp;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;


public class ListViewActivity extends FragmentActivity implements ActionBar.TabListener, Observer {

	private Observable data = PlaceItList.getInstance();
	
	private ViewPager viewPager;
	private ListViewAdapter mAdapter;
	private ActionBar actionBar;
		
	List<PlaceIt> placeIts; 
	
	private String[] tabs = { "Active", "Pulled Down" };

	
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
		
		placeIts = PlaceItList.all(); 
		
		mAdapter = new ListViewAdapter(getSupportFragmentManager());
		data.addObserver(this); 


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
	public void onDestroy() {
		super.onDestroy(); 
		data.deleteObserver(this); 
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
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

	@Override
	public void update(Observable observable, Object data) {
		//Inefficient
		finish();
		startActivity(getIntent());
	}
	

}






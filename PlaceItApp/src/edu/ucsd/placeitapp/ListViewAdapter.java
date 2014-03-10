package edu.ucsd.placeitapp;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListViewAdapter extends FragmentPagerAdapter {

	List<PlaceIt> placeIts;

	public ListViewAdapter(FragmentManager fm) {
		super(fm);
		this.placeIts = PlaceItList.all();
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// active fragment activity
			ListTabFragment activeTab = new ListTabFragment(getActive());
			return activeTab;
		case 1:
			ListTabFragment pulledDownTab = new ListTabFragment(getPulledDown());
			return pulledDownTab;
		}

		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	private List<PlaceIt> getActive() {
		List<PlaceIt> activeList = new ArrayList<PlaceIt>(); 
		for (PlaceIt placeIt : placeIts) {
			if (placeIt.isActive())
				activeList.add(placeIt); 
		}
		return activeList; 
	}
	
	private List<PlaceIt> getPulledDown() {
		List<PlaceIt> pulledDown = new ArrayList<PlaceIt>(); 
		for (PlaceIt placeIt : placeIts) {
			if (!placeIt.isActive())
				pulledDown.add(placeIt); 
		}
		return pulledDown; 
	}
	
}

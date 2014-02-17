package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class MarkerTest extends AndroidTestCase {
	PlaceIt recurringPlaceIt;
	PlaceIt normalPlaceIt;

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));

	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void mapTouch(Location location) {} 
	
	private void createPlaceIt(PlaceIt placeIt) {}
	
	private void markerExists(PlaceIt placeIt) {}
	
	public void testMarkerOnMap() {}
	
	
	
	

}

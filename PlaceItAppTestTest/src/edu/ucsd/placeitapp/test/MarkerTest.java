package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;
import edu.ucsd.placeitapp.model.EntityDb;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;

public class MarkerTest extends AndroidTestCase {
	PlaceIt recurringPlaceIt;
	PlaceIt normalPlaceIt;

	public void setUp() {
		EntityDb.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));

	}

	public void tearDown() {
		EntityDb.getInstance().close();
	}
	
	private void mapTouch(Location location) {} 
	
	private void createPlaceIt(PlaceIt placeIt) {}
	
	private void markerExists(PlaceIt placeIt) {}
	
	public void testMarkerOnMap() {}
	
	
	
	

}

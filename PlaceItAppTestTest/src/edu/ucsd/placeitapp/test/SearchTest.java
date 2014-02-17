package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class SearchTest extends AndroidTestCase {

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));

	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void searchFor(String location) {}
	
	private void mapCameraAt(Location location) {
		
	}
	
	public void testSearchAddress() {}
	
	public void testSearchName() {}

}

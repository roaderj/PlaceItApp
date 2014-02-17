package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class MapTest extends AndroidTestCase {

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void gpsIs(boolean enabled) {
		
	}
	
	private void firstUse(boolean first) {
		
	}
	
	private void openMap() {
		
	}
	
	public void testFirstUseAndGpsOff() {}
	
	public void testUsedAndGpsOff() {}
	
	public void testUsedAndGpsOn() {}
	
}

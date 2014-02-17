package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class ListTest extends AndroidTestCase {

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));

	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void whenDiscarded() {}
	private void whenReposted() {}
	private void inActiveList(boolean active) {	}
	
	private void createPlaceIt(boolean active) {}
	
	public void testListDiscard() {}
	public void testListRepost() {}
	public void testInListActive() {}
	public void testInListPulledDown() {}
	

}

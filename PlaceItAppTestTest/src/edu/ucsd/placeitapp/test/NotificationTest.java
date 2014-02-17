package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class NotificationTest extends AndroidTestCase {
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
	
	private void enterLocation(Location location) {}
	private void notificationExists(boolean exists) {}
	private void isDeleted(boolean deleted) {}
	private void isActive(boolean active) {}
	private void whenDiscareded() {}
	private void whenReposted() {}
	private void whenClicked() {}
	private void whenIgnored() {}
	private void markerGone() {}
	
	public void testUserNotified() {}
	public void testDiscardAction() {}
	public void testRepostAction() {}
	public void testIgnoreAction() {}
	public void testClickAction() {}


}

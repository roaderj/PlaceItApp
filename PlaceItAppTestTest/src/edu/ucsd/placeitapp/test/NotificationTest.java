package edu.ucsd.placeitapp.test;

import java.util.ArrayList;
import java.util.List;

import com.robotium.solo.Solo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class NotificationTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo; 

	private PlaceIt placeIt; 
	
	private MockNotification notification;   
	
	public NotificationTest() {
		super("edu.ucsd.placeitapp", MainActivity.class);
		notification = new MockNotification(); 
	}
	
	
	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getActivity(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getActivity(),
				"test_"));
		
		solo = new Solo(getInstrumentation(), getActivity());
		placeIt = null; 

	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void enterLocation(Location location) {
		
	}
	
	private void givenPlaceIt(Location location) {
		//placeIt = new PlaceIt("Title", "Description", location);
		//PlaceItList.save(placeIt); 
		//placeIt.setAlarm(getActivity(), true);
	}
	
	private void givenPlaceItRecurring(Location location) {
		placeIt = new PlaceIt("Title", "Description", location, true, 1);
		PlaceItList.save(placeIt); 
		placeIt.setAlarm(getActivity(), true);
		
	}
	
	private void notificationExists(int pID) {
		assertTrue(MockNotification.containsPlaceIt(pID));
	}
	
	private void isDeleted(boolean deleted) {}
	private void isActive(boolean active) {}
	
	private void whenDiscarded() {
		PlaceItNotification noti = new PlaceItNotification(); 
		Intent intent = new Intent(); 
		intent.putExtra(MainActivity.PLACEIT_ID, placeIt.getId()); 
		intent.putExtra(PlaceItNotification.NOTIFICATION_TAG, edu.ucsd.placeitapp.R.string.notification_discard); 
		noti.onReceive(getActivity(), intent);
	}
	
	private void whenReposted() {
		PlaceItNotification noti = new PlaceItNotification(); 
		Intent intent = new Intent(); 
		intent.putExtra(MainActivity.PLACEIT_ID, placeIt.getId()); 
		intent.putExtra(PlaceItNotification.NOTIFICATION_TAG, edu.ucsd.placeitapp.R.string.notification_repost); 
		noti.onReceive(getActivity(), intent);
	}
	private void whenClicked() {}
	private void whenIgnored() {}
	private void markerGone() {}
	
	private void notification(int pID) {
		PlaceItNotification.notify(getActivity(), pID); 
	}
	
//	Scenario 1: User walks near a Place-It
//	Given that I am using my Place-It app
//	When I am near a Place-It
//	I will be notified the default android way by notification and vibrate. 
/*
	public void testUserNotified() {
		Location l = new Location("test"); 
		l.setLatitude(30); 
		l.setLongitude(-100); 
		
		//Given 
		//givenPlaceIt(l); 
		
		//When
		//enterLocation(l);
		
		//Then
		//notificationExists(placeIt.getId()); 
			
	}
*/

//	Scenario 3: User does not want to complete the Place-it task
//	Given that the user is at the Place-it location for a different reason
//	When the user taps on the discard Place-it button
//	Then the place-it will be deleted from the app. 
	
	public void testDiscardAction() {
		Location l = new Location("test"); 
		l.setLatitude(30); 
		l.setLongitude(-100); 
		
		/* Non-recurring place-it */ 
		
		//Given
		givenPlaceIt(l); 
		notification(placeIt.getId());
		
		//When
		whenDiscarded(); 
		
		//Then
		assertNull(PlaceItList.find(placeIt.getId())); 
		
		/* Recurring place-it */ 
		
		//Given
		givenPlaceItRecurring(l); 
		notification(placeIt.getId()); 
		
		//When
		whenDiscarded();
		
		//Then
		assertNotNull(PlaceItList.find(placeIt.getId())); 
		
	}
	
	/*
	public void testRepostAction() {}
	public void testIgnoreAction() {}
	public void testClickAction() {}

	*/ 
}

class MockNotification {
	
	public static boolean containsPlaceIt(int pID) {
		if (PlaceItNotification.placeIts.contains(pID))
			return true; 
		return false; 
	}
	
}


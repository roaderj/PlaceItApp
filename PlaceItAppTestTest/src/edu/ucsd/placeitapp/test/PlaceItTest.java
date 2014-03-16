package edu.ucsd.placeitapp.test;

import java.sql.Timestamp;
import java.util.List;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;
import edu.ucsd.placeitapp.model.EntityDb;
import edu.ucsd.placeitapp.model.LocationPlaceIt;
import edu.ucsd.placeitapp.model.PlaceIt;
import edu.ucsd.placeitapp.model.PlaceItList;

public class PlaceItTest extends AndroidTestCase {
	public void setUp() {
		EntityDb.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(), "test_")); 
	}

	public void tearDown() {
		EntityDb.getInstance().close();
	}

	public void testSave() {
		Location location = new Location("network");
		location.setLatitude(123);
		location.setLongitude(456);
		for (int i = 1; i <= 10; ++i) {
			PlaceIt placeIt = new LocationPlaceIt("Test Title", "Test Description",
					location);
			assertEquals(placeIt.getId(), 0);
			PlaceItList.save(placeIt);
			assertEquals(placeIt.getId(), i);
		}
	}

	public void testUpdate() {
		Location location = new Location("test");
		location.setLatitude(80085.0);
		location.setLongitude(8008135.0);
		LocationPlaceIt placeIt = new LocationPlaceIt("Title", "Description", location);
		PlaceItList.save(placeIt);
		
		int id = placeIt.getId();

		placeIt.setTitle("New Title");
		placeIt.setDescription("New Description");

		Location location2 = new Location("test");
		location2.setLatitude(80085.0);
		location2.setLongitude(8008135.0);
		placeIt.setLocation(location2);
		placeIt.setStartTime(new Timestamp(5));
		placeIt.setEnabled(true);
		placeIt.setRecurring(true);
		placeIt.setRecurringIntervalWeeks(5);

		PlaceItList.save(placeIt);
		assertEquals(placeIt.getId(), id);

		placeIt = (LocationPlaceIt) PlaceItList.find(id);
		assertEquals(placeIt.getTitle(), "New Title");
		assertEquals(placeIt.getDescription(), "New Description");
		assertEquals(placeIt.getLocation().getLatitude(), 80085.0);
		assertEquals(placeIt.getLocation().getLongitude(), 8008135.0);
//		assertEquals(placeIt.getStartTime().getTime(), 5);
		assertEquals(placeIt.getRecurringIntervalWeeks(), 5);
		assertEquals(placeIt.isRecurring(), true);
		assertEquals(placeIt.isEnabled(), true);
		assertEquals(placeIt.getId(), id);
	}

	public void testFind() {
		Location location = new Location("test");
		location.setLatitude(80085.0);
		location.setLongitude(8008135.0);
		LocationPlaceIt originalPlaceIt = new LocationPlaceIt("Test Title", "Test Description", location);
		originalPlaceIt.setStartTime(new Timestamp(System.currentTimeMillis()));
		
		for (int i = 1; i <= 10; ++i) {
			PlaceIt placeIt = new LocationPlaceIt("Test Title", "Test Description",
					originalPlaceIt.getLocation());
			placeIt.setStartTime(originalPlaceIt.getStartTime());
			PlaceItList.save(placeIt);
		}

		for (int i = 1; i <= 10; ++i) {
			LocationPlaceIt placeIt = (LocationPlaceIt) PlaceIt.find(i);
			assertEquals(placeIt.getId(), i);
			assertEquals(placeIt.getDescription(), originalPlaceIt.getDescription());
			assertEquals(placeIt.getRecurringIntervalWeeks(), originalPlaceIt.getRecurringIntervalWeeks());
			assertEquals(placeIt.getTitle(), originalPlaceIt.getTitle());
			assertEquals(placeIt.getLocation().getLatitude(), originalPlaceIt.getLocation().getLatitude());
			assertEquals(placeIt.getLocation().getLongitude(), originalPlaceIt.getLocation().getLongitude());
			//assertEquals(placeIt.getStartTime(), originalPlaceIt.getStartTime());
		}
	}

	public void testFindWithInvalidId() {
		PlaceIt placeIt = PlaceIt.find(-1);
		assertNull(placeIt);
	}

	public void testDelete() {
		Location location = new Location("test");
		location.setLatitude(80085.0);
		location.setLongitude(8008135.0);
		LocationPlaceIt originalPlaceIt = new LocationPlaceIt("Title", "Description", location);
		
		for (int i = 1; i <= 10; ++i) {
			PlaceIt placeIt = new LocationPlaceIt("Test Title", "Test Description",
					originalPlaceIt.getLocation());
			PlaceItList.save(placeIt);
		}

		for (int i = 1; i <= 10; ++i) {
			PlaceIt placeIt = PlaceIt.find(i);
			PlaceItList.delete(placeIt);

			assertEquals(placeIt.getId(), -1);

			placeIt = PlaceIt.find(i);
			assertNull(placeIt);
		}
	}

	public void testAll() {
		Location location = new Location("test");
		location.setLatitude(80085.0);
		location.setLongitude(8008135.0);
		LocationPlaceIt originalPlaceIt = new LocationPlaceIt("Title", "Description", location);
		
		List<PlaceIt> placeIts = PlaceIt.all();
		assertEquals(placeIts.size(), 0);

		for (int i = 1; i <= 10; ++i) {
			PlaceIt placeIt = new LocationPlaceIt("Test Title", "Test Description",
					originalPlaceIt.getLocation());
			PlaceItList.save(placeIt);
		}

		placeIts = PlaceItList.all();
		assertEquals(placeIts.size(), 10);
		for (int i = 0; i < placeIts.size(); ++i) {
			assertEquals(placeIts.get(i).getId(), i + 1);
		}
	}
}
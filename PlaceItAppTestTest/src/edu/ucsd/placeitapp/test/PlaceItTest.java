package edu.ucsd.placeitapp.test;

import java.sql.Timestamp;
import java.util.List;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;

public class PlaceItTest extends AndroidTestCase {
	PlaceIt placeIt;

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		Location location = new Location("network");
		location.setLatitude(123.4);
		location.setLongitude(567.8);
		placeIt = new PlaceIt("Test Title", "Test Description", location);
	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}

	public void testSave() {
		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = new PlaceIt("Test Title", "Test Description",
					this.placeIt.getLocation());
			assertEquals(placeIt.getId(), -1);
			placeIt.save();
			assertEquals(placeIt.getId(), i);
		}
	}

	public void testUpdate() {
		placeIt.save();
		int id = placeIt.getId();

		placeIt.setTitle("New Title");
		placeIt.setDescription("New Description");

		Location location = new Location("test");
		location.setLatitude(80085.0);
		location.setLongitude(8008135.0);
		placeIt.setLocation(location);
		placeIt.setStartTime(new Timestamp(5));
		placeIt.setEnabled(true);
		placeIt.setRecurring(true);
		placeIt.setRecurringIntervalWeeks(5);

		placeIt.save();
		assertEquals(placeIt.getId(), id);

		placeIt = PlaceIt.find(id);
		assertEquals(placeIt.getTitle(), "New Title");
		assertEquals(placeIt.getDescription(), "New Description");
		assertEquals(placeIt.getLocation().getLatitude(), 80085.0);
		assertEquals(placeIt.getLocation().getLongitude(), 8008135.0);
		assertEquals(placeIt.getStartTime().getTime(), 5);
		assertEquals(placeIt.getRecurringIntervalWeeks(), 5);
		assertEquals(placeIt.isRecurring(), true);
		assertEquals(placeIt.isEnabled(), true);
		assertEquals(placeIt.getId(), id);
	}

	public void testFind() {
		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = new PlaceIt("Test Title", "Test Description",
					this.placeIt.getLocation());
			placeIt.setStartTime(this.placeIt.getStartTime());
			placeIt.save();
		}

		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = PlaceIt.find(i);
			assertEquals(placeIt.getId(), i);
			assertEquals(placeIt.getDescription(), this.placeIt.getDescription());
			assertEquals(placeIt.getRecurringIntervalWeeks(), this.placeIt.getRecurringIntervalWeeks());
			assertEquals(placeIt.getTitle(), this.placeIt.getTitle());
			assertEquals(placeIt.getLocation().getLatitude(), this.placeIt.getLocation().getLatitude());
			assertEquals(placeIt.getLocation().getLongitude(), this.placeIt.getLocation().getLongitude());
			assertEquals(placeIt.getStartTime(), this.placeIt.getStartTime());
		}
	}

	public void testFindWithInvalidId() {
		PlaceIt placeIt = PlaceIt.find(-1);
		assertNull(placeIt);
	}

	public void testDelete() {
		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = new PlaceIt("Test Title", "Test Description",
					this.placeIt.getLocation());
			placeIt.save();
		}
		
		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = PlaceIt.find(i);
			placeIt.delete();
			
			assertEquals(placeIt.getId(), -1);
			
			placeIt = PlaceIt.find(i);
			assertNull(placeIt);
		}
	}

	public void testAll() {
		List<PlaceIt> placeIts = PlaceIt.all();
		assertEquals(placeIts.size(), 0);
		
		for (int i = 1; i <= 100; ++i) {
			PlaceIt placeIt = new PlaceIt("Test Title", "Test Description",
					this.placeIt.getLocation());
			placeIt.save();
		}
		
		placeIts = PlaceIt.all();
		assertEquals(placeIts.size(), 100);
		for (int i = 0; i < placeIts.size(); ++i) {
			assertEquals(placeIts.get(i).getId(), i + 1);
		}
	}
}

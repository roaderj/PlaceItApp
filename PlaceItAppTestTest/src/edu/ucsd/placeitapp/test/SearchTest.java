package edu.ucsd.placeitapp.test;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;
import edu.ucsd.placeitapp.R;
import android.app.Activity;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.widget.Button;
import android.widget.EditText;
import edu.ucsd.placeitapp.*;
import edu.ucsd.placeitapp.model.PlaceItDBHelper;
import edu.ucsd.placeitapp.model.PlaceItList;

public class SearchTest extends ActivityInstrumentationTestCase2<MapActivity> {
	
	public SearchTest() {
		super(MapActivity.class);	
	}

	private Solo solo;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getActivity().getBaseContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getActivity().getBaseContext(),
				"test_"));
		
		solo = new Solo(getInstrumentation(), getActivity());

	}
	@Override
	public void tearDown() throws Exception {
		PlaceItDBHelper.getInstance().close();
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
	private void searchFor(String location) {
		EditText searchBox = (EditText) solo.getView(R.id.SearchBar);
		solo.enterText(searchBox, location);		
		solo.clickOnButton("Find");
	}
	
	private void mapCameraAt(LatLng location) {
		
	}
	
	public void testSearchAddress() {
        solo.waitForActivity("MapActivity");
        solo.sleep(5000);
		searchFor("UCSD");
	}
	
	public void testSearchName() {}

}

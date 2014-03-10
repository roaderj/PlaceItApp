package edu.ucsd.placeitapp.test;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;
import edu.ucsd.placeitapp.model.PlaceItDBHelper;
import edu.ucsd.placeitapp.model.PlaceItList;

public class MapTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	public MapTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getActivity(),
				"test_"));	
		PlaceItList.setInstance(new RenamingDelegatingContext(getActivity(),
				"test_"));
		solo = new Solo(getInstrumentation(),getActivity());
	}	

	@Override
	public void tearDown() throws Exception {
		PlaceItDBHelper.getInstance().close();
		solo.finishOpenedActivities();
		super.tearDown();
	}
	// Given the user open the app
	// When the user click on the map button
	// Then the map show
	public void testOpenMap() {
		solo.assertCurrentActivity("wrong activity", MainActivity.class);
		solo.clickOnButton("Map");
		solo.assertCurrentActivity("wrong activity", MapActivity.class);
	}
	
}

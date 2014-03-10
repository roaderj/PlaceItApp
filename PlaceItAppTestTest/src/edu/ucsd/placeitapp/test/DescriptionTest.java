package edu.ucsd.placeitapp.test;

import android.location.Location;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import edu.ucsd.placeitapp.*;
import edu.ucsd.placeitapp.model.PlaceItDBHelper;
import edu.ucsd.placeitapp.model.PlaceItList;

public class DescriptionTest extends AndroidTestCase {

	public void setUp() {
		PlaceItDBHelper.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));
		PlaceItList.setInstance(new RenamingDelegatingContext(getContext(),
				"test_"));

	}

	public void tearDown() {
		PlaceItDBHelper.getInstance().close();
	}
	
	private void mapClick() {
	}
	
	private void listClick() {}
	
	private void descriptionMatches() {}
	
	private void initialView() {}
	
	public void testViewFromList() {
		
	}
	
	public void testViewFromMap() {}

}

package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;

import android.content.Context;
import android.location.Location;

/* Needs PlaceItDBHelper to be fully functional */ 

public class CategoricalPlaceIt extends PlaceIt {
	
	private String[] tags; 
	
	public CategoricalPlaceIt(int id, String title, String description, String[] tags,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		
		//super needs to be changed in the future
		super(id, title, description, new Location("location"), startTime, isRecurring, recurringIntervalWeeks, isEnabled);
		
		this.tags = tags; 
	}
	
	public CategoricalPlaceIt() {
		this(0, "", "", null, null, false, 0, false);
	}
		
	public void setTags(String[] tags) {
		this.tags = tags; 
	}
	
	public String[] getTags() {
		return this.tags; 
	}
	
	@Override
	public void trackLocation(Context context, boolean enable) {
		//TODO 
	}
	
	public String toString() {
		String fullDescription = new String(super.toString() 
				+ "\n\n\nCategories: " 
				+ "\n" + tags[0]
				+ ", " + tags[1]
				+ ", " + tags[2]);
				
		return fullDescription;
	}

}

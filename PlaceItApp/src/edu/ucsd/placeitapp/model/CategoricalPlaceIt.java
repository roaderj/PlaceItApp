package edu.ucsd.placeitapp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

/* Needs PlaceItDBHelper to be fully functional */ 

public class CategoricalPlaceIt extends PlaceIt {
	
	private String address;
	private List<String> tags; 
	public static final String KEY = "CategoricalPlaceIt";
	
	
	public CategoricalPlaceIt(int id, String title, String description, List<String> tags,
			Timestamp startTime, boolean isRecurring,
			int recurringIntervalWeeks, boolean isEnabled) {
		
		//super needs to be changed in the future
		super(id, title, description, startTime, isRecurring, recurringIntervalWeeks, isEnabled);
		
		this.tags = tags; 
		this.key = KEY;
	}
	
	public CategoricalPlaceIt() {
		this(0, "", "", new ArrayList<String>(), null, false, 0, false);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	
	public List<String> getTags() {
		return this.tags; 
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	@Override
	public void trackLocation(Context context, boolean enable) {
		setEnabled(enable);
	}
	
	public String toString() {
		String fullDescription = new String(super.toString() 
				+ "\n\n\nCategories: " 
				+ "\n");
		
		for (String tag : getTags())
			fullDescription += tag + "\n";
				
		return fullDescription;
	}

}

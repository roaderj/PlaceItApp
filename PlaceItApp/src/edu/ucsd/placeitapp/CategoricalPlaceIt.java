package edu.ucsd.placeitapp;

public class CategoricalPlaceIt { //extends PlaceIt {
	
	public CategoricalPlaceIt(String title, String description, String[] tags,
			boolean isRecurring, int recurringIntervalWeeks) {
		//TODO
	}
	
	public CategoricalPlaceIt(String title, String description, String[] tags) {
		this(title, description, tags, false, -1); 
	}
	
	public void trackLocation() {
		//TODO 
	}
	
	public String toString() {
		//TODO
		return super.toString(); //temporary
	}

}

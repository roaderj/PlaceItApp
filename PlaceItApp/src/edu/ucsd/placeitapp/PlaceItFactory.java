package edu.ucsd.placeitapp;

public class PlaceItFactory {
	public enum PlaceIts {
		LOCATION, CATEGORICAL, LOCATION_RECURRING, CATEGORICAL_RECURRING;
	}

	public PlaceIt create(PlaceIts p) {
		switch (p) {
		case LOCATION:
			// TODO
			break;
		case CATEGORICAL:
			// TODO
			break;
		case LOCATION_RECURRING: 
			// TODO
			break; 
		case CATEGORICAL_RECURRING: 
			// TODO
			break; 
		}
		return null;
	}
}

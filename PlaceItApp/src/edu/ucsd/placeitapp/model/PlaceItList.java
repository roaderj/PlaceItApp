package edu.ucsd.placeitapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.ucsd.placeitapp.PlaceItNotification;
import android.content.Context;

public class PlaceItList extends Observable {

	public static List<PlaceIt> placeIts;
	public static PlaceItList instance;

	public static Context context;
	List<Observer> observers;

	public PlaceItList(Context c) {
		context = c;
		this.observers = new ArrayList<Observer>();
		updateList();
	}

	@Override
	public void notifyObservers() {
		for (Observer o : observers)
			o.update(this, null);
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		observers.remove(o);
	}

	public static void setInstance(Context c) {
		instance = new PlaceItList(c);
	}

	public static PlaceItList getInstance() {
		return instance;
	}

	public static void save(PlaceIt p) {
		int pID = PlaceItDb.getInstance().save(p);
		p.setId(pID);
		PlaceItDb.getInstance().save(p);

		
		updateList();
		getInstance().notifyObservers();

	}

	public static void delete(PlaceIt p) {
		p.trackLocation(context, false);
		p.setAlarm(context, false);
		PlaceItNotification.cancel(context, p.getId()); 
		PlaceItDb.getInstance().delete(p);
		p.setId(-1);

		updateList();
		getInstance().notifyObservers();
	}

	public static PlaceIt find(int id) {
		return PlaceItDb.getInstance().find(id);
	}

	public static List<PlaceIt> all() {
		return placeIts;
	}
	
	public static List<PlaceIt> all(String key) {
		return PlaceItDb.getInstance().all(key);
	}

	private static void updateList() {
		placeIts = PlaceItDb.getInstance().all();
	}

}

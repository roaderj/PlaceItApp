package edu.ucsd.placeitapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
		int pID = PlaceItDBHelper.getInstance().save(p);
		p.setId(pID);

		updateList();
		getInstance().notifyObservers();

	}

	public static void delete(PlaceIt p) {
		p.trackLocation(context, false);
		p.setAlarm(context, false);
		PlaceItNotification.cancel(context, p.getId()); 
		PlaceItDBHelper.getInstance().delete(p);
		p.setId(-1);

		updateList();
		getInstance().notifyObservers();
	}

	public static PlaceIt find(int id) {
		return PlaceItDBHelper.getInstance().find(id);
	}

	public static List<PlaceIt> all() {
		return placeIts;
	}

	private static void updateList() {
		placeIts = PlaceItDBHelper.getInstance().all();
	}

}

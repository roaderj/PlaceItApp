package edu.ucsd.placeitserver;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
/**
 * This class handles CRUD operations related to PlaceIt entity.
 */
public class PlaceIt {
	 /**
	   * Create or update PlaceIt for a particular user.
	   * @param identifier
	   *          : placeit name
	   * @param username
	   *          : user name
	   * @param data
	   *          : placeit data
	   * @return
	   */
	public static Entity createOrUpdatePlaceIt(String identifier,
			String username, String placeitdata) {

		Entity user = User.getUser(username);
		Entity placeit = getSinglePlaceIt(identifier);
		if (placeit == null) {
			// Create new entity
			placeit = new Entity("PlaceIt", identifier);
			placeit.setProperty("User", username);
			placeit.setProperty("Data", placeitdata);
		} else {
			// Update old entity's data
			placeit.setProperty("Data", placeitdata);
		}

		Util.persistEntity(placeit);
		return placeit;

	}
	 /**
	   * get all the placeits in the list
	   * @return all the placeits
	   */
	public static Iterable<Entity> getAllPlaceIt() {
		Iterable<Entity> entities = Util.listEntities("PlaceIt", null, null);
		return entities;
	}
	 /**
	   * Get the placeit by id
	   * @param identifier
	   *          : placeit id
	   * @return placeit
	   */
	public static Iterable<Entity> getPlaceIt(String identifier) {
		Iterable<Entity> entities = Util.listEntities("PlaceIt", "name",
				identifier);
		return entities;
	}
	 /**
	   * Get all the placeits for a user
	   * @param userName
	   *          : user name
	   * @return: all placeits of the user
	   */
	public static Iterable<Entity> getPlaceItForUser(String userName) {
		return Util.listEntities("PlaceIt", "User", userName);
	}
	 /**
	   * get placeit by id
	   * @param identifier: id
	   * @return  placeit
	   */
	public static Entity getSinglePlaceIt(String identifier) {
		Iterable<Entity> entities = getPlaceIt(identifier);
		for (Entity first : entities)
			return first;
		return null;
	}
   /**
    * delete placeit by id
    * @param identifier: id
    */
	public static String deletePlaceIt(String identifier) {
		Key key = KeyFactory.createKey("PlaceIt", identifier);

		Util.deleteEntity(key);
		return "Deletion was successful";
	}
}

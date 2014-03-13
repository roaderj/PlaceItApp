package edu.ucsd.placeitserver;

import java.util.List;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

/**
 * This class handles all the CRUD operations related to
 * User entity.
 *
 */
public class User {

  /**
   */
  public static void createUser(String username, String password) {
    Entity user = getUser(username);
  	if (user == null) {
  	  user = new Entity("User", username);
  	  user.setProperty("password", password);
  	  Util.persistEntity(user);
  	}
  }

  /**
   */
  public static Iterable<Entity> getAllUsers(String kind) {
    return Util.listEntities(kind, null, null);
  }

  /**
   */
  public static Entity getUser(String name) {
  	Key key = KeyFactory.createKey("User", name);
  	return Util.findEntity(key);
  }
  
  public static List<Entity> getPlaceIt(String name) {
	Query query = new Query();
	Key parentKey = KeyFactory.createKey("User", name);
	query.setAncestor(parentKey);
	query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.GREATER_THAN, parentKey);
	List<Entity> results = Util.getDatastoreServiceInstance()
	  	.prepare(query).asList(FetchOptions.Builder.withDefaults());
	return results;
  }
  
  /**
   */
  public static String deleteUser(String userKey) {
	  Key key = KeyFactory.createKey("User", userKey);	   
	  List<Entity> placeit = getPlaceIt(userKey);	  
	  if (!placeit.isEmpty()){
	      return "Cannot delete, as there are placeit associated with this user.";	      
	  }	    

	  Util.deleteEntity(key);
	  return "Deletion was successful";
	  
  }
}

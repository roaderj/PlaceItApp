package edu.ucsd.placeitserver;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
  
  /**
   */
  public static String deleteUser(String userKey) {
	  Key key = KeyFactory.createKey("User", userKey);	   
	  

	  Util.deleteEntity(key);
	  return "Deletion was successful";
	  
  }
}

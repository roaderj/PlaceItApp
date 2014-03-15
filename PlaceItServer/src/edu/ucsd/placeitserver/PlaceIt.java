package edu.ucsd.placeitserver;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class PlaceIt {

  public static Entity createOrUpdatePlaceIt(String userName, String placeitName,String placeitdata) {
    
	  Entity user = User.getUser(userName);
	  Entity placeit = getSinglePlaceIt(placeitName);
	  if(placeit == null){
	   placeit = new Entity("PlaceIt",user.getKey());
	   placeit.setProperty("Name", placeitName);
	   placeit.setProperty("User", userName);
	   placeit.setProperty("Data", placeitdata);
	  } else{
	   if (placeitdata != null && !"".equals(placeitdata)) {
	   placeit.setProperty("Data", placeitdata);
	   } 
	  }
	  Util.persistEntity(placeit);
	  return placeit;

  }

  public static Iterable<Entity> getAllPlaceIt() {
  	Iterable<Entity> entities = Util.listEntities("PlaceIt", null, null);
  	return entities;
  }

  public static Iterable<Entity> getPlaceIt(String placeitName) {
  	Iterable<Entity> entities = Util.listEntities("PlaceIt", "Name", placeitName);
  	return entities;
  }

  public static Iterable<Entity> getPlaceItForUser(String kind, String userName) {
    Key ancestorKey = KeyFactory.createKey("User", userName);
    return Util.listChildren("PlaceIt", ancestorKey);
  }

  public static Entity getSinglePlaceIt(String placeitName) {
    
	  Query query = new Query("PlaceIt");
	  query.addFilter("Name", FilterOperator.EQUAL, placeitName);
	  List<Entity> results = 
	  Util.getDatastoreServiceInstance().prepare(query).asList(FetchOptions.Builder.withDefaults());
	  if (!results.isEmpty()) {
	   return (Entity)results.remove(0);
	  }
	  return null;
  }
  
  public static String deletePlaceIt(String placeitKey)
  {
	  Entity entity = getSinglePlaceIt(placeitKey); 
	  if(entity != null){
	   Util.deleteEntity(entity.getKey());
	   return("PlaceIt deleted successfully.");
	  } else
	   return("PlaceIt not found"); 
  }
}

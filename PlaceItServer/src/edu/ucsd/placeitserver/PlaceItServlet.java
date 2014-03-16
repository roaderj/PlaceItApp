package edu.ucsd.placeitserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
/**
 * This servlet responds to the request corresponding to PlaceIt data. 
 */
@SuppressWarnings("serial")
public class PlaceItServlet extends BaseServlet {

	private static final Logger logger = Logger.getLogger(PlaceItServlet.class
			.getCanonicalName());
	 /**
	   * Searches for the entity based on the search criteria and returns result in
	   */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		super.doGet(req, resp);
		logger.log(Level.INFO, "Obtaining PlaceIt listing");
		
		String searchByUser = req.getParameter("username"); //Get listings for a user
		String searchForSingle = req.getParameter("identifier"); //Get a single placeit
		
		PrintWriter out = resp.getWriter();
		Iterable<Entity> entities; 
		if (searchByUser == null || searchByUser == "") {
			if (searchForSingle == null || searchForSingle == "") {
				//Get all placeits
				entities = PlaceIt.getAllPlaceIt(); 
			} else {
				//Get the single placeit requested
				entities = PlaceIt.getPlaceIt(searchForSingle); 
			}
		} else {
			//Get the user's placeits 
			entities = PlaceIt.getPlaceItForUser(searchByUser);
		}
		
		out.println(Util.writeJSON(entities));
	}
	/**
	  * Creates entity and persists the same
	  */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.log(Level.INFO, "Creating PlaceIt");
		String identifier = req.getParameter("identifier");
		String userName = req.getParameter("User");
		String placeitdata = req.getParameter("Data");
		PlaceIt.createOrUpdatePlaceIt(identifier, userName, placeitdata);
	}
	/**
	  * Delete the entity
	  */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String placeitKey = req.getParameter("identifier");
		PrintWriter out = resp.getWriter();
		try {
			out.println(PlaceIt.deletePlaceIt(placeitKey));
		} catch (Exception e) {
			out.println(Util.getErrorMessage(e));
		}

	}
	/**
	  * Redirects to delete or insert entity based on the action in the HTTP
	  * request.
	  */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action.equalsIgnoreCase("delete")) {
			doDelete(req, resp);
			return;
		} else if (action.equalsIgnoreCase("put")) {
			doPut(req, resp);
			return;
		}
	}
}
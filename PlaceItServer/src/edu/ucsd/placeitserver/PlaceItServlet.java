
package edu.ucsd.placeitserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class PlaceItServlet extends BaseServlet {

  private static final Logger logger = Logger.getLogger(PlaceItServlet.class.getCanonicalName());

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    super.doGet(req, resp);
    logger.log(Level.INFO, "Obtaining PlaceIt listing");
    String searchBy = req.getParameter("placeit-searchby");
    String searchFor = req.getParameter("q");
    PrintWriter out = resp.getWriter();
    if (searchFor == null || searchFor.equals("")) {
      Iterable<Entity> entities = PlaceIt.getAllPlaceIt();
      out.println(Util.writeJSON(entities));
    } else if (searchBy == null || searchBy.equals("Name")) {
      Iterable<Entity> entities = PlaceIt.getPlaceIt(searchFor);
      out.println(Util.writeJSON(entities));
    } else if (searchBy != null && searchBy.equals("User")) {
      Iterable<Entity> entities = PlaceIt.getPlaceItForUser("PlaceIt", searchFor);
      out.println(Util.writeJSON(entities));
    }
  }

  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.log(Level.INFO, "Creating PlaceIt");
    String placeitName = req.getParameter("Name");
    String userName = req.getParameter("User");
    String placeitdata = req.getParameter("Data");
    PlaceIt.createOrUpdatePlaceIt(userName, placeitName, placeitdata);
  }

  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String placeitKey = req.getParameter("id");
    PrintWriter out = resp.getWriter();
    try{      
      out.println(PlaceIt.deletePlaceIt(placeitKey));
    } catch(Exception e) {
      out.println(Util.getErrorMessage(e));
    }      
    
  }

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
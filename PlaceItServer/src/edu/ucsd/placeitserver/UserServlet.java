package edu.ucsd.placeitserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

/**
 * Servlet for the User
 */
@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {

  private static final Logger logger = Logger.getLogger(UserServlet.class.getCanonicalName());
  
  /**
   * Get the entities in JSON format.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
    logger.log(Level.INFO, "Obtaining users");

	super.doGet(req, resp);

    String searchFor = req.getParameter("q");
    PrintWriter out = resp.getWriter();
    Iterable<Entity> entities = null;
    if (searchFor == null || searchFor.equals("") || searchFor == "*") {
      entities = User.getAllUsers("User"); 
      out.println(Util.writeJSON(entities));
    } else {
      Entity user = User.getUser(searchFor); 
      if (user != null) {
        Set<Entity> result = new HashSet<Entity>();
        result.add(user);
        out.println(Util.writeJSON(result));
      }
    }
  }

  /**
   * Create the entity and persist it.
   */
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.log(Level.INFO, "Creating Product");
    PrintWriter out = resp.getWriter();

    String username = req.getParameter("name");
    String password = req.getParameter("password");
    try {
      User.createUser(username, password);
    } catch (Exception e) {
      String msg = Util.getErrorMessage(e);
      out.print(msg);
    }
  }

  /**
   * Delete the product entity
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String user = req.getParameter("name");
    PrintWriter out = resp.getWriter();
    try{    	
    	out.println(User.deleteUser(user));
    } catch(Exception e) {
    	out.println(Util.getErrorMessage(e));
    }    
  }

  /**
   * Redirect the call to doDelete or doPut method
   */
  @Override
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
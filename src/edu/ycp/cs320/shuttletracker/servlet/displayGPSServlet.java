package edu.ycp.cs320.shuttletracker.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.shuttletracker.controller.ShuttleTrackerController;
import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

public class displayGPSServlet extends HttpServlet  {
private static final long serialVersionUID = 1L;


	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("displayGPS Servlet: doGet");
		
		ShuttleTracker model = new ShuttleTracker();
		ShuttleTrackerController controller = new ShuttleTrackerController();
		controller.setModel(model);
		controller.StartTracker();
		controller.getLastLocations();
		
		// Add result objects as request attributes
				req.setAttribute("latitude", 123.123 );
				req.setAttribute("longitude", controller.getLongitude() );
				req.setAttribute("id", controller.getIds());
				req.setAttribute("locations", controller.getLastLocations());
		
				//System.out.println("Locations: (SERVLET)  " + controller.getLastLocations());
		req.getRequestDispatcher("/_view/displayGPS.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ShuttleTracker model = new ShuttleTracker();
		ShuttleTrackerController controller = new ShuttleTrackerController();
		controller.setModel(model);
		controller.StartTracker();
		
		System.out.println("displayGPS Servlet: doPost");
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;				
		
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("latitude", controller.getLatitude() );
		req.setAttribute("longitude", controller.getLongitude() );
		req.setAttribute("id", controller.getIds());
		req.setAttribute("locations", controller.getLastLocations());
		
		//System.out.println("LAT: " +  model.getLatitude() + " LON: " + model.getLongitude() + " Controller: " + controller.getLatitude());
		
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/displayGPS.jsp").forward(req, resp);
	}
	
	/*
	private Double getDoubleFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return Double.parseDouble(s);
		}
	}*/
}

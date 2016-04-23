package edu.ycp.cs320.shuttletracker.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.shuttletracker.controller.ShuttleTrackerController;
import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

public class ShuttleTrackerServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/shuttleTracker.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String message = "Data Accepted";
		
		try{
			ShuttleTracker model = new ShuttleTracker();
			ShuttleTrackerController controller = new ShuttleTrackerController();
			controller.setModel(model);
			controller.getData();
			
			// Reconstruct current ShuttleTracker model object
			Double curLat = getDouble(req, "latitude");
			Double curLong = getDouble(req, "longitude");
			String mac = req.getParameter("mac");
			int id = getInteger(req, "id");	
			
			// Check for max shuttle ID ( Error prevention )
			if( id > controller.getMaxID()+1 )
			{
				id = controller.getMaxID()+1;
				message = "Error: ID changed to: " + id;				
			}
			
			// Set Model
			model.setId(id);
			model.setLatitude(curLat);
			model.setLongitude(curLong);
			model.setMAC(mac);
			model.setComment("Data from Pi " + id );
			// Get time
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			model.setTime(dateFormat.format(date));
			
			// Update controller data
			controller.updateModel(id, curLat, curLong, mac, dateFormat.format(date));
			
			
			System.out.println("\nUser entered: \nID: " + model.getId() + " MAC: " + model.getMAC() + " Latitude: " + model.getLatitude() + " Longitude: " + model.getLongitude() + " Time: " + dateFormat.format(date));
		} catch (NumberFormatException e) {
			message = "Invalid Data";
			System.out.println("\nInvalid data.");			
		}
		
		// Add parameters as request attributes
		req.setAttribute("id", req.getParameter("id"));
		req.setAttribute("mac", req.getParameter("mac"));
		req.setAttribute("longitude", req.getParameter("longitude"));
		req.setAttribute("latitude", req.getParameter("latitude"));
				
		// Add result objects as request attributes
		req.setAttribute("message", message);
				
		
		req.getRequestDispatcher("/_view/shuttleTracker.jsp").forward(req, resp);
	}

	private int getInteger(HttpServletRequest req, String name) {
		return Integer.parseInt(req.getParameter(name));
	}
	
	private double getDouble(HttpServletRequest req, String name)
	{
		return Double.parseDouble(req.getParameter(name));
	}
}

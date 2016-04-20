package edu.ycp.cs320.shuttletracker.servlet.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

public class AddDataAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doRequest(req, resp);
	}

	private void doRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Read client's model
		int id = -1;
		Double longitude = getDouble(req, "longitude");
		Double latitude = getDouble(req, "latitude");
		id = getInteger(req, "id");
		String mac = req.getParameter("mac");
		
		if (id == -1) {
			badRequest("Invalid ID", resp);
			return;
		}
		System.out.println("Doing work in AddData");
		ShuttleTracker model = new ShuttleTracker();
		model.setLatitude(latitude);
		model.setLongitude(longitude);
		model.setId(id);
		model.setMAC(mac);
		
	
				String result = "ID: " + id + "MAC: " + mac + "Long: " + longitude + "Lat: " + latitude;
				
				// Send back a response
				resp.setContentType("text/plain");
				resp.getWriter().println(result);
		
		
		// Send back updated model to client
		/*
		resp.setContentType("application/json");
		resp.getWriter().println(
				"{ \"min\": " + model.getMin() +
				", \"max\": " + model.getMax() +
				", \"guess\": " + guess + "}" );
				*/
	}

	private Integer getInteger(HttpServletRequest req, String name) {
		String val = req.getParameter(name);
		if (val == null) {
			return null;
		}
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private Double getDouble(HttpServletRequest req, String name) {
		String val = req.getParameter(name);
		if( val == null ) {
			return 0.0;
		}
		try {
			return Double.parseDouble(val);
		} catch (NumberFormatException e)
		{
			return null;
		}
		
		
	}

	private void badRequest(String message, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		resp.getWriter().println(message);
	}

}

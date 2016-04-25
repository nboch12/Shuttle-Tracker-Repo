package edu.ycp.cs320.shuttletracker.controller;

import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Queue;
import java.util.Stack;

public class ShuttleTrackerController {
	private ShuttleTracker model;
	private Hashtable<Integer, Stack<String>> data = new Hashtable<Integer, Stack<String>>();
	
	// SQL String Variables
	private int id;
	private double lat;
	private double lon;
	private String mac;
	private String time;
	
	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "root";
	
	/** The name of the table we are testing with */
	private final String tableName = "pi_data";
	
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shuttle_data?autoReconnect=true&useSSL=false", connectionProps);

		return conn;
	}
	
	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	
	public void setModel(ShuttleTracker model) {
		this.model = model;
	}	
	
	public void StartTracker()
	{
		model.setId(0);
		// Default coordinates for York College
		model.setLatitude(39.9455);
		model.setLongitude(-76.7321);
		model.setMAC("00:00:00:00:00:00");
		model.setComment("Starting up shuttle tracker");
		
		// Get time
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		model.setTime(dateFormat.format(date));
		
		// Load initial data from database
		getData();
	}
	
	public void updateModel(int id, Double lat, Double lon, String mac, String time)
	{
		model.setId(id);
		model.setLatitude(lat);
		model.setLongitude(lon);
		model.setMAC(mac);
		model.setTime(time);
		
		outputToDatabase();
		
		//System.out.println("updateModel: " + id + " " + lat + " " + lon + " " + mac + " " + time);
	}
	
	
	
	public void outputToDatabase()
	{
		
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("\nConnected to database. Sending data");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
		
		// Insert into table
		
		PreparedStatement update_pidata = null;
		
		String updateString = "INSERT INTO pi_data VALUES(?,?,?,?,?,?)";
		
		try {
	        conn.setAutoCommit(false);
	        update_pidata = conn.prepareStatement(updateString);
	        
	        update_pidata.setInt(1, model.getId());
	        update_pidata.setDouble(2, model.getLatitude());
	        update_pidata.setDouble(3, model.getLongitude());
	        update_pidata.setString(4, model.getMAC());
	        update_pidata.setString(5, model.getTime());
	        update_pidata.setString(6, model.getComment());
	        
	        update_pidata.executeUpdate();
	        conn.commit();
	        conn.close();
	    } catch (SQLException e ) {
	    	System.out.println("ERROR: Could not insert records");
			e.printStackTrace();
			return;
	    }
		
		/* Old code to write to CSV
		try{
			FileWriter fw = new FileWriter(fileName, true);
			fw.write(model.getId() + "," + model.getLatitude() + "," + model.getLongitude() + "," + model.getMAC() + "," + model.getTime() + "," + model.getComment() + "\n");		
			fw.close();
		} catch( IOException e )
		{
			e.printStackTrace();
		}	*/	
	}
	
	public void getData()
	{
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("\nConnected to database. Getting data");
		} catch (SQLException e) {
			System.out.println("\nERROR: Could not connect to the database\n");
			e.printStackTrace();
			return;
		}
		
		// Get data from MySQL and store in hashtable
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM pi_data";
			
			ResultSet rs = st.executeQuery(sql);
					
			while( rs.next() )
			{
				// Parse set into variables
				id = rs.getInt("PI_ID");				
				lat = rs.getDouble("LATITUDE");
				lon = rs.getDouble("LONGITUDE");
				mac = rs.getString("MAC_ADDR");
				time = rs.getString("DATE");
				
				// Add new entry to queue
				Stack<String> tempQ = new Stack();
				
				// Use previous queue 
				if( data.get(id) != null )
				{
					tempQ = data.get(id);
					// If entry doesn't exist already, add it
					if( !tempQ.contains(time) )
					{
						tempQ.add(id + "," + lat + "," + lon + "," + mac + "," + time);
						data.put(id, tempQ );
					} else {
						System.out.println("Entry exists. Wasn't added");
					}
				} else
				{
					// If the queue is empty, add entry
					tempQ.add(id + "," + lat + "," + lon + "," + mac + "," + time);
					data.put(id, tempQ );
				}												
			}
			
			// Output entire ResultSet 
			//System.out.println(data);
			
		} catch ( SQLException e ) {
			System.out.println("ERROR: Could not pull records");
			e.printStackTrace();
			return;
		}		
	}
	
	// Returns number of keys in data hashtable. This is used to ensure new PI_IDs are added without gaps
	public int getMaxID()
	{
		System.out.println("GetMaxID: " + data.size());
		return data.size();
	}
	
	// Finds the last locations for each shuttle by looking through the "data" hashtable
	public String getLastLocations()
	{		
		//String[] locations = new String[10];
		//locations[0] = "";
		String locations="";
		String temp;
		
		
		for( int i=1; i<=data.size(); i++)
		{
			//System.out.println("For Loop: " + i + " Data: " + data.get(i));
			if( !data.get(i).peek().isEmpty() )
			{
				// Get last entry from hashtable key and parse it to only include Lat/Lon
				temp = data.get(i).peek();
				
				 // Splits data row into separate entries
				 String[] parsedLine = temp.split(",");		
				 
				 locations += "\""+parsedLine[1]+","+parsedLine[2] + "\",";				 
				 
			} else
			{
				System.out.println("Null entry avoided");
			}			
		}		
		
		// Remove last comma
		locations = locations.substring(0, locations.length()-1);
		
		System.out.println("Locations: (CONTROLLER) "+locations);
		return locations;		
	}	
}

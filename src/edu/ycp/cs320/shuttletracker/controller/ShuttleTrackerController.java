package edu.ycp.cs320.shuttletracker.controller;

import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ShuttleTrackerController {
	private ShuttleTracker model;
	private String fileName = "pidata.csv";
	private String parsedLine[] = {"0","0.0","0.0","00:00:00:00:00","test"};
	
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
		
		try{
			File f = new File(fileName);
			if(!f.exists()) {
				outputCSV();
			}
			FileReader fr = new FileReader(fileName);			
			BufferedReader br = new BufferedReader(fr);
			if( br.readLine() == null )
				outputCSV();	
			br.close();
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	public void updateModel(int id, Double lat, Double lon, String mac, String time)
	{
		model.setId(id);
		model.setLatitude(lat);
		model.setLongitude(lon);
		model.setMAC(mac);
		model.setTime(time);	
		
		
		outputCSV();
		
		System.out.println("updateModel: " + id + " " + lat + " " + lon + " " + mac + " " + time);
	}
	
	
	
	public void outputCSV()
	{
		
		
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
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
	    } catch (SQLException e ) {
	    	System.out.println("ERROR: Could not insert records");
			e.printStackTrace();
			return;
	    }
		
		
		/*try {
			Statement stmt = null;
			//STEP 4: Execute a query
		      System.out.println("Inserting records into the table...");
		      stmt = conn.createStatement();
		      
		      String sql = "INSERT INTO pi_data" +
		                   "VALUES (" + model.getId() + "," + model.getLatitude() + "," + model.getLongitude() + "," + model.getMAC() + "," + model.getTime() + "," + model.getComment()+ ");";
		      
		      
		      
		      this.executeUpdate(conn, sql);
		      System.out.println("Inserted records into the table...");

			
	    } catch (SQLException e) {
			System.out.println("ERROR: Could not insert records");
			e.printStackTrace();
			return;
		}*/
		
		try{
			FileWriter fw = new FileWriter(fileName, true);
			fw.write(model.getId() + "," + model.getLatitude() + "," + model.getLongitude() + "," + model.getMAC() + "," + model.getTime() + "," + model.getComment() + "\n");		
			fw.close();
		} catch( IOException e )
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void parseLastLine()
	{
		try{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String lastline = "";
			
			while( (line = br.readLine()) != null )
			{
				lastline = line;
			}
			
			
			
			// Parse line into separate array entries
			parsedLine = lastline.split(",");
			
			br.close();
			
		} catch ( IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public int getId()
	{		
		parseLastLine();
		return Integer.parseInt(parsedLine[0]);
	}
	
	public double getLatitude()
	{
		parseLastLine();
		System.out.println("\nGetLatitude: " + parsedLine[1]);
		//return model.getLatitude();	
		return Double.parseDouble(parsedLine[1]);
	}
	
	public double getLongitude()
	{
		parseLastLine();
		System.out.println("GetLongitude: " + parsedLine[2]);
		//return model.getLongitude();
		return Double.parseDouble(parsedLine[2]);	
	}
	
	
	
	
	
	
	
}

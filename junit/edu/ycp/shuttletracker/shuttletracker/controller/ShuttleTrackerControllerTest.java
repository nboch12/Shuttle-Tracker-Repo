package edu.ycp.shuttletracker.shuttletracker.controller;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.shuttletracker.controller.ShuttleTrackerController;
import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

public class ShuttleTrackerControllerTest {
	private ShuttleTracker model;
	private ShuttleTrackerController controller;
	private String fileName = "pidata.csv";
	private String parsedLine[] = {"0","0.0","0.0","00:00:00:00:00","test"};
	
	@Before
	public void setup()
	{
		model = new ShuttleTracker();
		controller = new ShuttleTrackerController();
		
		controller.setModel(model);
		controller.StartTracker();
	}
	
	@Test
	public void testStartTracker()
	{
		assertTrue(model.getId() == 0);
		assertTrue(model.getLatitude() == 39.9455);
		assertTrue(model.getLongitude() == -76.7321);
		assertTrue(model.getMAC() == "00:00:00:00:00:00");
		assertTrue(model.getComment() == "Starting up shuttle tracker");
	}
	
	@Test
	public void testUpdateModel()
	{
		controller.updateModel(123, 123.123, 321.321, "00:11:22:33:44:55", "1234");
				
		assertTrue(model.getId() == 123);
		assertTrue(model.getLatitude() == 123.123);
		assertTrue(model.getLongitude() == 321.321);
		assertTrue(model.getMAC() == "00:11:22:33:44:55");
		assertTrue(model.getTime() == "1234");
	}
	
	@Test
	public void testOutputCSV()
	{
		controller.StartTracker();
		controller.outputCSV();
		
		try{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			assertTrue(br.readLine() != null );
		} catch( IOException e )
		{
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testGetId()
	{				
		assertTrue(controller.getId() ==  123);
	}
	
	@Test
	public void testGetLatitude()
	{				
		assertTrue(controller.getLatitude() ==  123.123);
	}
	
	@Test
	public void testGetLongitude()
	{				
		assertTrue(controller.getLongitude() ==  321.321);
	}
}

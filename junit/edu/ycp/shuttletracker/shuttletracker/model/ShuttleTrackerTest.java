package edu.ycp.shuttletracker.shuttletracker.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.shuttletracker.model.ShuttleTracker;

public class ShuttleTrackerTest 
{
	private ShuttleTracker model;
	
	@Before
	public void setUp()
	{
		model = new ShuttleTracker();
	}
	
	@Test
	public void testSetId()
	{
		model.setId(22);
		assertEquals(22, model.getId());
	}
	
	@Test
	public void testSetMAC()
	{
		model.setMAC("55:44:33:22:11:00");
		assertEquals("55:44:33:22:11:00", model.getMAC());
	}
	
	@Test
	public void testSetLongitude()
	{
		model.setLongitude(523.4);
		assertTrue( model.getLongitude() == 523.4);
	}
	
	@Test
	public void testSetTime()
	{
		model.setTime("1234");
		assertEquals("1234", model.getTime());
	}
	
	@Test
	public void testSetComment()
	{
		model.setComment("JUnit Test");
		assertEquals("JUnit Test", model.getComment());
	}
	
}

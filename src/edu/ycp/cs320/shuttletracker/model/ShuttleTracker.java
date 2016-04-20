package edu.ycp.cs320.shuttletracker.model;

public class ShuttleTracker {
	private int id;
	private String time, comment, mac;
	private double longitude=0.0, latitude=0.0;
	
	public ShuttleTracker() {
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setMAC(String mac)
	{
		this.mac = mac;		
	}
	
	public String getMAC() {
		return mac;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setComment( String comment )
	{
		this.comment = comment;
	}
	
	public String getComment()
	{
		return comment;
	}
	
}
	


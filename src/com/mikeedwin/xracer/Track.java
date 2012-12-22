package com.mikeedwin.xracer;

import java.util.List;



public class Track 
{
	private List<TrackPoint> trackPoints;
	private int trackTime;  //loops after this time is reached
	
	
	public Track() {
		
		
		createTestTrack1();
		
	}
	
	private void createTestTrack1()
	{
		trackPoints.add(new TrackPoint(0, 0, 0));
		trackPoints.add(new TrackPoint(1, 10, 0));
		trackPoints.add(new TrackPoint(2, 5, 40));
		trackPoints.add(new TrackPoint(2.5, 5, 40));
		trackPoints.add(new TrackPoint(3.5, 0, 0));
		trackPoints.add(new TrackPoint(4.5, -2, 0));
		trackPoints.add(new TrackPoint(5.5, -2, -20));
		trackPoints.add(new TrackPoint(6.5, 0, 0));
		
		trackTime = 7;
	}
	
	public int getHillValue(float clockValue)
	{
		return 20;
		
	}
	
	public int getTurnValue(float clockValue)
	{
		return 20;
	}
	

}

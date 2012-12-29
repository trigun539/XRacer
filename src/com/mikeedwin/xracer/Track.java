package com.mikeedwin.xracer;

import java.util.List;



public class Track 
{
	private List<TrackPoint> trackPoints;
	private int trackDistance;  //number of miles of the track, loops after this
	
	
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
		
		trackDistance = 7;
	}
	
	public int getHillValue(float distanceValue)
	{
		return 20;
		
		
	}
	
	public int getTurnValue(float distanceValue)
	{
		double mileValue = distanceValue/5280;
		
		for(int i=0; i<trackPoints.size(); i++)
		{
			TrackPoint TP = trackPoints.get(i);
			
			if(mileValue == TP.mileVal)
				return TP.turnVal;
			
			else if(mileValue < TP.mileVal)  //this is the first track point you havent reached yet
			{
				TrackPoint TP_Prev = trackPoints.get(i-1);
				
				double mileDiff = TP.mileVal - TP_Prev.mileVal;
				double mileAlleg = (mileValue - TP_Prev.mileVal)/mileDiff;  //from 0 to 1 based on how close to each track point
				
				int returner = (int)((TP_Prev.turnVal*mileAlleg)+(TP.turnVal*(1-mileAlleg)));
				
				return returner;
				
			}
		}
		
		return 0;  //returns 0 if the loop fails to get a return [it shouldn't ever get here if it works]
	}
	

}

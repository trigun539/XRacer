package com.mikeedwin.xracer;

import java.util.ArrayList;



public class Track 
{
	private ArrayList<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
	private int trackDistance;  //number of miles of the track, loops after this
	
	public float turnVal = 0;
	public int hillVal = 0;
	public float nextTurnVal = 0;
	public float nextHillVal = 0;
	public float nextTurnDist = 0;
	
	public Track() {
		createTestTrack1();
		
	}
	
	private void createTestTrack1()
	{
		trackPoints.add(new TrackPoint(0, 0, 0));
		trackPoints.add(new TrackPoint(1, 10, -8));
		trackPoints.add(new TrackPoint(2, 5, 40));
		trackPoints.add(new TrackPoint(2.5, 5, 40));
		trackPoints.add(new TrackPoint(3.5, 0, 0));
		trackPoints.add(new TrackPoint(4.5, -2, 0));
		trackPoints.add(new TrackPoint(5.5, -2, -20));
		trackPoints.add(new TrackPoint(6.5, 0, 0));
		
		trackDistance = 7;
	}
	
	
	public void setValues(int distanceValue)
	{
		double mileValue = (distanceValue/5280)%trackDistance;
		
		for(int i=0; i<trackPoints.size(); i++)
		{
			TrackPoint TP = trackPoints.get(i);
			
			if(mileValue == TP.mileVal)
			{
				this.turnVal = TP.turnVal;
				this.hillVal = TP.hillVal;
				return;
			}
			
			
			else if(mileValue < TP.mileVal)  //this is the first track point you havent reached yet
			{
				TrackPoint TP_Prev = trackPoints.get(i-1);
				
				double mileDiff = TP.mileVal - TP_Prev.mileVal;
				double mileAlleg = (mileValue - TP_Prev.mileVal)/mileDiff;  //from 0 to 1 based on how close to each track point
				
				this.turnVal = (float)((TP_Prev.turnVal*(1-mileAlleg))+(TP.turnVal*mileAlleg));
				this.hillVal = (int)((TP_Prev.hillVal*(1-mileAlleg))+(TP.hillVal*mileAlleg));
				
				return;
				
			}
		}
		
		return;  //returns if the loop fails to get a return [it shouldn't ever get here if it works]
	}
	

}

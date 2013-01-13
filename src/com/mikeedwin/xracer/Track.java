package com.mikeedwin.xracer;

import java.util.ArrayList;



public class Track 
{
	private ArrayList<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
	private int trackDistance;  //number of miles of the track, loops after this
	
	public float turnVal = 0;
	public float hillVal = 0;
	public float nextTurnVal = 0;
	public float nextHillVal = 0;
	public float turnChangeSpeed = 0;  //the rate at which the road is changing
	public float distPastTrackPt = 0;  //distance driven past the last track point reached
	public float distToNextTrackPt = 0;  
	
	public Track() {
		generateRandomTrack(20);
		//createTestTrack1();
	}
	
	private void generateRandomTrack(int TrackPoints)  //generates a random track of X number of track points, then loops
	{
		double dist = 0;
		int turn = 0;
		int hill = 0;
		
		trackPoints.add(new TrackPoint(dist, turn, hill));  //the track starts with everything at zero
		
		for(int i=0; i<TrackPoints-1; i++)
		{
			dist += (Math.random() * .45)+.05;   //.05 to .5
			turn = (int)((Math.random() * 70)-35);   //-35 to 35
			hill = (int)((Math.random() * 30)-10);    //-10 to 20
			
			trackPoints.add(new TrackPoint(dist, hill, turn));
		}
		
		dist = Math.ceil(dist);
		
		trackPoints.add(new TrackPoint(dist, 0, 0));   //last track point is 0,0 to match the first [for looping]
		
		trackDistance = (int)dist;
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
	
	
	public int setValues(float distanceValue)
	{
		double mileValue = (distanceValue/5280)%(trackDistance);
		 
		for(int i=0; i<trackPoints.size(); i++)
		{
			TrackPoint TP = trackPoints.get(i);
			
			if(mileValue == TP.mileVal)
			{
				this.turnVal = TP.turnVal;
				this.hillVal = TP.hillVal;
				return 1;
			}
			
			
			if(mileValue < TP.mileVal)  //this is the first track point you havent reached yet
			{
				TrackPoint TP_Prev = trackPoints.get(i-1);
				
				double mileDiff = TP.mileVal - TP_Prev.mileVal;
				double mileAlleg = (mileValue - TP_Prev.mileVal)/mileDiff;  //from 0 to 1 based on how close to each track point
				
				this.turnVal = (float)((TP_Prev.turnVal*(1-mileAlleg))+(TP.turnVal*mileAlleg));
				this.hillVal = (float)((TP_Prev.hillVal*(1-mileAlleg))+(TP.hillVal*mileAlleg));
				this.nextHillVal = TP.hillVal;
				this.nextTurnVal = TP.turnVal;
				float nextTurnDist = (float)(TP.mileVal - mileValue);
				this.turnChangeSpeed = (nextTurnVal - turnVal)/nextTurnDist;
				this.distPastTrackPt = (float)(mileValue - TP_Prev.mileVal);
				this.distToNextTrackPt = (float)(TP.mileVal - mileValue);
				
				return 1;
				
			}
		}
		
		return 0;  //returns if the loop fails to get a return [it shouldn't ever get here if it works]
	}
	

}

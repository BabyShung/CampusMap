package com.example.campusmap.routefilter;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class ReturnRoute implements Comparable<ReturnRoute>{

	private int distance;
	private int taketime;
	private ArrayList<LatLng> points;
	private int type;
	
	public ReturnRoute(int distance, int taketime, ArrayList<LatLng> points,int type) {
		this.distance = distance;
		this.taketime = taketime;
		this.points = points;
		this.type = type;
	}

	public int getType(){
		return type;
	}
	
	public int getDistance(){
		return distance;
	}
	
	public int getTakeTime(){
		return taketime;
	}
	
	public ArrayList<LatLng> getPoints(){
		return points;
	}
	
	public void setPoints(ArrayList<LatLng> rpoints){
		points = rpoints;
	}
	
	
	@Override
	public int compareTo(ReturnRoute rr) {
		 
		int compare_distance = rr.getDistance();
 
		if(this.distance>compare_distance)
			return 1;
		else if((this.distance<compare_distance))
			return -1;
		else
			return 0;
 
	}	

}

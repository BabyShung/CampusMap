package com.example.campusmap.helper;

public class DistanceConvert {

	private int distance;
	public DistanceConvert(int distance){
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return distance+"M";
	}
}

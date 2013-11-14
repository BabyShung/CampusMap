package com.example.campusmap;

public class Location_Hao {
	private double x;
	private double y;
	private long ts;
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public long getTS(){
		return ts;
	}
	
	public Location_Hao(String unit){
		String[] xyz = unit.split(",");
		x = Double.parseDouble(xyz[0]);
		y = Double.parseDouble(xyz[1]);
		ts = Long.parseLong(xyz[2]);
	}
	
	public boolean LocationTheSame(Location_Hao b){
		if((this.getX()==b.getX())||(this.getY()==b.getY()))
		{
			return true;
		}
		else 
			return false;
	}
	
	@Override
	public String toString(){
		return x+","+y+","+ts+";";
	}
	
}

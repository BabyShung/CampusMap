package com.example.campusmap.db_object;

public class DB_Route {
	private int Rid;
	private String fileName;
	private double Starting_lat;
	private double Starting_lng;
	private double Ending_lat;
	private double Ending_lng;
	private double Distance;
	private int TakeTime;
	private String CreateTime;

	public DB_Route() {
	}

	public DB_Route(int Rid, String fileName, String CreateTime) {
		this.Rid = Rid;
		this.fileName = fileName;
		this.CreateTime = CreateTime;
	}

	public int getRid() {
		return Rid;
	}

	public String getFileName() {
		return this.fileName;
	}

	@Override
	public String toString() {
		return "Route_" + Rid + ":   " + CreateTime;
	}
}

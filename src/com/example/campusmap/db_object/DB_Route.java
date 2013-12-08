package com.example.campusmap.db_object;

public class DB_Route {
	private int Rid;
	private String fileName;
	private double Starting_lat;
	private double Starting_lng;
	private double Ending_lat;
	private double Ending_lng;
	private double Distance;
	private long TakeTime;
	private String CreateTime;

	public DB_Route() {
	}

	// take from db
	public DB_Route(int Rid, String fileName, String CreateTime) {
		this.Rid = Rid;
		this.fileName = fileName;
		this.CreateTime = CreateTime;
	}

	// prepare to store in db
	public DB_Route(double Starting_lat, double Starting_lng,
			double Ending_lat, double Ending_lng, double Distance, long TakeTime) {
		this.Starting_lat = Starting_lat;
		this.Starting_lng = Starting_lat;
		this.Ending_lat = Ending_lat;
		this.Ending_lng = Ending_lng;
		this.Distance = Distance;
		this.TakeTime = TakeTime;
	}

	public int getRid() {
		return Rid;
	}

	public void setFileName(String fn) {
		this.fileName = fn;
	}

	/**
	 * all the get methods
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}

	public double getStarting_lat() {
		return this.Starting_lat;
	}

	public double getStarting_lng() {
		return this.Starting_lng;
	}

	public double getEnding_lat() {
		return this.Ending_lat;
	}

	public double getEnding_lng() {
		return this.Ending_lng;
	}

	public double getDistance() {
		return this.Distance;
	}

	public long getTakeTime() {
		return this.TakeTime;
	}

	@Override
	public String toString() {
		return "Route_" + Rid + ":   " + CreateTime;
	}
}

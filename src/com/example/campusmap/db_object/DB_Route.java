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
	private String Destination;
	private String CreateTime;

	public DB_Route() {
	}

	// take from db
//	public DB_Route(int Rid, String fileName, String CreateTime) {
//		this.Rid = Rid;
//		this.fileName = fileName;
//		this.CreateTime = CreateTime;
//	}

	// prepare to store in db
	public DB_Route(double Starting_lat, double Starting_lng,
			double Ending_lat, double Ending_lng, double Distance, long TakeTime ) {
		this.Starting_lat = Starting_lat;
		this.Starting_lng = Starting_lng;
		this.Ending_lat = Ending_lat;
		this.Ending_lng = Ending_lng;
		this.Distance = Distance;
		this.TakeTime = TakeTime;

	}
	
	// read from db, route act
	public DB_Route(int Rid,String fileName, double Starting_lat, double Starting_lng,
			double Ending_lat, double Ending_lng, double Distance, long TakeTime,String Destination,String CreateTime) {
		this.Rid = Rid;
		this.fileName = fileName;
		this.Starting_lat = Starting_lat;
		this.Starting_lng = Starting_lng;
		this.Ending_lat = Ending_lat;
		this.Ending_lng = Ending_lng;
		this.Distance = Distance;
		this.TakeTime = TakeTime;
		this.Destination = Destination;
		this.CreateTime = CreateTime;
	}

	public int getRid() {
		return Rid;
	}

	public void setFileName(String fn) {
		this.fileName = fn;
	}
	
	public void setDestination(String des) {
		this.Destination = des;
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

	public String getDestination() {
		return this.Destination;
	}
	
	public String getCreateTime() {
		return this.CreateTime;
	}
	@Override
	public String toString() {
		return "Route_" + Rid + ":   " + CreateTime;
	}
}

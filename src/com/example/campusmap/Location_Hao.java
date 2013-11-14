package com.example.campusmap;

public class Location_Hao {
	private double x;
	private double y;
	private long ts;
	private Location_Hao middle;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public long getTS() {
		return ts;
	}

	public Location_Hao(String unit) {
		String[] xyz = unit.split(",");
		x = Double.parseDouble(xyz[0]);
		y = Double.parseDouble(xyz[1]);
		ts = Long.parseLong(xyz[2]);
	}

	public Location_Hao(double x, double y, long ts) {
		this.x = x;
		this.y = y;
		this.ts = ts;
	}

	public boolean LocationTheSame(Location_Hao b) {
		if ((this.getX() == b.getX()) || (this.getY() == b.getY())) {
			return true;
		} else
			return false;
	}

	@Override
	public String toString() {
		return x + "," + y + "," + ts;
	}

	public double euclideanDistance(Location_Hao a, Location_Hao b) {
		double xDiff = a.x - b.x;
		double xSqr = Math.pow(xDiff, 2);

		double yDiff = a.y - b.y;
		double ySqr = Math.pow(yDiff, 2);

		double output = Math.sqrt(xSqr + ySqr);
		return output;
	}

	public Location_Hao getAverageMeanPoint(Location_Hao a, Location_Hao b,
			Location_Hao second) {
		double newX = (a.x + b.x) / 2;
		double newY = (a.y + b.y) / 2;
		Long timeS = second.getTS();

		return new Location_Hao(newX, newY,timeS);
	}

	public boolean checkNextPointInScope(Location_Hao second, Location_Hao third) {
		Location_Hao mid = getAverageMeanPoint(this, third, second);
		this.middle = mid;
		double midToSecond = euclideanDistance(mid, second);
		double firstTomid = euclideanDistance(this, mid);

		if (midToSecond > firstTomid) {
			return false;
		} else
			return true;

	}

	public Location_Hao getMidPoint() {
		if (middle != null)
			return middle;
		else
			return null;
	}

}

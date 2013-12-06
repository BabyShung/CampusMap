package com.example.campusmap.geometry;

public class Point {
	private double x;
	private double y;
	private Point middle;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double euclideanDistance(Point a, Point b) {
		double xDiff = a.x - b.x;
		double xSqr = Math.pow(xDiff, 2);

		double yDiff = a.y - b.y;
		double ySqr = Math.pow(yDiff, 2);

		double output = Math.sqrt(xSqr + ySqr);
		return output;
	}

	public Point getAverageMeanPoint(Point a,Point b) {
		double newX = (a.x + b.x) / 2;
		double newY = (a.y + b.y) / 2;

		return new Point(newX, newY);
	}

	public boolean checkNextPointInScope(Point second, Point third) {
		Point mid = getAverageMeanPoint(this,third);
		this.middle = mid;
		double midToSecond = euclideanDistance(mid, second);
		double firstTomid = euclideanDistance(this, mid);

		if (midToSecond > firstTomid) {
			return false;
		} else
			return true;

	}
	
	public Point getMidPoint(){
		if(middle != null)
			return middle;
		else
			return null;
	}
	
	@Override
	public String toString(){
		return this.x+","+this.y;
	}
}

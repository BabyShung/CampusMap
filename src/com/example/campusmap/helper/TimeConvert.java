package com.example.campusmap.helper;

public class TimeConvert {

	private int total_seconds;

	public TimeConvert(int total_seconds) {
		this.total_seconds = total_seconds;
	}

	public TimeConvert(String total_seconds) {
		this.total_seconds = Integer.parseInt(total_seconds);
	}

	@Override
	public String toString() {
		if (total_seconds >= 60) {
			int minute = total_seconds / 60;
			int second = total_seconds % 60;
			if (minute == 1) {
				if (second == 0) {
					return minute + "min";
				} else {
					return minute + "min " + second + "s";
				}
			} else {
				if (second == 0) {
					return minute + "mins";
				} else {
					return minute + "mins " + second + "s";
				}

			}
		} else {
			return total_seconds + "s";
		}

	}

}

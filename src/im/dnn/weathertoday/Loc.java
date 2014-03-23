package im.dnn.weathertoday;

public class Loc {
	private static double _lat = 0;
	private static double _lng = 0;
	
	public static double getLat () {
		return _lat;
	}
	public static double getLng () {
		return _lng;
	}

	public static void setLatLng(double latitude, double longitude) {
		_lat = latitude; _lng = longitude;
	}
}
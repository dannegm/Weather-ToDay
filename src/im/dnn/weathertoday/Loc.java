package im.dnn.weathertoday;

// Objeto donde almacenemos la ubicación
public class Loc {
	// Almacena latitud
	private static double _lat = 0;
	// Almacena longitud
	private static double _lng = 0;
	
	// Obtiene latitud
	public static double getLat () {
		return _lat;
	}
	// Obtiene longitud
	public static double getLng () {
		return _lng;
	}

	// Establece latitu y longitud
	public static void setLatLng(double latitude, double longitude) {
		_lat = latitude; _lng = longitude;
	}
}
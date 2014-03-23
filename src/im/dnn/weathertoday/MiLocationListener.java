package im.dnn.weathertoday;

import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

public final class MiLocationListener implements LocationListener {
	private double Latitude = 0;
	private double Longitude = 0;
	
	private Context ctx;
	
	public void setContext(Context _ctx) {
		ctx = _ctx;
	}

	public double getLat () {
		return Latitude;
	}
	public double getLng () {
		return Longitude;
	}

	public void onLocationChanged(Location loc) {
		Latitude = loc.getLatitude();
		Longitude = loc.getLongitude();

		Loc.setLatLng(Latitude, Longitude);
	}

	public void onProviderDisabled(String provider) {
		// Lost GPS
	}
	public void onProviderEnabled(String provider) {
		Toast.makeText( ctx, "Buscando ubicación", Toast.LENGTH_SHORT ).show();
	}
	public void onStatusChanged(String provider, int status, Bundle extras){}
}

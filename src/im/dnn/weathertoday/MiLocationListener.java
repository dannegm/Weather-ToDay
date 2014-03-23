package im.dnn.weathertoday;

// Importamos utiler�as
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

// Objeto que iteractua con geolocate
public final class MiLocationListener implements LocationListener {
	// Almacena el contexto
	private Context ctx;
	
	// Se establece el contexto
	public void setContext(Context _ctx) {
		ctx = _ctx;
	}

	// Cuando se cambia la ubicaci�n
	public void onLocationChanged(Location loc) {
		// Obtenemos coordenadas
		double Latitude = loc.getLatitude();
		double Longitude = loc.getLongitude();

		// Almacenamos las coordenadas
		Loc.setLatLng(Latitude, Longitude);
	}

	// M�todo de la interfaz LocationListener cuando se desactivan los servicios de ubicaci�n
	public void onProviderDisabled(String provider) {
		// Lost GPS
	}
	// M�todo de la interfaz LocationListener cuando se activan los servicios de ubicaci�n
	public void onProviderEnabled(String provider) {
		Toast.makeText( ctx, "Buscando ubicaci�n", Toast.LENGTH_SHORT ).show();
	}
	// M�todo de la interfaz LocationListener cuando se actualizan los servicios de ubicaci�n
	public void onStatusChanged(String provider, int status, Bundle extras){}
}

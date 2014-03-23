package im.dnn.weathertoday;

// Importamos utilerías
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

	// Cuando se cambia la ubicación
	public void onLocationChanged(Location loc) {
		// Obtenemos coordenadas
		double Latitude = loc.getLatitude();
		double Longitude = loc.getLongitude();

		// Almacenamos las coordenadas
		Loc.setLatLng(Latitude, Longitude);
	}

	// Método de la interfaz LocationListener cuando se desactivan los servicios de ubicación
	public void onProviderDisabled(String provider) {
		// Lost GPS
	}
	// Método de la interfaz LocationListener cuando se activan los servicios de ubicación
	public void onProviderEnabled(String provider) {
		Toast.makeText( ctx, "Buscando ubicación", Toast.LENGTH_SHORT ).show();
	}
	// Método de la interfaz LocationListener cuando se actualizan los servicios de ubicación
	public void onStatusChanged(String provider, int status, Bundle extras){}
}

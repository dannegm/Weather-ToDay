/*
 * Muy bien, comentemos el código para saber qué estamos haciendo y ver si podemos resolver lo de las peticiones al servidor
 */

// Primero las importaciones
package im.dnn.weathertoday;

// Utilerías android
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

// Geolocate
import android.location.LocationManager;
import android.location.LocationListener;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Activity principal
public class MainActivity extends Activity {
	
	// API Key de Wundergroud (para el clima)
	private String apiWeatherKey = "[aquí la clave]";
	// Url que nos devolverá el JSON
	private String apiWeatherUrl = "http://api.wunderground.com/api/" + apiWeatherKey + "/conditions/q/";
	// En un futuro instanciaremos aquí el JSON
	JSONArray WeatherResponse = null;

	@Override
	// Lanza el layout
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Construimos Geolocation
		BuildLocation();
	}

	@Override
	// Lanzamos el actionbar
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	// Obtenemos eventos del action bar
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Boton refresh
		case R.id.action_refresh:
			// Actializamos ubicación
			UpdateLocation();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	    }
	}
	
	// Método para construir Geolocate
	public void BuildLocation () {
		// Iniciamos los sericios de locación
		LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Creeamos instancia de la locación e interactuamos con la interfaz, posteriormente nos proporcionará las coordenadas que ecesitamos
		MiLocationListener locc = new MiLocationListener();
		// Pasamos la instancia actual a el objeto locc, tengo el presentimiento que no funciona e.e
		locc.setContext(getApplicationContext());
		
		// Le pasamos la interfaz a la instancia para obtener las coordenadas
		LocationListener milocListener = locc;
		// Obtenemos coordenadas por GPS
		milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
		
		// Actualizaos locación
		UpdateLocation();
	}
	
	public void UpdateLocation () {
		// Mostramos mensaje donde esperamos ubicación
		Toast.makeText(getApplicationContext(), "Esperando ubicación", Toast.LENGTH_LONG).show();
		// Actualizamos los datos del API
		CallApiWeather();
	}
	
	public void CallApiWeather () {
			//String l_lat = (String) Double.toString(Loc.getLat());
			//String l_lng = (String) Double.toString(Loc.getLng());
			String LatLang = "19.62313108,-99.01779141";
			
			String apiRest = apiWeatherUrl + LatLang + ".json";
			URL url = null;
			try {
				url = new URL(apiRest);
			} catch (MalformedURLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			InputStream urlInputStream = null;
			try {
				urlInputStream = url.openConnection().getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject json = null;
			try {
				json = new JSONObject(urlInputStream.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
	}
}

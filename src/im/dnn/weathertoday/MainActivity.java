/*
 * Muy bien, comentemos el c�digo para saber qu� estamos haciendo y ver si podemos resolver lo de las peticiones al servidor
 */

// Primero las importaciones
package im.dnn.weathertoday;

// Utiler�as android
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
	private String apiWeatherKey = "[aqu� la clave]";
	// Url que nos devolver� el JSON
	private String apiWeatherUrl = "http://api.wunderground.com/api/" + apiWeatherKey + "/conditions/q/";
	// En un futuro instanciaremos aqu� el JSON
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
			// Actializamos ubicaci�n
			UpdateLocation();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	    }
	}
	
	// M�todo para construir Geolocate
	public void BuildLocation () {
		// Iniciamos los sericios de locaci�n
		LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Creeamos instancia de la locaci�n e interactuamos con la interfaz, posteriormente nos proporcionar� las coordenadas que ecesitamos
		MiLocationListener locc = new MiLocationListener();
		// Pasamos la instancia actual a el objeto locc, tengo el presentimiento que no funciona e.e
		locc.setContext(getApplicationContext());
		
		// Le pasamos la interfaz a la instancia para obtener las coordenadas
		LocationListener milocListener = locc;
		// Obtenemos coordenadas por GPS
		milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
		
		// Actualizaos locaci�n
		UpdateLocation();
	}
	
	public void UpdateLocation () {
		// Mostramos mensaje donde esperamos ubicaci�n
		Toast.makeText(getApplicationContext(), "Esperando ubicaci�n", Toast.LENGTH_LONG).show();
		// Actualizamos los datos del API
		CallApiWeather();
	}
	
	public void CallApiWeather () {
		// Si las coordenadas son diferentes a 0,0
		if (Loc.getLat() != 0 && Loc.getLat() != 0) {
			// Construimos string para hacer la petici�n al API
			String l_lat = (String) Double.toString(Loc.getLat());
			String l_lng = (String) Double.toString(Loc.getLng());
			String LatLang = l_lat + "," + l_lng;
			
			// Construimos URL que llamar� el API
			String apiRest = apiWeatherUrl + LatLang + ".json";
			// Mostramos un mensaje que os mostrar� la url a la que se le har� petici�n
			Toast.makeText(getApplicationContext(), "Petici�n a " + apiRest, Toast.LENGTH_LONG).show();
			// Constuimos JSON a partir de la petici�n
			JSONObject json = JSONParser.getJSONfromURL(apiRest);
			
			// Si obtenemos datos, continuamos
			if (json != null) {
				try {
					// OteniendoJSON Array
					WeatherResponse = json.getJSONArray("current_observation");
					JSONObject wapi = WeatherResponse.getJSONObject(0);
					
					// Obtenemos el lugar actual seg�n la API
					JSONArray display_location_json = wapi.getJSONArray("display_location");
					JSONObject display_location = display_location_json.getJSONObject(0);
					String display_city = display_location.getString("city") + ", " + display_location.getString("country");
					
					// La mostramos en pantalla
					TextView txtDisplayCity = (TextView) findViewById(R.id.cityname);
					txtDisplayCity.setText(display_city);
					
					// Obtenemos temperaturas
					String temp_c = wapi.getString("temp_c") + "�";
					TextView txtDregressUp = (TextView) findViewById(R.id.degress_up);
					txtDregressUp.setText(temp_c);
					
					String dewpoint_c = wapi.getString("dewpoint_c") + "�";
					TextView txtDregressDown = (TextView) findViewById(R.id.degress_down);
					txtDregressDown.setText(dewpoint_c);
					
					// Obtenemos la fecha en la que se hizo la cosulta
					String local_time_rfc822 = wapi.getString("local_time_rfc822");
					TextView txtDatedisplay = (TextView) findViewById(R.id.datedisplay);
					txtDatedisplay.setText(local_time_rfc822);
				
				// Si tenemos unaexcepci�n... creo que deber�a mostrarlo en pantalla XD
				} catch (JSONException e) {}
				
			// Si no tenemos datos, avisamos al usuario
			} else {
				Toast.makeText(getApplicationContext(), "Sin respuesta del servidor", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Esperando ubicaci�n", Toast.LENGTH_LONG).show();
		}
	}
}

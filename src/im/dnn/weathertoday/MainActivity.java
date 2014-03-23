package im.dnn.weathertoday;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
	private String apiWeatherKey = "953a5a77a301d5c3";
    private String apiWeatherUrl = "http://api.wunderground.com/api/" + apiWeatherKey + "/conditions/q/";
    JSONArray WeatherResponse = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BuildLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	UpdateLocation();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void BuildLocation () {
		LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		MiLocationListener locc = new MiLocationListener();
		locc.setContext(getApplicationContext());
		
		LocationListener milocListener = locc;
		milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);
		
		UpdateLocation();
	}
	
	public void UpdateLocation () {
		Toast.makeText(getApplicationContext(), "Esperando ubicación", Toast.LENGTH_LONG).show();
		CallApiWeather();
	}
	
	public void CallApiWeather () {
		if (Loc.getLat() != 0 && Loc.getLat() != 0) {
			String l_lat = (String) Double.toString(Loc.getLat());
			String l_lng = (String) Double.toString(Loc.getLng());
			String LatLang = l_lat + "," + l_lng;
			
			String apiRest = apiWeatherUrl + LatLang + ".json";
			Toast.makeText(getApplicationContext(), "Petición a " + apiRest, Toast.LENGTH_LONG).show();
			JSONObject json = JSONParser.getJSONfromURL(apiRest);
			if (json != null) {
				try {
					// Getting JSON Array
					WeatherResponse = json.getJSONArray("current_observation");
					JSONObject wapi = WeatherResponse.getJSONObject(0);
					
					// Display Location
					JSONArray display_location_json = wapi.getJSONArray("display_location");
					JSONObject display_location = display_location_json.getJSONObject(0);
					String display_city = display_location.getString("city") + ", " + display_location.getString("country");
					
					TextView txtDisplayCity = (TextView) findViewById(R.id.cityname);
					txtDisplayCity.setText(display_city);
					
					// Temperaturas
					String temp_c = wapi.getString("temp_c") + "º";
					TextView txtDregressUp = (TextView) findViewById(R.id.degress_up);
					txtDregressUp.setText(temp_c);
					
					String dewpoint_c = wapi.getString("dewpoint_c") + "º";
					TextView txtDregressDown = (TextView) findViewById(R.id.degress_down);
					txtDregressDown.setText(dewpoint_c);
					
					// Día local_time_rfc822
					String local_time_rfc822 = wapi.getString("local_time_rfc822");
					TextView txtDatedisplay = (TextView) findViewById(R.id.datedisplay);
					txtDatedisplay.setText(local_time_rfc822);
					
				} catch (JSONException e) {}
			} else {
				Toast.makeText(getApplicationContext(), "Sin respuesta del servidor", Toast.LENGTH_LONG).show();
			}
		}
	}
}

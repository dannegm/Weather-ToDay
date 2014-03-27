package im.dnn.weathertoday;

import com.loopj.android.http.*;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.location.LocationManager;
import android.location.LocationListener;

import org.json.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BuildLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	// Obtenemos eventos del action bar
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Boton refresh
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
		try {
			CallApiWeather();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public void CallApiWeather () throws JSONException {
		String l_lat = (String) Double.toString(Loc.getLat());
		String l_lng = (String) Double.toString(Loc.getLng());
		
		String LatLang = l_lat + "," + l_lng;
		GetApiFlickr.SetLatLng(l_lat, l_lng);
		
		//GetApiFlickr.SetLatLng("19.62313108", "-99.01779141");
		//String LatLang = "19.62313108,-99.01779141";
		
		String picUrl = GetApiFlickr.getUrlPicture();

		AsyncHttpClient client = new AsyncHttpClient();
		String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/gif" };
		client.get(picUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
		    @Override
		    public void onSuccess(byte[] fileData) {
		        Bitmap bm = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
		        ImageView bgFlickr = (ImageView) findViewById(R.id.BG_Weather);
		        
		        DisplayMetrics dm = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(dm);

		        int newBitmapW = dm.widthPixels;
		        int newBitmaph = dm.heightPixels;
		        bm = PicFilters.resize(bm, newBitmapW, newBitmaph);

		        bgFlickr.setImageBitmap(bm);
		    }
		});

        GetApiWeather.get("conditions", LatLang, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
            	try {
                	 // Getting JSON Array
            		JSONObject wapi = json.getJSONObject("current_observation");

            		// Display Location
            		JSONObject display_location = wapi.getJSONObject("display_location");
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
            		String local_time_rfc822 = wapi.getString("weather");
            		TextView txtDatedisplay = (TextView) findViewById(R.id.datedisplay);
            		txtDatedisplay.setText(local_time_rfc822);
            		
            		// Icon
            		String iconWeather = wapi.getString("icon");
            		int iconWeatherID = getResources().getIdentifier(iconWeather, "drawable", getPackageName());
            		ImageView imgIconWeatherView = (ImageView) findViewById(R.id.iconWeather);
            	    Drawable imgIconWeather = getResources().getDrawable(iconWeatherID);
            	    imgIconWeatherView.setImageDrawable(imgIconWeather);

                } catch (JSONException e) {}
            }
        });
    }
}

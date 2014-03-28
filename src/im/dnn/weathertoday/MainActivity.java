package im.dnn.weathertoday;

import com.loopj.android.http.*;

import android.os.Bundle;
import android.os.Handler;
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
	String LatLang = "";
	String l_lat = "";
	String l_lng = "";
	
	Config conf = null;

	LocationManager milocManager = null;
	LocationListener milocListener = null;

	Handler handler = new Handler();
	Runnable updateData = new Runnable() {
		public void run(){
			handler.postDelayed(updateData, 500);
			if (Loc.getLat() != 0 || Loc.getLng() != 0) {
				milocManager.removeUpdates(milocListener);
				l_lat = (String) Double.toString(Loc.getLat());
				l_lng = (String) Double.toString(Loc.getLng());
				
				conf.setLat(l_lat);
				conf.setLng(l_lng);
				LatLang = l_lat + "," + l_lng;
				UpdateWeather();
				handler.removeCallbacks(updateData);
			} else {
				UpdateLocation();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		conf = new Config(getApplicationContext());
		
		if (conf.getLat() != null && conf.getLng() != null) {
			l_lat = conf.getLat();
			l_lng = conf.getLng();
			LatLang = l_lat + "," + l_lng;
			UpdateWeather();
		} else {
			updateData.run();
		}
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
			updateData.run();
			return true;
		default:
			return super.onOptionsItemSelected(item);
	    }
	}

	public void UpdateLocation () {
		milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		MiLocationListener locc = new MiLocationListener();
		locc.setContext(getApplicationContext());
		
		milocListener = locc;
		Toast.makeText(getApplicationContext(), "Esperando ubicación", Toast.LENGTH_LONG).show();
		milocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, milocListener);
		milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, milocListener);
	}
	
	public void UpdateWeather () {
		try {
			CallApiWeather();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public void CallApiWeather () throws JSONException {
		GetApiFlickr.SetLatLng(l_lat, l_lng);
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
            		String temp_c = wapi.getInt("temp_c") + "º";
            		TextView txtDregressUp = (TextView) findViewById(R.id.degress_up);
            		txtDregressUp.setText(temp_c.toString());

            		String dewpoint_c = wapi.getInt("dewpoint_c") + "º";
            		TextView txtDregressDown = (TextView) findViewById(R.id.degress_down);
            		txtDregressDown.setText(dewpoint_c.toString());
            		
            		// Icon
            		String iconWeather = wapi.getString("icon");
            		int iconWeatherID = getResources().getIdentifier(iconWeather, "drawable", getPackageName());
            		ImageView imgIconWeatherView = (ImageView) findViewById(R.id.iconWeather);
            	    Drawable imgIconWeather = getResources().getDrawable(iconWeatherID);
            	    imgIconWeatherView.setImageDrawable(imgIconWeather);
            	    
            	    // Weather Now
            		int textWeatherID = getResources().getIdentifier(iconWeather, "string", getPackageName());
            		String txtWeatherNow = getResources().getString(textWeatherID);
            		TextView txtDatedisplay = (TextView) findViewById(R.id.datedisplay);
            		txtDatedisplay.setText(txtWeatherNow);

                } catch (JSONException e) {}
            }
        });
    }
}

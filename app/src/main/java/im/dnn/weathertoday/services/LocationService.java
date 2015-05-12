package im.dnn.weathertoday.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class LocationService extends Service implements LocationListener {
    private final Context ctx;

    private Location location;
    private LocationManager lm;

    private boolean gpsActive = false;
    private final long MIN_TIME = 60 * 60 * 1000; // #m * #s * 1000ms
    private final float MIN_DISTANCE = 1 * 1000; // #k * 1000m

    public LocationService (Context c) {
        super();
        this.ctx = c;
        listenLocation();
    }

    @Override
    public void onLocationChanged (Location location) {
        listenLocation();
    }

    @Override
    public void onStatusChanged (String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled (String provider) {

    }

    @Override
    public void onProviderDisabled (String provider) {
        gpsActive = false;
    }

    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    public void listenLocation () {
        try {
            lm = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            gpsActive = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {}
        if (gpsActive) {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                this
            );
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public Location getLocation () {
        return location;
    }

    public JsonObject getLocationJson () {
        Log.i("Location", "Pidiendo Json");
        try {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            Double altitude = location.getAltitude();

            String stringLocation = "{\"latitude\":\"__lat__\",\"longitude\":\"__lng__\",\"altitude\":\"__alt__\"}";
            stringLocation = stringLocation.replaceAll("__lat__", String.valueOf(latitude));
            stringLocation = stringLocation.replaceAll("__lng__", String.valueOf(longitude));
            stringLocation = stringLocation.replaceAll("__alt__", String.valueOf(altitude));

            JsonParser parserLocation = new JsonParser();
            JsonObject jsonLocation = (JsonObject) parserLocation.parse(stringLocation);

            return jsonLocation;
        } catch (Exception ex) {
            Log.e("Location", ex.getMessage());
            return null;
        }
    }
}

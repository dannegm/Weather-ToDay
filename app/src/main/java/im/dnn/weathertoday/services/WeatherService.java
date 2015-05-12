package im.dnn.weathertoday.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class WeatherService extends Service {
    private Context ctx;

    private final String API_KEY = "953a5a77a301d5c3";
    private final String HOSTNAME = "http://api.wunderground.com/api/" + API_KEY + "/";

    private final String CONSULT_CONDITIONS = "conditions";

    JsonObject location;

    public WeatherService (Context c) {
        super();
        this.ctx = c;
    }

    public void setLocation (Location loc) {
        Double latitude = loc.getLatitude();
        Double longitude = loc.getLongitude();
        Double altitude = loc.getAltitude();

        JsonObject locationJson = new JsonObject();
        locationJson.addProperty("latitude", latitude);
        locationJson.addProperty("longitude", longitude);
        locationJson.addProperty("altitude", altitude);

        this.location = locationJson;
    }

    public void setJsonLocation (JsonObject loc) {
        this.location = loc;
    }

    public void getConditions (FutureCallback<JsonObject> callback) {
        try {
            Ion.with(ctx)
                    .load(HOSTNAME
                            + CONSULT_CONDITIONS
                            + "/q/"
                            + String.valueOf(location.get("latitude").getAsString()) + ","
                            + String.valueOf(location.get("longitude").getAsString())
                            + ".json")
                    .setLogging("WeatherService", Log.DEBUG)
                    .asJsonObject()
                    .setCallback(callback);
        } catch (Exception ex) {}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

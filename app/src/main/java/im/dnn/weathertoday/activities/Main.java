package im.dnn.weathertoday.activities;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import im.dnn.weathertoday.R;
import im.dnn.weathertoday.services.LocationService;

public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationService locationService = new LocationService(this);
        Location loc = locationService.getLocation();
        ((TextView) findViewById(R.id.coords)).setText(
                "Lat: " + loc.getLatitude() +
                ", " +
                "Lng: " + loc.getLongitude()
        );
    }
}

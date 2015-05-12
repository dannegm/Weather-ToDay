package im.dnn.weathertoday.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import im.dnn.weathertoday.R;
import im.dnn.weathertoday.fragments.City;
import im.dnn.weathertoday.services.LocationService;
import im.dnn.weathertoday.utils.Config;
import me.relex.circleindicator.CircleIndicator;

public class Main extends ActionBarActivity {
    Context ctx;
    Config config;

    JsonArray cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        config = new Config(ctx);

        buildSavedCities ();
        buildDefaultLocation ();
        buildViewPager ();
    }

    public void buildSavedCities () {
        String savedCities = config.get("cities", "[]");
        JsonParser jsonCities = new JsonParser();
        cities = (JsonArray) jsonCities.parse(savedCities);
    }

    public void buildDefaultLocation () {
        LocationService locationService = new LocationService(ctx);
        JsonObject locationJson = locationService.getLocationJson();
        cities.add(locationJson);
        cities.add(locationJson);
        cities.add(locationJson);
        cities.add(locationJson);
    }

    ViewPager citiesPager;

    public void buildViewPager () {
        citiesPager = (ViewPager) findViewById(R.id.cityPager);
        citiesPager.setAdapter(new CityPagerAdapter(ctx, getSupportFragmentManager(), cities));

        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator);
        circleIndicator.setViewPager(citiesPager);

        ParallaxPagerTransformer parallax = new ParallaxPagerTransformer(R.id.weatherBG);
        parallax.setSpeed(0.5f);
        citiesPager.setPageTransformer(false, parallax);
        citiesPager.setPageMargin(2);
    }

    class CityPagerAdapter extends FragmentPagerAdapter {
        Context superCtx;
        JsonArray thisCities;

        public CityPagerAdapter (Context c, FragmentManager fm, JsonArray cities) {
            super(fm);
            this.superCtx = c;
            this.thisCities = cities;
        }
        public Fragment getItem (int position) {
            JsonObject thisCity = thisCities.get(position).getAsJsonObject();
            City city = new City(superCtx, thisCity);
            return city;
        }

        @Override
        public int getCount() {
            return thisCities.size();
        }
    }
}

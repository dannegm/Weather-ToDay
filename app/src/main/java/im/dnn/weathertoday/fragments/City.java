package im.dnn.weathertoday.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.squareup.picasso.Picasso;

import java.util.Random;

import im.dnn.weathertoday.R;
import im.dnn.weathertoday.services.GoogleSearchSerivce;
import im.dnn.weathertoday.services.WeatherService;


@SuppressLint("ValidFragment")
public class City extends Fragment {
    Context ctx;
    JsonObject cityObject;

    public City (Context c, JsonObject city) {
        this.ctx = c;
        this.cityObject = city;
    }
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.fragment_city, container, false);

        // TODO: Hay que buscar una forma de cachear el contenido
        WeatherService weatherService = new WeatherService(ctx);
        weatherService.setJsonLocation(cityObject);

        weatherService.getConditions(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                try {
                    JsonObject current_observation = result.getAsJsonObject("current_observation");

                    // Location info
                    String display_location = current_observation.getAsJsonObject("display_location").get("city").getAsString();
                    TextView weatherCity = (TextView) layout.findViewById(R.id.weatherCity);
                    weatherCity.setText(display_location);

                    // Background City
                    GoogleSearchSerivce gss = new GoogleSearchSerivce(ctx);
                    gss.findImages(display_location, new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray results = result.getAsJsonObject("responseData").getAsJsonArray("results");

                            int range = results.size();
                            int randInt = new Random().nextInt(range);

                            JsonObject pictureObject = results.get(randInt).getAsJsonObject();
                            String pictureUrl = pictureObject.get("url").getAsString();

                            ImageView weatherBG = (ImageView) layout.findViewById(R.id.weatherBG);
                            Picasso.with(ctx)
                                    .load(pictureUrl)
                                    .placeholder(R.drawable.bg)
                                    .error(R.drawable.bg)
                                    .into(weatherBG);
                        }
                    });

                    // Temp info
                    String temp_c = current_observation.get("temp_c").getAsString();
                    String dewpoint_c = current_observation.get("dewpoint_c").getAsString();
                    String temp_id = current_observation.get("icon").getAsString();

                    TextView weatherDescription = (TextView) layout.findViewById(R.id.weatherDescription);
                    TextView weatherTemp = (TextView) layout.findViewById(R.id.weatherTemp);
                    TextView weatherMinTemp = (TextView) layout.findViewById(R.id.weatherMinTemp);

                    weatherTemp.setText(temp_c + "º");
                    weatherMinTemp.setText(dewpoint_c + "º");

                    String temp_description = temp_id;
                    switch (temp_id) {
                        case "chanceflurries": temp_description = getResources().getString(R.string.chanceflurries); break;
                        case "chancerain": temp_description = getResources().getString(R.string.chancerain); break;
                        case "chancesleet": temp_description = getResources().getString(R.string.chancesleet); break;
                        case "chancesnow": temp_description = getResources().getString(R.string.chancesnow); break;
                        case "chancetstorms": temp_description = getResources().getString(R.string.chancetstorms); break;
                        case "clear": temp_description = getResources().getString(R.string.clear); break;
                        case "cloudy": temp_description = getResources().getString(R.string.cloudy); break;
                        case "flurries": temp_description = getResources().getString(R.string.flurries); break;
                        case "fog": temp_description = getResources().getString(R.string.fog); break;
                        case "hazy": temp_description = getResources().getString(R.string.hazy); break;
                        case "mostlycloudy": temp_description = getResources().getString(R.string.mostlycloudy); break;
                        case "mostlysunny": temp_description = getResources().getString(R.string.mostlysunny); break;
                        case "nt_chanceflurries": temp_description = getResources().getString(R.string.nt_chanceflurries); break;
                        case "nt_chancerain": temp_description = getResources().getString(R.string.nt_chancerain); break;
                        case "nt_chancesleet": temp_description = getResources().getString(R.string.nt_chancesleet); break;
                        case "nt_chancesnow": temp_description = getResources().getString(R.string.nt_chancesnow); break;
                        case "nt_chancetstorms": temp_description = getResources().getString(R.string.nt_chancetstorms); break;
                        case "nt_clear": temp_description = getResources().getString(R.string.nt_clear); break;
                        case "nt_cloudy": temp_description = getResources().getString(R.string.nt_cloudy); break;
                        case "nt_flurries": temp_description = getResources().getString(R.string.nt_flurries); break;
                        case "nt_fog": temp_description = getResources().getString(R.string.nt_fog); break;
                        case "nt_hazy": temp_description = getResources().getString(R.string.nt_hazy); break;
                        case "nt_mostlysunny": temp_description = getResources().getString(R.string.nt_mostlysunny); break;
                        case "nt_partlycloudy": temp_description = getResources().getString(R.string.nt_partlycloudy); break;
                        case "nt_partlysunny": temp_description = getResources().getString(R.string.nt_partlysunny); break;
                        case "nt_rain": temp_description = getResources().getString(R.string.nt_rain); break;
                        case "nt_sleet": temp_description = getResources().getString(R.string.nt_sleet); break;
                        case "nt_snow": temp_description = getResources().getString(R.string.nt_snow); break;
                        case "nt_sunny": temp_description = getResources().getString(R.string.nt_sunny); break;
                        case "nt_tstorms": temp_description = getResources().getString(R.string.nt_tstorms); break;
                        case "partlycloudy": temp_description = getResources().getString(R.string.partlycloudy); break;
                        case "partlysunny": temp_description = getResources().getString(R.string.partlysunny); break;
                        case "rain": temp_description = getResources().getString(R.string.rain); break;
                        case "sleet": temp_description = getResources().getString(R.string.sleet); break;
                        case "snow": temp_description = getResources().getString(R.string.snow); break;
                        case "sunny": temp_description = getResources().getString(R.string.sunny); break;
                        case "tstorms": temp_description = getResources().getString(R.string.tstorms); break;
                    }

                    weatherDescription.setText(temp_description);
                } catch (Exception ex) {}
            }
        });

        return layout;
    }
}

package im.dnn.weathertoday.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URLEncoder;

/**
 * Created by dannegm on 4/3/15.
 */
public class GoogleSearchSerivce extends Service {
    private Context ctx;

    private final String API_KEY = "AIzaSyBMe_v4_d84EatdKNkGqo1eWUYEWA0-eCA";
    private final String HOSTNAME = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";

    public GoogleSearchSerivce (Context c) {
        super();
        this.ctx = c;
    }

    // Image Search Config
    private final String filter = "1";
    private final String imgSize = "large";
    private final String searchType = "image";
    private final String imgType = "photo";
    private final String fileType = "jpg";
    private final String rights = "cc_publicdomain";

    public void findImages (String query, FutureCallback<JsonObject> callback) {
        query = URLEncoder.encode(query);
        Ion.with(ctx)
            .load(HOSTNAME
                + "&q=" + query
                + "&imgsz=" + imgSize
                + "&imgtype=" + imgType
            )
            .setLogging("GoogleSearchService", Log.DEBUG)
            .asJsonObject()
            .setCallback(callback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

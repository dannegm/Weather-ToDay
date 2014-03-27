package im.dnn.weathertoday;

import com.loopj.android.http.*;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetApiFlickr {
	private static String apiFlickrKey = "19996438a657e40bd1e08152d7011a48";
	private static String apiFlickrPlaces = "";
	private static String apiFlickrPhotos = "";
	private static String picUrl = "";

	private static AsyncHttpClient client = new AsyncHttpClient();

	private static void getJsonPlace () throws JSONException {
		client.get(apiFlickrPlaces, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
            	try {
	               	// Getting JSON Array
	           		JSONObject places = json.getJSONObject("places");
            		JSONArray placeArray = places.getJSONArray("place");
            		JSONObject place = placeArray.getJSONObject(0);
            		String woeid = place.getString("woeid");
            		apiFlickrPhotos = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + apiFlickrKey + "&woe_id=" + woeid + "&format=json&nojsoncallback=1";
            	} catch (JSONException e) {}
            }
        });
    }
	private static void getJsonPicture () throws JSONException {
		client.get(apiFlickrPhotos, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
            	try {
	               	// Getting JSON Array
	           		JSONObject photos = json.getJSONObject("photos");
            		JSONArray photoArray = photos.getJSONArray("photo");
            		
            		int range = photoArray.length();
            		int randInt = new Random().nextInt(range) + 1;
            		JSONObject photo = photoArray.getJSONObject(randInt);
            		
            		picUrl = "http://farm" + photo.getString("farm") + ".staticflickr.com/" + photo.getString("server") + "/" + photo.getString("id") + "_" + photo.getString("secret") + ".jpg";
                } catch (JSONException e) {}
            }
        });
    }
	public static void SetLatLng (String Lat, String Lng) {
		apiFlickrPlaces = "http://api.flickr.com/services/rest/?method=flickr.places.findByLatLon&api_key=" + apiFlickrKey + "&lat=" + Lat + "&lon=" + Lng + "&format=json&nojsoncallback=1";
		try {
			getJsonPlace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static String getUrlPicture () {
		try {
			getJsonPicture();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return picUrl;
	}
}
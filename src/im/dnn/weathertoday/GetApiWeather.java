package im.dnn.weathertoday;

import com.loopj.android.http.*;

public class GetApiWeather {
	private static String apiWeatherKey = "953a5a77a301d5c3";
	private static String apiWeatherUrl = "http://api.wunderground.com/api/" + apiWeatherKey + "/";

	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String request, String consult, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(request, consult), null, responseHandler);
	}
	
	private static String getAbsoluteUrl(String apiRequest, String apiConsult) {
		return apiWeatherUrl + apiRequest + "/q/" + apiConsult + ".json";
	}
}
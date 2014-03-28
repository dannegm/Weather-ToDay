package im.dnn.weathertoday;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
	private final String SHARED_PREFS_FILE = "WPrefs";

	private Context mContext;

	public Config (Context context){
		mContext = context;
	}
	private SharedPreferences getSettings(){
		 return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
	}

	public String getLat (){
		return getSettings().getString("lat", null);  
	}
	public void setLat (String _latlang) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString("lat", _latlang);
		editor.commit();
	}

	public String getLng (){
		return getSettings().getString("lng", null);  
	}
	public void setLng (String _latlang) {
		SharedPreferences.Editor editor = getSettings().edit();
		editor.putString("lng", _latlang);
		editor.commit();
	}
}

package im.dnn.weathertoday;

// Importamos todo lo que necesitamos
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

// Objeto para otener JSON
public class JSONParser {
	
	// Método para obtener JSON de una URL
	public static JSONObject getJSONfromURL(String url){
		// Inicializamos variables
		InputStream is = null;
		String result = "";
		JSONObject json = null;
		
		// Probamos
		try {
			// Creamos un cliente HTTP
			HttpClient httpclient = new DefaultHttpClient();
			// Creamos petición POST (sí, ya intenté también con GET) 
			HttpPost httppost = new HttpPost(url);
			// Ejecutamos petición
			HttpResponse response = httpclient.execute(httppost);
			// Obtenemos contenido de la respuesta
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {}

		// Probamos
		try {
			// Leemos la respuesta y la codificamos
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"), 8);
			// Construimos string
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	sb.append(line + "\n");
	        }
	        is.close();
	        // Guardamos string
	        result = sb.toString();
		} catch(Exception e) {}

		// Probamos
		try {
			// Convertimos string en JSON
			json = new JSONObject(result);
		} catch (JSONException e) {}

		// Regresamos JSON
		return json;
	}
}

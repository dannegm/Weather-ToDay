package im.dnn.weathertoday;
import java.io.*;
import java.net.*;


public class GetJSONDanne {

    public static String get (String coord) {
		Socket conn;
		String reply = "";
		BufferedReader resp;
		String Header;
		Header = "GET /api/953a5a77a301d5c3/conditions/q/" + coord + ".json HTTP/1.1\nHost: api.wunderground.com\nConnection: Close\n\n";
                
		try{
			conn = new Socket("api.wunderground.com",80);
                        conn.getOutputStream().write(Header.getBytes("ASCII"));
			resp = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while(reply!=null){
				reply = resp.readLine().toString();
				System.out.println(reply); 
            }
			conn.close();
			resp.close();
		} catch(Exception e ){}
		
		return reply;
    }
}
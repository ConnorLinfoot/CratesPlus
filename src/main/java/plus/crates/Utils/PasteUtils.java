package plus.crates.Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PasteUtils {

	public synchronized static String paste(String urlParameters) {
		HttpURLConnection connection = null;
		try {
			//Create connection
			URL url = new URL("http://hastebin.com/documents");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			//Get Response
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(rd.readLine());
			return "http://hastebin.com/" + obj.get("key");
		} catch (Exception ex) {
			return null;
		} finally {
			if (connection == null) {
				return null;
			}
			connection.disconnect();
		}
	}

}

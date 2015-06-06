package com.connorlinfoot.cratesplus.Utils;


import com.connorlinfoot.cratesplus.Utils.json.JSONObject;

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
            URL url = new URL("https://paste.enkelhosting.com/documents");
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
            return "https://paste.enkelhosting.com/" + new JSONObject(rd.readLine()).getString("key");
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

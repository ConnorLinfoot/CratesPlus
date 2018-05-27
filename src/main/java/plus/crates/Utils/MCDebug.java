package plus.crates.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MCDebug {

    public synchronized static String paste(String name, String contents) {
        String desc = "";

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mcdebug.xyz/v3/paste/").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                StringWriter sw = new StringWriter();
                new JsonWriter(sw).beginObject()
                        .name("description").value(desc)
                        .name("public").value(false)
                        .name("files")
                        .beginObject().name(name)
                        .beginObject().name("content").value(contents)
                        .endObject()
                        .endObject()
                        .endObject();

                os.write(sw.toString().getBytes(StandardCharsets.UTF_8));
            }

            if (connection.getResponseCode() >= 400) {
                return null;
            }
//            StringWriter writer = new StringWriter();
//            IOUtils.copy(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), writer);
//            String theString = writer.toString();
//            System.out.println(theString);
            JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
            connection.disconnect();
            System.out.println(response.toString());
            if (response.has("success")) {
                if (response.get("success").getAsBoolean()) {
                    return response.get("url").getAsString();
                } else {
                    return response.get("error").getAsString();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}

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
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/gists").openConnection();
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

            JsonObject response = new Gson().fromJson(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
            String pasteUrl = response.get("html_url").getAsString();
            connection.disconnect();

            try {
                connection = (HttpURLConnection) new URL("https://git.io").openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(("url=" + pasteUrl).getBytes(StandardCharsets.UTF_8));
                }
                pasteUrl = connection.getHeaderField("Location");
                connection.disconnect();
            } catch (Exception e) {
                // ignored
            }

            return pasteUrl;
        } catch (Exception e) {
            return null;
        }
    }

}

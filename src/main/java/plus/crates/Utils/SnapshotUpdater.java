package plus.crates.Utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import plus.crates.CratesPlus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SnapshotUpdater {
    private SnapshotUpdater.UpdateResult result = SnapshotUpdater.UpdateResult.DISABLED;
    private String version;

    public enum UpdateResult {
        NO_UPDATE,
        DISABLED,
        FAIL_HTTP,
        SNAPSHOT_UPDATE_AVAILABLE
    }

    public SnapshotUpdater(JavaPlugin plugin) {
        doCheck();
    }

    private void doCheck() {
        String data = null;
        String url = "http://api.connorlinfoot.com/v1/resource/snapshot/cratesplus/";
        try {
            data = doCurl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) jsonParser.parse(data);
            if (obj.get("version") != null) {
                String newestVersion = obj.get("version") + "." + obj.get("snapshot");
                String currentVersion = CratesPlus.getPlugin().getDescription().getVersion().replaceAll("-SNAPSHOT-", "."); // Changes 4.0.0-SNAPSHOT-4 to 4.0.0.4
                if (Integer.parseInt(newestVersion.replace(".", "")) > Integer.parseInt(currentVersion.replace(".", ""))) {
                    result = UpdateResult.SNAPSHOT_UPDATE_AVAILABLE;
                    version = obj.get("version") + "-SNAPSHOT-" + obj.get("snapshot");
                } else {
                    result = UpdateResult.NO_UPDATE;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public UpdateResult getResult() {
        return result;
    }

    public String getVersion() {
        return version;
    }

    public String doCurl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setDoInput(true);
        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.close();
        DataInputStream input = new DataInputStream(con.getInputStream());
        int c;
        StringBuilder resultBuf = new StringBuilder();
        while ((c = input.read()) != -1) {
            resultBuf.append((char) c);
        }
        input.close();
        return resultBuf.toString();
    }

}
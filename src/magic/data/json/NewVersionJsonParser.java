package magic.data.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import magic.MagicMain;
import magic.data.GeneralConfig;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import org.json.JSONArray;
import org.json.JSONObject;

public final class NewVersionJsonParser {
    private NewVersionJsonParser() {}

    private static void downloadLatestJsonFile(final File jsonFile) {
        try {
            final NewVersionJsonFile downloadFile = new NewVersionJsonFile(jsonFile);
            downloadFile.download(GeneralConfig.getInstance().getProxy());
        } catch (IOException ex) {
            System.err.println("Download of json file failed : " + ex.getMessage());
        }
    }

    public static String getLatestVersion() {

        final File jsonFile = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("newversion.json").toFile();

        downloadLatestJsonFile(jsonFile);
        if (jsonFile.length() == 0) {
            // a problem occurred, log and ignore.
            System.err.println("new version json file is empty!");
            return "";
        }

        try {
            // parse json file.
            final String jsonText = getJsonString(jsonFile);
            System.out.println(jsonText);
            final JSONArray jsonArray = new JSONArray(jsonText);
            if (jsonArray.length() > 0) {
                final JSONObject jsonRelease = jsonArray.getJSONObject(0);
                final String release = jsonRelease.getString("name");
                return isNewVersion(release) ? release : "";
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return "";

    }

    private static boolean isNewVersion(final String releaseValue) {
        System.out.println(releaseValue + ", " + MagicMain.VERSION);
        return (releaseValue != null && !releaseValue.equals(MagicMain.VERSION));
    }

    private static String getJsonString(final File jsonFile) throws IOException {
        try (final BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
    }
}

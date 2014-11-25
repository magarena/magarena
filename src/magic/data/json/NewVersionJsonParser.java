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

    public static String getLatestVersion() {
        final File jsonFile = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("newversion.json").toFile();
        if (downloadLatestJsonFile(jsonFile)) {
            return getFirstVersionInJsonFile(jsonFile);
        } else {
            return "";
        }
    }

    private static boolean downloadLatestJsonFile(final File jsonFile) {
        try {
            final DownloadableJsonFile downloadFile
                    = new DownloadableJsonFile("https://api.github.com/repos/magarena/magarena/tags", jsonFile);
            downloadFile.download(GeneralConfig.getInstance().getProxy());
            if (jsonFile.length() == 0) {
                System.err.println("new version json file is empty!");
                return false;
            } else {
                return true;
            }
        } catch (IOException ex) {
            System.err.println("Error accessing/saving new version json feed:- " + ex);
            return false;
        }
    }

    /**
     * The latest version should be the first item in the json feed.
     */
    private static String getFirstVersionInJsonFile(final File jsonFile ) {
        try {
            final JSONArray jsonArray = new JSONArray(getJsonString(jsonFile));
            if (jsonArray.length() > 0) {
                final JSONObject jsonRelease = jsonArray.getJSONObject(0);
                final String release = jsonRelease.getString("name");
                return isNewVersion(release) ? release : "";
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return "";
    }

    private static boolean isNewVersion(final String releaseValue) {
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

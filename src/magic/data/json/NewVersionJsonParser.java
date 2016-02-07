package magic.data.json;

import java.io.File;
import java.io.IOException;
import magic.data.GeneralConfig;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;
import org.json.JSONObject;

public final class NewVersionJsonParser {
    private NewVersionJsonParser() {}

    public static String getLatestVersion() {
        final File jsonFile = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("newversion.json").toFile();
        if (downloadJsonToFile(jsonFile)) {
            return getVersionInJsonFile(jsonFile);
        } else {
            return "";
        }
    }

    private static boolean downloadJsonToFile(final File jsonFile) {
        try {
            final DownloadableJsonFile downloadFile = new DownloadableJsonFile("https://magarena.github.io/current.json", jsonFile);
            downloadFile.doDownload(GeneralConfig.getInstance().getProxy());
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
    private static String getVersionInJsonFile(final File jsonFile ) {
        try {
            final JSONObject jsonRelease = new JSONObject(DownloadableJsonFile.getJsonString(jsonFile));
            final String release = jsonRelease.getString("version");
            return isNewVersion(release) ? release : "";
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return "";
    }

    private static boolean isNewVersion(final String releaseValue) {
        return releaseValue != null && versionCompare(releaseValue, MagicSystem.VERSION) > 0;
    }

    //source: https://stackoverflow.com/questions/6701948/efficient-way-to-compare-version-strings-in-java
    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        } else {
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}

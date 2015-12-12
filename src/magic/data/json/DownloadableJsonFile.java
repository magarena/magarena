package magic.data.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import magic.data.DownloadableFile;

public class DownloadableJsonFile extends DownloadableFile {

    public DownloadableJsonFile(final String url, final File jsonFile) throws MalformedURLException {
        super(jsonFile, new URL(url));
    }

    public static String getJsonString(final File jsonFile) throws IOException {
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

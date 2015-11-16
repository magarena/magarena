package magic.data.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import magic.data.DownloadableFile;

public class DownloadableJsonFile extends DownloadableFile {

    private final URL url;
    private final File file;

    public DownloadableJsonFile(final String url, final File jsonFile) throws MalformedURLException {
        this.url = new URL(url);
        file = jsonFile;
    }

    @Override
    public void download(Proxy proxy) throws IOException {
        final File tempFile = new File(file.getParent(), "~" + file.getName());
        DownloadableFile.downloadToFile(proxy, url, tempFile);
        Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public String getFilename() {
        return file.getName();
    }

    @Override
    public File getLocalFile() {
        return file;
    }

    @Override
    public URL getDownloadUrl() {
        return url;
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

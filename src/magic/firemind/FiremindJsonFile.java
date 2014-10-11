package magic.firemind;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import magic.data.DownloadableFile;

public class FiremindJsonFile extends DownloadableFile {

    private static final String JSON_URL = "https://www.firemind.ch/decks/top.json";

    private final URL url;
    private final File file;

    public FiremindJsonFile(final File jsonFile) throws MalformedURLException {
        url = new URL(JSON_URL);
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
    public File getFile() {
        return file;
    }

    @Override
    public URL getDownloadUrl() {
        return url;
    }

}

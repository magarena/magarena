package magic.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public class CardImageFile extends DownloadableFile {

    private final File file;
    private final URL url;
    private final String cardName;

    public CardImageFile(final MagicCardDefinition cdef) throws MalformedURLException {
        file = MagicFileSystem.getCardImageFile(cdef);
        url = new URL(cdef.getImageURL());
        cardName = cdef.getName();
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
    public void download(final Proxy proxy) throws IOException {
        final File tempFile = new File(file.getParent(), "~" + file.getName());
        DownloadableFile.downloadToFile(proxy, url, tempFile);
        Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public URL getDownloadUrl() {
        return url;
    }

}

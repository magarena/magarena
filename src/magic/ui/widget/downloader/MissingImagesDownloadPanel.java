package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public abstract class MissingImagesDownloadPanel extends ImageDownloadPanel {
    
    protected MissingImagesDownloadPanel(final IImageDownloadListener listener) {
        super(listener);
    }

    @Override
    protected String doFileDownloadAndGetName(final DownloadableFile file, final Proxy proxy) throws IOException {
        file.download(proxy);
        if (file instanceof CardImageFile) {
            return ((CardImageFile) file).getCardName();
        } else {
            return "";
        }
    }

    @Override
    protected int getCustomCount(int countInteger) {
        return countInteger;
    }

}

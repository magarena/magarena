package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import javax.swing.SwingWorker;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public abstract class MissingImagesDownloadPanel extends ImageDownloadPanel {
    
    private final IImageDownloadListener listener;

    protected MissingImagesDownloadPanel(final IImageDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected SwingWorker<Void, Integer> getImageDownloadWorker(final ImagesDownloadList downloadList, final Proxy proxy) {
        return new ImageDownloadWorker(downloadList, CONFIG.getProxy(), listener);
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

package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.util.Collection;
import javax.swing.SwingUtilities;
import magic.data.DownloadableFile;
import magic.model.MagicCardDefinition;
import magic.ui.MagicDownload;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public class HQImagesDownloadPanel extends ImageDownloadPanel {

    private volatile int imageSizeChangedCount = 0;

    public HQImagesDownloadPanel(final IImageDownloadListener listener) {
        super(listener);
    }

    @Override
    protected String getProgressCaption() {
        return "Low quality images = ";
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        return MagicDownload.getLowQualityImageCards();
    }

    @Override
    protected String getLogFilename() {
        return "hqimages.log";
    }

    @Override
    protected String getDownloadButtonCaption() {
        return "Check for new HQ images & download";
    }

    @Override
    protected String doFileDownloadAndGetName(final DownloadableFile file, final Proxy proxy) throws IOException {
        if (MagicDownload.isRemoteFileDownloadable(file)) {
            imageSizeChangedCount += MagicDownload.doDownloadImageFile(file, proxy);
            return file.getFilename();
        } else {
            return "";
        }
    }

    @Override
    protected int getCustomCount(int countInteger) {
        return imageSizeChangedCount;
    }

    @Override
    protected void doCustomActionAfterDownload(int errorCount) {
        // nothing to do.
    }
        
}

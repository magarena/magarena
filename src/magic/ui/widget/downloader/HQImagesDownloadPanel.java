package magic.ui.widget.downloader;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.utility.MagicUtility;
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.model.MagicCardDefinition;
import magic.utility.MagicDownload;

@SuppressWarnings("serial")
public class HQImagesDownloadPanel extends ImageDownloadPanel {

    @Override
    protected SwingWorker<Void, Integer> getImageDownloadWorker(final ImagesDownloadList downloadList, final Proxy proxy) {
        return new DownloadImagesWorker(downloadList, CONFIG.getProxy());
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

    public static Dimension getImageDimensions(final File resourceFile) throws IOException {
        try (final ImageInputStream in = ImageIO.createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        }
        return null;
    }

    @Override
    protected String getLogFilename() {
        return "hqimages.log";
    }

    @Override
    protected String getDownloadButtonCaption() {
        return "Check for new HQ images & download";
    }

    private class DownloadImagesWorker extends SwingWorker<Void, Integer> {

        private final List<String> downloadedImages = new ArrayList<>();
        private final Proxy proxy;
        private volatile int imageSizeChangedCount = 0;
        private final ImagesDownloadList downloadList;

        public DownloadImagesWorker(final ImagesDownloadList downloadList, final Proxy proxy) {
            this.downloadList = downloadList;
            this.proxy = proxy;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int errorCount = 0;
            int fileCount = 0;
            for (DownloadableFile downloadableFile : downloadList) {
                try {
                    if (MagicDownload.isRemoteFileDownloadable(downloadableFile)) {
                        imageSizeChangedCount += MagicDownload.doDownloadImageFile(downloadableFile, this.proxy);
                        downloadedImages.add(downloadableFile.getFilename());
                    }
                } catch (IOException ex) {
                    final String msg = ex.toString() + " [" + downloadableFile.getFilename() + "]";
                    if (++errorCount >= MagicDownload.MAX_ERROR_COUNT) {
                        throw new IOException(msg);
                    } else {
                        System.err.println(msg);
                    }
                }
                fileCount++;
                if (isCancelled()) {
                    break;
                } else {
                    publish(new Integer(fileCount));
                }
            }
            return null;
        }

        @Override
        protected void done() {
            boolean downloadFailed = false;
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println(ex.getCause().getMessage());
                downloadFailed = true;
            } catch (CancellationException ex) {
                // System.out.println("DownloadSwingWorker cancelled by user!");
            }
            setButtonState(false);
            resetProgressBar();
            if (downloadFailed) {
                captionLabel.setText("!!! ERROR - See console for details !!!");
                captionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                captionLabel.setIcon(null);
            } else {
                magic.ui.CachedImagesProvider.getInstance().clearCache();
                if (MagicUtility.isDevMode() && downloadedImages.size() > 0) {
                    saveDownloadLog(downloadedImages);
                }
                buildDownloadImagesList();
            }
            notifyStatusChanged(DownloaderState.STOPPED);
        }

        @Override
        protected void process(List<Integer> chunks) {
            final int countInteger = chunks.get(chunks.size() - 1);
            if (!isCancelled()) {
                progressBar.setValue(countInteger);
                captionLabel.setText(getProgressCaption() + (downloadList.size() - imageSizeChangedCount));
            }
        }

        private void resetProgressBar() {
            assert SwingUtilities.isEventDispatchThread();
            progressBar.setValue(0);
            progressBar.setString(null);
        }

    }
        
}

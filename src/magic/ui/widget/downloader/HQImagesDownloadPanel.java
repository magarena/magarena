package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.utility.MagicSystem;
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.model.MagicCardDefinition;
import magic.ui.MagicDownload;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public class HQImagesDownloadPanel extends ImageDownloadPanel {

    private final IImageDownloadListener listener;

    public HQImagesDownloadPanel(final IImageDownloadListener listener) {
        this.listener = listener;
    }

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
                        throw new RuntimeException(msg +
                                String.format("\nERROR THRESHOLD[%d] REACHED!", MagicDownload.MAX_ERROR_COUNT));
                    } else {
                        listener.setMessage(msg);
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
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                isError = true;
                listener.setMessage(ex.getCause().getMessage());
            } catch (CancellationException ex) {
                // System.out.println("DownloadSwingWorker cancelled by user!");
            }
            setButtonState(false);
            resetProgressBar();
            if (isError) {
                captionLabel.setText("!!! ERROR - See console for details !!!");
                captionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                captionLabel.setIcon(null);
                downloadButton.setEnabled(false);
            } else {
                magic.ui.CachedImagesProvider.getInstance().clearCache();
                if (MagicSystem.isDevMode() && downloadedImages.size() > 0) {
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

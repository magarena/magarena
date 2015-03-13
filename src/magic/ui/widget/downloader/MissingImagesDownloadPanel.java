package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.utility.MagicSystem;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.ui.MagicDownload;
import magic.ui.dialog.IImageDownloadListener;

@SuppressWarnings("serial")
public abstract class MissingImagesDownloadPanel extends ImageDownloadPanel {
    
    private final IImageDownloadListener listener;

    protected MissingImagesDownloadPanel(final IImageDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected SwingWorker<Void, Integer> getImageDownloadWorker(final ImagesDownloadList downloadList, final Proxy proxy) {
        return new ImagesDownloader(downloadList, CONFIG.getProxy());
    }

    private class ImagesDownloader extends SwingWorker<Void, Integer> {

        private final List<String> downloadedImages = new ArrayList<>();
        private final Proxy proxy;
        private final ImagesDownloadList downloadList;

        public ImagesDownloader(final ImagesDownloadList downloadList, final Proxy proxy) {
            this.downloadList = downloadList;
            this.proxy = proxy;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int fileCount = 0;
            int errorCount = 0;
            for (DownloadableFile imageFile : downloadList) {
                try {
                    imageFile.download(proxy);
                    if (imageFile instanceof CardImageFile) {
                        downloadedImages.add(((CardImageFile) imageFile).getCardName());
                    }
                } catch (IOException ex) {
                    final String msg = ex.toString() + " [" + imageFile.getFilename() + "]";
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
                if (MagicSystem.isDevMode()) {
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
                captionLabel.setText(getProgressCaption() + (downloadList.size() - countInteger));
            }
        }

        private void resetProgressBar() {
            assert SwingUtilities.isEventDispatchThread();
            progressBar.setValue(0);
            progressBar.setString(null);
        }

    }

}

package magic.ui.widget.downloader;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.MagicUtility;
import magic.data.DownloadImageFile;
import magic.data.WebDownloader;

@SuppressWarnings("serial")
public abstract class MissingImagesDownloadPanel extends ImageDownloadPanel {

    @Override
    protected SwingWorker<Void, Integer> getImageDownloadWorker(final Proxy proxy) {
        return new ImagesDownloader(CONFIG.getProxy());
    }

    private class ImagesDownloader extends SwingWorker<Void, Integer> {

        private final List<String> downloadedImages = new ArrayList<>();
        private final Proxy proxy;

        public ImagesDownloader(final Proxy proxy) {
            this.proxy = proxy;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int fileCount = 0;
            int errorCount = 0;
            final int MAX_DOWNLOAD_ERRORS = 10;
            for (WebDownloader imageFile : files) {
                try {
                    imageFile.download(proxy);
                } catch (IOException ex) {
                    if (errorCount++ >= MAX_DOWNLOAD_ERRORS) {
                        throw new RuntimeException("Maximum download errors exceeded!", ex);
                    } else {
                        System.err.println("Image download failed : " + imageFile.getFilename() + " -> " + ex);
                        imageFile = null;
                    }
                }
                if (imageFile instanceof DownloadImageFile) {
                    downloadedImages.add(((DownloadImageFile) imageFile).getCardName());
                }
                fileCount++;
                if (isCancelled()) {
                    break;
                } else {
                    publish(new Integer(fileCount));
                }
            }
            magic.data.HighQualityCardImagesProvider.getInstance().clearCache();
            if (MagicUtility.isDevMode()) {
                saveDownloadLog(downloadedImages);
            }
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (CancellationException ex) {
//                System.out.println("DownloadSwingWorker cancelled by user!");
            } finally {
                setButtonState(false);
                resetProgressBar();
            }
            scanForMissingImages();
            notifyStatusChanged(DownloaderState.STOPPED);
        }

        @Override
        protected void process(List<Integer> chunks) {
            final int countInteger = chunks.get(chunks.size() - 1);
            if (!isCancelled()) {
                progressBar.setValue(countInteger);
                captionLabel.setText(getProgressCaption() + (files.size() - countInteger));
            }
        }

        private void resetProgressBar() {
            assert SwingUtilities.isEventDispatchThread();
            progressBar.setValue(0);
            progressBar.setString(null);
        }

    }

}

package magic.ui.screen.images.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.exception.DownloadException;
import magic.ui.MagicImages;
import magic.ui.MagicLogFile;
import magic.ui.PrintedCardImage;
import magic.ui.ProxyCardImage;
import magic.utility.MagicSystem;

class DownloadWorker extends SwingWorker<Void, Integer> {

    private static final MagicLogFile missingLog = new MagicLogFile("downloaded-images");

    private ImagesDownloadList downloadList;
    private final IDownloadListener listener;
    private final CardImageDisplayMode displayMode;
    private boolean updateDownloadDate = true;
    private boolean isLogging = true;
    private int serverBusyCooldown = 1; // in millisecs

    DownloadWorker(IDownloadListener aListener, CardImageDisplayMode aMode) {
        this.listener = aListener;
        this.displayMode = aMode;
    }

    @Override
    protected Void doInBackground() throws MalformedURLException {
        this.downloadList = ScanWorker.getImagesDownloadList((IScanListener)listener, displayMode);
        doDownloadImages();
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (CancellationException ex) {
            System.err.println("ImageDownloadWorker cancelled.");
        }
        listener.setButtonState(false);
        resetProgressBar();

        MagicImages.clearCache();
        listener.buildDownloadImagesList();
    }

    @Override
    protected void process(List<Integer> chunks) {
        final int countInteger = chunks.get(chunks.size() - 1);
        if (!isCancelled()) {
            listener.showProgress(countInteger, downloadList.size());
        }
    }

    private boolean isLoggingOn() {
        return isLogging && MagicSystem.isDevMode();
    }

    /**
     * Initializes log files for capturing useful info (DEVMODE ONLY!).
     */
    private void initializeLogFiles() {
        if (isLoggingOn()) {
            synchronized (missingLog) {
                try {
                    missingLog.deleteLogFileIfExists();
                    missingLog.log("# " + LocalDateTime.now());
                    missingLog.log("#");
                } catch (IOException ex) {
                    System.err.println("initializeLogFiles: " + ex);
                    isLogging = false;
                }
            }
        }
    }

    private void setServerBusyCooldown(String errmsg) {
        if (errmsg.contains("HTTP response code: 503")) {
            serverBusyCooldown += 100;
        }
    }

    private boolean doDeleteLocalImageFile(File aFile) {
        try {
            Files.deleteIfExists(aFile.toPath());
        } catch (IOException ex) {
            listener.setMessage(String.format("%s [%s]", ex.toString(), aFile.getName()));
            return false;
        }
        return true;
    }

    private void downloadPrintedImage(CardImageFile imageFile) {
        try {
            PrintedCardImage.tryDownloadingPrintedImage(imageFile);
        } catch (DownloadException ex) {
            listener.setMessage(ex.getMessage());
            setServerBusyCooldown(ex.toString());
            if (ex.getCause() instanceof IOException) {
                // if local image file already existed before download then
                // download was triggered by the 'image_updated' script property.
                // As download failed remove local image so it gets picked up
                // by the missing image trigger instead. If unable to remove
                // local image then don't update 'imageDownloaderRunDate'.
                updateDownloadDate = doDeleteLocalImageFile(imageFile.getLocalFile());
            }
        }
    }

    private boolean tryDownloadingCroppedImage(CardImageFile imageFile) {
        try {
            return ProxyCardImage.tryDownloadingCroppedImage(imageFile);
        } catch (DownloadException | MalformedURLException ex) {
            System.err.println(String.format("%s [%s]", ex.toString(), imageFile.getFilename()));
        }
        return false;
    }

    private void doPause(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }

    private void doDownloadImages() throws MalformedURLException {

        assert !SwingUtilities.isEventDispatchThread();

        initializeLogFiles();
        int fileCount = 0;

        for (DownloadableFile dFile : downloadList) {

            final CardImageFile imageFile = (CardImageFile) dFile;

            if (displayMode == CardImageDisplayMode.PROXY) {
                if (!tryDownloadingCroppedImage(imageFile)) {
                    downloadPrintedImage(imageFile);
                }
            } else {
                downloadPrintedImage(imageFile);
            }
            doPause(serverBusyCooldown);

            fileCount++;

            if (isCancelled()) {
                return;
            } else {
                publish(new Integer(fileCount));
            }
        }

        if (updateDownloadDate) {
            listener.doCustomActionAfterDownload();
        }

    }

    private void resetProgressBar() {
        assert SwingUtilities.isEventDispatchThread();
        listener.resetProgress();
    }

}

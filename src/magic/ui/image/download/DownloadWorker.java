package magic.ui.image.download;

import magic.ui.CardTextLanguage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.GeneralConfig;
import magic.data.ImagesDownloadList;
import magic.ui.URLUtils;
import magic.ui.MagicLogFile;
import magic.utility.MagicSystem;

class DownloadWorker extends SwingWorker<Void, Integer> {

    private static final MagicLogFile missingLog = new MagicLogFile("downloaded-images");
    
    private final Proxy proxy;
    private ImagesDownloadList downloadList;
    private final IDownloadListener listener;
    private final CardTextLanguage textLanguage;
    private final DownloadMode downloadMode;
    private boolean updateDownloadDate = true;
    private boolean isLogging = true;
   
    DownloadWorker(
        IDownloadListener aListener,
        CardTextLanguage aLanguage,
        DownloadMode aDownloadMode) {
        
        this.listener = aListener;
        this.textLanguage = aLanguage;
        this.downloadMode = aDownloadMode;
        this.proxy = GeneralConfig.getInstance().getProxy();
    }

    @Override
    protected Void doInBackground() {
        this.downloadList = ScanWorker.getImagesDownloadList((IScanListener)listener, downloadMode);
        doDownloadImages(textLanguage);
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

        magic.ui.CachedImagesProvider.getInstance().clearCache();
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
        return isLogging && MagicSystem.isDevMode() && downloadMode == DownloadMode.MISSING;
    }

    /**
     * Experimental, currently only available in devMode and missing mode.
     */
    private void doLog(String cardName, CardTextLanguage aLang, URL cardUrl) {
        if (isLoggingOn()) {
            synchronized (missingLog) {
                try {
                    missingLog.log(cardName, aLang, cardUrl);
                } catch (IOException ex) {
                    System.err.println("doLog: " + ex);
                    isLogging = false;
                }
            }
        }
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

    /**
     * Gets alternate magiccards.info image url for given card text language.
     * If it fails then displays reason in error log panel and returns null url.
     */
    private URL getAlternateUrl(DownloadableFile aFile, CardTextLanguage textLang) {
        try {
            return URLUtils.getAlternateMagicCardsImageUrl(aFile.getUrl(), textLang);
        } catch (MalformedURLException ex) {
            listener.setMessage(String.format("%s [%s]", ex.toString(), aFile.getUrl()));
            return null;
        }
    }

    /**
     * Attempts to download alternate card image.
     * If it fails displays reason in error log panel.
     */
    private boolean tryAlternateDownload(final DownloadableFile aFile, CardTextLanguage aLang) {
        try {
            aFile.doDownload(proxy);
        } catch (IOException ex) {
            listener.setMessage(String.format("%s [%s (%s)]", ex.toString(), aFile.getFilename(), aLang));
            return false;
        }
        return true;
    }

    private boolean downloadAlternateCardImage(final CardImageFile aFile, CardTextLanguage aLang) {
        final URL altUrl = getAlternateUrl(aFile, aLang);
        if (URLUtils.isUrlValid(altUrl)) {
            if (tryAlternateDownload(new DownloadableFile(aFile.getLocalFile(), altUrl), aLang)) {
                doLog(aFile.getCardName(), aLang, altUrl);
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to download default card image.
     * If it fails displays reason in error log panel.
     */
    private boolean tryDefaultDownload(final DownloadableFile aFile) {
        try {
            aFile.doDownload(proxy);
        } catch (IOException ex) {
            listener.setMessage(String.format("%s [%s]", ex.toString(), aFile.getFilename()));
            return false;
        }
        return true;
    }

    private void downloadDefaultCardImage(final CardImageFile aFile) {
        if (tryDefaultDownload(aFile)) {
            doLog(aFile.getCardName(), CardTextLanguage.ENGLISH, null);
        }
    }

    private void doDownloadImages(CardTextLanguage textLang) {

        initializeLogFiles();
        int fileCount = 0;

        for (DownloadableFile dFile : downloadList) {

            final CardImageFile imageFile = (CardImageFile) dFile;

            if (deleteLocalImageFileIfMissing(imageFile) == false)
                continue; // with next DownloadableFile.

            if (textLang.isEnglish() || !downloadAlternateCardImage(imageFile, textLang)) {
                downloadDefaultCardImage(imageFile);
            }

            fileCount++;

            if (isCancelled()) {
                break;
            } else {
                publish(new Integer(fileCount));
            }
        }

        if (updateDownloadDate) {
            listener.doCustomActionAfterDownload();
        }

    }

    /**
     * If downloading missing images but local image file already exists then download was
     * triggered by 'image_updated'. If unable to delete image file so that it becomes
     * "missing" do not update 'imageDownloaderRunDate' so that image update remains pending.
     */
    private boolean deleteLocalImageFileIfMissing(DownloadableFile dFile) {
        if (downloadMode == DownloadMode.MISSING) {
            try {
                Files.deleteIfExists(dFile.getLocalFile().toPath());
            } catch (IOException ex) {
                updateDownloadDate = false;
                listener.setMessage(String.format("%s [%s]", ex.toString(), dFile.getFilename()));
                return false;
            }
        }
        return true;
    }

    private void resetProgressBar() {
        assert SwingUtilities.isEventDispatchThread();
        listener.resetProgress();
    }
    
}

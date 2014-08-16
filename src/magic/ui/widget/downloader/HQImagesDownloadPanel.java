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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.MagicUtility;
import magic.data.CardDefinitions;
import magic.data.WebDownloader;
import magic.model.MagicCardDefinition;
import magic.utility.MagicDownload;
import magic.utility.MagicFiles;
import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class HQImagesDownloadPanel extends ImageDownloadPanel {

    @Override
    protected SwingWorker<Void, Integer> getImageDownloadWorker(final Proxy proxy) {
        return new DownloadImagesWorker(CONFIG.getProxy());
    }

    @Override
    protected String getProgressCaption() {
        return "Low quality images = ";
    }

    @Override
    protected Collection<MagicCardDefinition> getCards() {
        assert !SwingUtilities.isEventDispatchThread();
        final List<MagicCardDefinition> cards = new ArrayList<>();
        for (final MagicCardDefinition cardDefinition : CardDefinitions.getCards()) {
            if (cardDefinition.getImageURL() != null) {
                final File imageFile = MagicFiles.getCardImageFile(cardDefinition);
                if (imageFile.exists() && isLowQualityImage(imageFile)) {
                    cards.add(cardDefinition);
                }
            }
        }
        return cards;
    }

    private boolean isLowQualityImage(final File imageFile) {
        Dimension imageSize = null;
        try {
            imageSize = getImageDimensions(imageFile);
            return (imageSize.width < 480);
        } catch (IOException | NullPointerException ex) {
            System.err.println(imageFile.getName() + " (" + imageSize + ") : " + ex);
            return false;
        }
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

        public DownloadImagesWorker(final Proxy proxy) {
            this.proxy = proxy;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int fileCount = 0;
            int errorCount = 0;
            final int MAX_DOWNLOAD_ERRORS = 10;
            for (WebDownloader imageFile : files) {
                final File localImageFile = imageFile.getFile();
                final long localFileSize = imageFile.getFile().length();
                final long remoteFileSize = MagicDownload.getDownloadableFileSize1(imageFile.getDownloadUrl());
//                System.out.println(imageFile.getFilename() + " : R=" + remoteFileSize + ", L=" + localFileSize);
                if (remoteFileSize != localFileSize) {
                    try {
                        // save downloaded image file with ~ prefix.
                        imageFile.setFilenamePrefix("~");
                        imageFile.download(proxy);
                        final File tempImageFile = imageFile.getFile();
                        final Dimension tempImageSize = getImageDimensions(tempImageFile);
                        final Dimension localImageSize = getImageDimensions(localImageFile);
                        if (!tempImageSize.equals(localImageSize)) {
                            // only interested in counting where image size changes because
                            // you can also get downloads where the file size has changed
                            // but the image size is still the same and in this context
                            // that means the image is still LQ so don't decrement the LQ
                            // count displayed in the progress bar.
                            imageSizeChangedCount++;
                        }
                        FileUtils.copyFile(tempImageFile, localImageFile);
                        tempImageFile.delete();
                    } catch (IOException ex) {
                        if (errorCount++ >= MAX_DOWNLOAD_ERRORS) {
                            throw new RuntimeException("Maximum download errors exceeded!", ex);
                        } else {
                            System.err.println("Image download failed : " + imageFile.getFilename() + " -> " + ex);
                            imageFile = null;
                        }
                    }
                    if (imageFile != null) {
                        downloadedImages.add(imageFile.getFilename());
                    }
                }
                fileCount++;
                if (isCancelled()) {
                    break;
                } else {
                    publish(new Integer(fileCount));
                }
            }
            magic.data.HighQualityCardImagesProvider.getInstance().clearCache();
            if (MagicUtility.isDevMode() && downloadedImages.size() > 0) {
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
                captionLabel.setText(getProgressCaption() + (files.size() - imageSizeChangedCount));
            }
        }

        private void resetProgressBar() {
            assert SwingUtilities.isEventDispatchThread();
            progressBar.setValue(0);
            progressBar.setString(null);
        }

    }


}

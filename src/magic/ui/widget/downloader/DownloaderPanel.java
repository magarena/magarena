package magic.ui.widget.downloader;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.MagicUtility;
import magic.data.DownloadImageFile;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.MissingImages;
import magic.data.WebDownloader;
import magic.model.MagicCardDefinition;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class DownloaderPanel extends JPanel {

    public enum DownloaderState {
        STOPPED,
        SCANNING,
        DOWNLOADING;
    }

    private final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MigLayout migLayout = new MigLayout();
    private final JLabel captionLabel = getCaptionLabel(getProgressCaption());
    private final JButton downloadButton = new JButton("Download new images");
    private final JButton cancelButton = new JButton("Cancel");
    private final JProgressBar progressBar = new JProgressBar();

    private MissingImages files;
    private boolean isCancelled = false;
    private ImagesDownloader imagesDownloader;
    private MissingImagesScanner missingImagesScanner;
    private DownloaderState downloaderState = DownloaderState.STOPPED;

    protected abstract String getProgressCaption();
    protected abstract Collection<MagicCardDefinition> getCards();
    protected abstract String getLogFilename();

    public DownloaderPanel() {
        setLookAndFeel();
        refreshLayout();
        setActions();
        scanForMissingImages();
    }

    public DownloaderState getState() {
        return downloaderState;
    }
    
    private void saveDownloadLog(final List<String> downloadLog) {
        final Path logPath = Paths.get(MagicMain.getLogsPath()).resolve(getLogFilename());
        System.out.println("saving log : " + logPath);
        try (final PrintWriter writer = new PrintWriter(logPath.toFile())) {
            for (String cardName : downloadLog) {
                writer.println(cardName);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        MagicFiles.openFileInDefaultOsEditor(logPath.toFile());
    }

    private void setActions() {
        downloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDownloadingState();
                imagesDownloader = new ImagesDownloader(CONFIG.getProxy());
                imagesDownloader.execute();
                notifyStatusChanged(DownloaderState.DOWNLOADING);
            }
        });
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancelDownloadSwingWorker();
            }
        });
    }
    
    public void doCancel() {
        isCancelled = true;
        doCancelDownloadSwingWorker();
        doCancelMissingImagesScanner();
    }

    private void doCancelDownloadSwingWorker() {
        if (imagesDownloader != null && !imagesDownloader.isCancelled() & !imagesDownloader.isDone()) {
            imagesDownloader.cancel(true);
            setButtonState(false);
        }
    }

    private void doCancelMissingImagesScanner() {
        if (missingImagesScanner != null && !missingImagesScanner.isCancelled() && !missingImagesScanner.isDone()) {
            missingImagesScanner.cancel(true);
        }
    }

    private void scanForMissingImages() {
        if (!isCancelled) {
            captionLabel.setIcon(IconImages.BUSY16);
            captionLabel.setText(getProgressCaption());
            missingImagesScanner = new MissingImagesScanner();
            missingImagesScanner.execute();
            downloadButton.setEnabled(false);
            notifyStatusChanged(DownloaderState.SCANNING);
        }
    }    

    private void resetProgressBar() {
        progressBar.setMinimum(0);
        progressBar.setMaximum(files.size());
        progressBar.setValue(0);
    }

    protected void setButtonState(final boolean isDownloading) {
        downloadButton.setVisible(!isDownloading);
        cancelButton.setVisible(isDownloading);
        refreshLayout();
    }

    private void setDownloadingState() {
        setButtonState(true);
        resetProgressBar();
        captionLabel.setIcon(IconImages.BUSY16);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 2, gapy 0");
        add(progressBar, "w 100%, h 26!");
        add(downloadButton.isVisible() ? downloadButton : cancelButton, "w 100%");
    }

    private void setLookAndFeel() {
        // Layout manager.
        setLayout(migLayout);
        // download button
        downloadButton.setEnabled(false);
        // progress bar
        progressBar.setLayout(new MigLayout("insets 0 8 0 0, aligny center"));
        progressBar.add(captionLabel, "w 100%, h 100%");
        progressBar.setStringPainted(false);
    }

    private JLabel getCaptionLabel(final String text) {
        final ImageIcon ii = IconImages.BUSY16;
        final JLabel lbl = new JLabel(ii);
        lbl.setText(text);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setHorizontalTextPosition(SwingConstants.LEADING);
        lbl.setOpaque(false);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
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
                if (!imageFile.exists()) {
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
                        downloadedImages.add(((DownloadImageFile)imageFile).getCardName());
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

    private void notifyStatusChanged(final DownloaderState newState) {
        final DownloaderState oldState = downloaderState;
        downloaderState = newState;
        firePropertyChange("downloaderState", oldState, newState);
    }

    private class MissingImagesScanner extends SwingWorker<MissingImages, Void> {
        @Override
        protected MissingImages doInBackground() throws Exception {
            return new MissingImages(getCards());
        }
        @Override
        protected void done() {
            try {
                files = get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (CancellationException ex) {
//                System.out.println("MissingImagesScanner cancelled by user!");
            }
            if (!isCancelled) {
                downloadButton.setEnabled(files.size() > 0);
                captionLabel.setIcon(null);
                captionLabel.setText(getProgressCaption() + files.size());
            }
            notifyStatusChanged(DownloaderState.STOPPED);
        }
    }

}

//    @Override
//    public void run() {
//
//        //NOT run by EDT
//        assert !SwingUtilities.isEventDispatchThread();
//
//        final int MAX_DOWNLOAD = 10;
//
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setMinimum(0);
//                progressBar.setMaximum(files.size() < MAX_DOWNLOAD ? files.size() : MAX_DOWNLOAD);
//            }
//        });
//
//        final File[] oldDirs = {
//            new File(oldDataFolder, CardDefinitions.CARD_IMAGE_FOLDER),
//            new File(oldDataFolder, CardDefinitions.TOKEN_IMAGE_FOLDER)
//        };
//
//        int count = 0;
//        final List<String> downloadedImages = new ArrayList<>();
//
//        for (final WebDownloader file : files) {
//
//            if (!file.exists()) {
//                file.download(proxy);
//                if (file instanceof DownloadImageFile) {
//                    downloadedImages.add(((DownloadImageFile) file).getCardName());
//                }
//            }
//
//            count++;
//            if (cancelDownload || count > MAX_DOWNLOAD) {
//                break;
//            }
//
//            final int fcount = count;
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    progressBar.setString(fcount + "/" + files.size());
//                    progressBar.setValue(fcount);
//                }
//            });
//        }
//
//        // clear images that are set to "missing image" in cache
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                magic.data.HighQualityCardImagesProvider.getInstance().clearCache();
//                //frame.updateGameView();
//            }
//        });
//
//        saveDownloadLog(downloadedImages);
//
//        if (!cancelDownload) {
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    GeneralConfig.getInstance().setIsMissingFiles(false);
////                    dispose();
//                }
//            });
//        }
//    }

package magic.ui.widget.downloader;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Proxy;
import java.nio.file.Path;
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
import magic.data.DownloadableFile;
import magic.data.ImagesDownloadList;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.dialog.IImageDownloadListener;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class ImageDownloadPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Cancel";
    private static final String _S2 = "!!! ERROR - See console for details !!!";

    public enum DownloaderState {
        STOPPED,
        SCANNING,
        DOWNLOADING;
    }

    protected final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MigLayout migLayout = new MigLayout();
    protected final JLabel captionLabel = getCaptionLabel(getProgressCaption());
    protected final JButton downloadButton = new JButton();
    private final JButton cancelButton = new JButton(UiString.get(_S1));
    protected final JProgressBar progressBar = new JProgressBar();
    protected volatile boolean isException = false;

    private ImagesDownloadList files;
    private boolean isCancelled = false;
    private SwingWorker<Void, Integer> imagesDownloader;
    private ImagesScanner imagesScanner;
    private DownloaderState downloaderState = DownloaderState.STOPPED;
    private final IImageDownloadListener listener;

    protected abstract String getProgressCaption();
    protected abstract Collection<MagicCardDefinition> getCards();
    protected abstract String getLogFilename();
    protected abstract String getDownloadButtonCaption();
    protected abstract String doFileDownloadAndGetName(final DownloadableFile file, final Proxy proxy) throws IOException;
    protected abstract int getCustomCount(final int countInteger);
    protected abstract void doCustomActionAfterDownload(final int errorCount);

    public ImageDownloadPanel(final IImageDownloadListener listener) {
        this.listener = listener;
        setLookAndFeel();
        refreshLayout();
        setActions();
        buildDownloadImagesList();
    }

    public DownloaderState getState() {
        return downloaderState;
    }
    
    protected void saveDownloadLog(final List<String> downloadLog) {
        final Path logPath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve(getLogFilename());
        System.out.println("saving log : " + logPath);
        try (final PrintWriter writer = new PrintWriter(logPath.toFile())) {
            for (String cardName : downloadLog) {
                writer.println(cardName);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setActions() {
        downloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDownloadingState();
                imagesDownloader = new ImageDownloadWorker(files, CONFIG.getProxy());
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
        doCancelImagesScanner();
    }

    private void doCancelDownloadSwingWorker() {
        if (imagesDownloader != null && !imagesDownloader.isCancelled() && !imagesDownloader.isDone()) {
            imagesDownloader.cancel(true);
            setButtonState(false);
        }
    }

    private void doCancelImagesScanner() {
        if (imagesScanner != null && !imagesScanner.isCancelled() && !imagesScanner.isDone()) {
            imagesScanner.cancel(true);
        }
    }

    protected final void buildDownloadImagesList() {
        if (!isCancelled) {
            captionLabel.setIcon(IconImages.getIcon(MagicIcon.BUSY16));
            captionLabel.setText(getProgressCaption());
            imagesScanner = new ImagesScanner();
            imagesScanner.execute();
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
        captionLabel.setIcon(IconImages.getIcon(MagicIcon.BUSY16));
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
        downloadButton.setText(getDownloadButtonCaption());
        // progress bar
        progressBar.setLayout(new MigLayout("insets 0 8 0 0, aligny center"));
        progressBar.add(captionLabel, "w 100%, h 100%");
        progressBar.setStringPainted(false);
    }

    private JLabel getCaptionLabel(final String text) {
        final ImageIcon ii = IconImages.getIcon(MagicIcon.BUSY16);
        final JLabel lbl = new JLabel(ii);
        lbl.setText(text);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setHorizontalTextPosition(SwingConstants.LEADING);
        lbl.setOpaque(false);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
    }

    protected void notifyStatusChanged(final DownloaderState newState) {
        assert SwingUtilities.isEventDispatchThread();
        final DownloaderState oldState = downloaderState;
        downloaderState = newState;
        firePropertyChange("downloaderState", oldState, newState);
    }

    private class ImagesScanner extends SwingWorker<ImagesDownloadList, Void> {
        @Override
        protected ImagesDownloadList doInBackground() throws Exception {
            return new ImagesDownloadList(getCards());
        }
        @Override
        protected void done() {
            try {
                files = get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (CancellationException ex) {
//                System.out.println("ImagesScanner cancelled by user!");
            }
            if (!isCancelled) {
                downloadButton.setEnabled(files.size() > 0);
                captionLabel.setIcon(null);
                captionLabel.setText(String.format("%s = %d",
                        getProgressCaption(),
                        files.size())
                );
            }
            notifyStatusChanged(DownloaderState.STOPPED);
        }
    }

    private class ImageDownloadWorker extends SwingWorker<Void, Integer> {

        private final List<String> downloadedImages = new ArrayList<>();
        private final Proxy proxy;
        private final ImagesDownloadList downloadList;
        private volatile int errorCount = 0;

        public ImageDownloadWorker(final ImagesDownloadList downloadList, final Proxy proxy) {
            this.downloadList = downloadList;
            this.proxy = proxy;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int fileCount = 0;
            errorCount = 0;
            for (DownloadableFile file : downloadList) {
                try {
                    final String name = doFileDownloadAndGetName(file, proxy);
                    if (!name.isEmpty()) {
                        downloadedImages.add(name);
                    }
                } catch (IOException ex) {
                    final String msg = String.format("%s [%s]", ex.toString(), file.getFilename());
                    listener.setMessage(msg);
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
            } catch (ExecutionException | InterruptedException ex) {
                isException = true;
                if (ex.getCause() instanceof IOException) {
                    listener.setMessage(ex.getCause().getMessage());
                } else {
                    throw new RuntimeException(ex);
                }
            } catch (CancellationException ex) {
                System.err.println("ImageDownloadWorker cancelled.");
            }
            setButtonState(false);
            resetProgressBar();
            doCustomActionAfterDownload(errorCount);
            if (isException) {
                captionLabel.setText(UiString.get(_S2));
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
                captionLabel.setText(String.format("%s = %d",
                        getProgressCaption(),
                        downloadList.size() - getCustomCount(countInteger))
                );
            }
        }

        private void resetProgressBar() {
            assert SwingUtilities.isEventDispatchThread();
            progressBar.setValue(0);
            progressBar.setString(null);
        }

    }

}

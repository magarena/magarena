package magic.ui.screen.images.download;

import java.awt.Font;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import magic.data.CardDefinitions;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.GeneralConfig;
import magic.data.ImagesDownloadList;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.MagicImages;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class DownloadPanel extends JPanel implements IScanListener, IDownloadListener {

    static final String CP_STATE_CHANGED = "5234acd4-e05d-4fc4-a599-95ecd34aa893";

    // translatable strings
    private static final String _S1 = "Cancel";

    private static final Logger LOGGER = Logger.getLogger(DownloadPanel.class.getName());

    protected static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MigLayout migLayout = new MigLayout();
    protected final JLabel captionLabel = getCaptionLabel(getProgressCaption());
    protected final JButton downloadButton = new JButton();
    private final JButton cancelButton = new JButton(MText.get(_S1));
    protected final JProgressBar progressBar = new JProgressBar();

    private ImagesDownloadList files;
    private SwingWorker<Void, Integer> imagesDownloader;
    private ScanWorker imagesScanner;
    private DownloadState downloaderState = DownloadState.STOPPED;
    private CardImageDisplayMode displayMode = CardImageDisplayMode.PRINTED;
    private final DialogMainPanel mainPanel;

    protected abstract String getProgressCaption();
    protected abstract String getDownloadButtonCaption();

    DownloadPanel(CardImageDisplayMode aMode, DialogMainPanel aPanel) {
        this.displayMode = aMode;
        this.mainPanel = aPanel;
        setLookAndFeel();
        refreshLayout();
        setActions();
        buildDownloadImagesList();
    }

    DownloadState getState() {
        return downloaderState;
    }

    private void doRunImageDownloadWorker() {
        setDownloadingState();
        imagesDownloader = new DownloadWorker(this, displayMode);
        imagesDownloader.execute();
        notifyStatusChanged(DownloadState.DOWNLOADING);
    }

    private void setActions() {
        downloadButton.addActionListener(action -> doRunImageDownloadWorker());
        cancelButton.addActionListener(action -> doCancelImageDownloadWorker());
    }

    void doCancel() {
        doCancelImageDownloadWorker();
        doCancelImagesScanner();
    }

    private void doCancelImageDownloadWorker() {
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

    @Override
    public final void buildDownloadImagesList() {
        if (imagesScanner != null && !imagesScanner.isDone()) {
            imagesScanner.cancel(true);
        }
        if (imagesScanner != null && !imagesScanner.isDone()) {
            LOGGER.log(Level.WARNING, "Scanner still running!");
            return;
        }
        captionLabel.setIcon(MagicImages.getIcon(MagicIcon.BUSY16));
        captionLabel.setText(getProgressCaption());
        imagesScanner = new ScanWorker(this, displayMode);
        imagesScanner.execute();
        downloadButton.setEnabled(false);
        notifyStatusChanged(DownloadState.SCANNING);
    }

    private void resetProgressBar() {
        progressBar.setMinimum(0);
        progressBar.setMaximum(files.size());
        progressBar.setValue(0);
    }

    @Override
    public void setButtonState(final boolean isDownloading) {
        downloadButton.setVisible(!isDownloading);
        cancelButton.setVisible(isDownloading);
        refreshLayout();
    }

    private void setDownloadingState() {
        setButtonState(true);
        resetProgressBar();
        captionLabel.setIcon(MagicImages.getIcon(MagicIcon.BUSY16));
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowx, insets 0, gap 0");
        add(progressBar, "w 100%, h 26!");
        add(downloadButton.isVisible() ? downloadButton : cancelButton, "w 100!");
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
        final ImageIcon ii = MagicImages.getIcon(MagicIcon.BUSY16);
        final JLabel lbl = new JLabel(ii);
        lbl.setText(text);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setHorizontalTextPosition(SwingConstants.LEADING);
        lbl.setOpaque(false);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
    }

    @Override
    public void notifyStatusChanged(final DownloadState newState) {
        assert SwingUtilities.isEventDispatchThread();
        final DownloadState oldState = downloaderState;
        downloaderState = newState;
        firePropertyChange(CP_STATE_CHANGED, oldState, newState);
    }

    @Override
    public void doScannerFinished(final ImagesDownloadList aList) {
        files = aList;
        downloadButton.setEnabled(files != null && files.size() > 0);
        captionLabel.setIcon(null);
        captionLabel.setText(String.format("%s = %d",
            getProgressCaption(),
            files == null ? 0 : files.size())
        );
    }

    void refreshDownloadList(CardImageDisplayMode aMode) {
        doCancel();
        this.displayMode = aMode;
        buildDownloadImagesList();
    }

    @Override
    public void setMessage(final String message) {
        mainPanel.setMessage(message);
    }

    @Override
    public void showProgress(int count, int total) {
        progressBar.setValue(count);
        captionLabel.setText(String.format("%s = %d",
            getProgressCaption(),
            total - getCustomCount(count))
        );
    }

    @Override
    public void resetProgress() {
        progressBar.setValue(0);
        progressBar.setString(null);
    }

    protected String doFileDownloadAndGetName(final DownloadableFile file) throws IOException {
        file.doDownload();
        if (file instanceof CardImageFile) {
            return ((CardImageFile) file).getCardName();
        } else {
            return "";
        }
    }

    protected int getCustomCount(int countInteger) {
        return countInteger;
    }

    public static Stream<MagicCardDefinition> getCards(Collection<MagicCardDefinition> cards, Date aDate, CardImageDisplayMode mode) {
        return cards.stream().filter(card -> CardDefinitions.requiresNewImageDownload(card, aDate));
    }

    void setLocked(boolean b) {
        downloadButton.setEnabled(!b && files != null && !files.isEmpty());
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        downloadButton.setEnabled(b && imagesScanner.isDone());
        progressBar.setEnabled(b);
        captionLabel.setEnabled(b);
    }

}

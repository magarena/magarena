package magic.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.ui.*;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.downloader.HQImagesDownloadPanel;
import magic.ui.widget.downloader.ImageDownloadPanel;
import magic.ui.widget.downloader.ImageDownloadPanel.DownloaderState;
import magic.ui.widget.downloader.PlayableDownloadPanel;
import magic.ui.widget.downloader.UnimplementedDownloadPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadImagesDialog extends JDialog implements ActionListener, PropertyChangeListener {

    // not static so that it will reflect theme changes without restart.
    private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();

    private final JButton cancelButton = new JButton();
    private final JButton backgroundButton = new JButton();
    private static List<String> newCards = null;

    private ImageDownloadPanel playableDownloaderPanel;
    private ImageDownloadPanel unimplementedDownloaderPanel;
    private ImageDownloadPanel highQualityDownloaderPanel;

    public DownloadImagesDialog(final MagicFrame frame) {
        super(frame, true);
        setLookAndFeel();
        refreshLayout();
        setEscapeKeyAsCancelAction();
        this.setLocationRelativeTo(frame);
        this.setVisible(true);
    }

    private void setEscapeKeyAsCancelAction() {
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!backgroundButton.isEnabled()) {
                    doCancelAndClose();
                }
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void refreshLayout() {
        setLayout(new MigLayout("flowy, gapy 0, insets 0"));
        add(getDialogCaptionLabel(), "w 100%, h 26!");
        add(getDownloadPanel(), "w 100%");
        add(getButtonPanel(), "w 100%, aligny bottom, pushy");
    }

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(THEME.getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(THEME.getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void setLookAndFeel() {
        setTitle("Download Card Images");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 460);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, THEME.getColor(Theme.COLOR_TITLE_BACKGROUND)));
        //
        backgroundButton.setEnabled(false);
    }

    private JPanel getDownloadPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 8, gapy 10"));
        panel.add(getPlayableDownloaderPanel(), "w 100%");
        panel.add(getUnimplementedDownloaderPanel(), "w 100%");
        panel.add(getHighQualityDownloaderPanel(), "w 100%");
        return panel;
    }

    private ImageDownloadPanel getHighQualityDownloaderPanel() {
        highQualityDownloaderPanel = new HQImagesDownloadPanel();
        highQualityDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return highQualityDownloaderPanel;
    }

    private ImageDownloadPanel getPlayableDownloaderPanel() {
        playableDownloaderPanel = new PlayableDownloadPanel();
        playableDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return playableDownloaderPanel;
    }

    private ImageDownloadPanel getUnimplementedDownloaderPanel() {
        unimplementedDownloaderPanel = new UnimplementedDownloadPanel();
        unimplementedDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return unimplementedDownloaderPanel;
    }

    private void updateComponentState() {
        backgroundButton.setEnabled(
                playableDownloaderPanel.getState() == DownloaderState.DOWNLOADING ||
                unimplementedDownloaderPanel.getState() == DownloaderState.DOWNLOADING);
    }

    private JPanel getButtonPanel() {
        // cancel button
        cancelButton.setText("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
        // background button
        backgroundButton.setText("Run in background...");
        backgroundButton.setFocusable(false);
        backgroundButton.addActionListener(this);
        // layout
        final JPanel panel = new JPanel(new MigLayout());
        panel.add(backgroundButton, "w 100%, alignx left");
        panel.add(cancelButton, "w 100!, alignx right, pushx");
        return panel;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==cancelButton) {
            doCancelAndClose();
        } else if (source == backgroundButton) {
            setVisible(false);
        }
    }

    private void doCancelAndClose() {
        playableDownloaderPanel.doCancel();
        unimplementedDownloaderPanel.doCancel();
        dispose();
    }

    public static boolean isCardInDownloadsLog(MagicCardDefinition card) {
        if (newCards == null) {
            newCards = getCardNamesFromDownloadLog();
        }
        return newCards.contains(card.getName());
    }

    private static List<String> getCardNamesFromDownloadLog() {
        final List<String> cardNames = new ArrayList<>();
        Path logPath = Paths.get(MagicMain.getLogsPath()).resolve("newcards.log");
        if (logPath.toFile().exists()) {
            try {
                for (final String cardName : Files.readAllLines(logPath, Charset.defaultCharset())) {
                    cardNames.add(cardName.trim());
                }
            } catch (final IOException ex) {
               throw new RuntimeException(ex);
            }
        }
        return cardNames;
    }

    public static void clearLoadedLogs() {
        if (newCards != null) {
            newCards.clear();
            newCards = null;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("downloaderState")) {
            updateComponentState();
//            final int progressValue = (int)evt.getNewValue();
//            progressBar.setIndeterminate(progressValue == 0 && importWorker.getProgressNote() != "");
//            progressBar.setValue(progressValue);
//        } else if (evt.getPropertyName().equalsIgnoreCase("state")) {
//            progressBar.setIndeterminate(!evt.getNewValue().toString().equalsIgnoreCase("done"));
//        } else if (evt.getPropertyName().equalsIgnoreCase("progressNote")) {
//            taskOutput.append(evt.getNewValue().toString());
        }
    }

}

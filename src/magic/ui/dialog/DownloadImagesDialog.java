package magic.ui.dialog;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.ui.*;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.downloader.HQImagesDownloadPanel;
import magic.ui.widget.downloader.ImageDownloadPanel;
import magic.ui.widget.downloader.ImageDownloadPanel.DownloaderState;
import magic.ui.widget.downloader.PlayableDownloadPanel;
import magic.ui.widget.downloader.UnimplementedDownloadPanel;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.CancelButton;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadImagesDialog
    extends JDialog
    implements ActionListener, PropertyChangeListener, IImageDownloadListener {

    // translatable strings
    private static final String _S1 = "Download Card Images";
    private static final String _S3 = "Run in background...";
    private static final String _S4 = "Copy to clipboard";
    private static final String _S5 = "Error details have been copied to the clipboard.";

    private final ErrorLogPanel errorPanel = new ErrorLogPanel();
    private final JButton cancelButton = new CancelButton();
    private final JButton backgroundButton = new JButton();
    private static List<String> newCards = null;
    private final List<ImageDownloadPanel> downloadPanels = new ArrayList<>();

    public DownloadImagesDialog(final MagicFrame frame) {
        super(frame, true);
        setDownloadPanels();
        setLookAndFeel();
        refreshLayout();
        setEscapeKeyAsCancelAction();
        this.setLocationRelativeTo(frame);
        this.setVisible(true);
    }

    private void setDownloadPanels() {
        downloadPanels.add(getPlayableDownloaderPanel());
        downloadPanels.add(getUnimplementedDownloaderPanel());
        if (MagicSystem.isDevMode()) {
            downloadPanels.add(getHighQualityDownloaderPanel());
        }
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
        add(errorPanel, "w 100%, h 100%");
        add(getButtonPanel(), "w 100%, aligny bottom, pushy");
    }

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void setLookAndFeel() {
        setTitle(UiString.get(_S1));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 460);
        setUndecorated(true);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
        //
        backgroundButton.setEnabled(false);
    }

    private JPanel getDownloadPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 8, gapy 10"));
        for (ImageDownloadPanel downloadPanel : downloadPanels) {
            panel.add(downloadPanel, "w 100%");
        }
        return panel;
    }

    private ImageDownloadPanel getHighQualityDownloaderPanel() {
        final ImageDownloadPanel highQualityDownloaderPanel = new HQImagesDownloadPanel(this);
        highQualityDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return highQualityDownloaderPanel;
    }

    private ImageDownloadPanel getPlayableDownloaderPanel() {
        final ImageDownloadPanel playableDownloaderPanel = new PlayableDownloadPanel(this);
        playableDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return playableDownloaderPanel;
    }

    private ImageDownloadPanel getUnimplementedDownloaderPanel() {
        final ImageDownloadPanel  unimplementedDownloaderPanel = new UnimplementedDownloadPanel(this);
        unimplementedDownloaderPanel.addPropertyChangeListener("downloaderState", this);
        return unimplementedDownloaderPanel;
    }

    private void updateComponentState() {
        boolean isBackgroundButtonEnabled = false;
        for (ImageDownloadPanel panel : downloadPanels) {
            final boolean isPanelDownloading = panel.getState() == DownloaderState.DOWNLOADING;
            isBackgroundButtonEnabled = isBackgroundButtonEnabled || isPanelDownloading;
        }
        backgroundButton.setEnabled(isBackgroundButtonEnabled);
    }

    private JPanel getButtonPanel() {
        // cancel button
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);
        // background button
        backgroundButton.setText(UiString.get(_S3));
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
        for (ImageDownloadPanel panel : downloadPanels) {
            panel.doCancel();
        }
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
        final Path logPath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("newcards.log");
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

    @Override
    public synchronized void setMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                errorPanel.setErrorText(message + "\n");
                if (!errorPanel.isVisible()) {
                    errorPanel.setVisible(true);
                }
            }
        });
    }

    private class ErrorLogPanel extends JPanel {

        private final JTextArea textArea;

        public ErrorLogPanel() {

            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setForeground(Color.red);
            textArea.setFont(FontsAndBorders.FONT_README.deriveFont(11.0f));

            final JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(FontsAndBorders.BLACK_BORDER);

            final JButton copyButton = new JButton(UiString.get(_S4));
            copyButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                    final StringSelection textSelection = new StringSelection(textArea.getText());
                    clip.setContents(textSelection, null);
                    ScreenController.showInfoMessage(UiString.get(_S5));
                }
            });

            setVisible(false);
            setLayout(new MigLayout("flowy, gap 0, insets 2"));
            add(scrollPane, "w 100%, h 100%");
            add(copyButton, "w 100%");

        }

        public void setErrorText(String text) {
            textArea.append(text);
        }

    }

}

package magic.ui.screen.images.download;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.WikiPage;
import magic.ui.dialog.button.CloseButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DialogMainPanel extends JPanel implements PropertyChangeListener {

    static final String CP_CLOSE = "9dbf20f7-f239-4cdf-95e6-85cfe1969ea7";
    static final String CP_RUN_BACKGROUND = "56a6f389-6238-48ba-a3d3-bc9538d1d4c7";

    // translatable strings
    private static final String _S1 = "Closes screen but continues to download images in the background.";
    private static final String _S2 = "Wiki help page";
    private static final String _S3 = "Run in background...";
    private static final String _S4 = "Opens a page containing more information on this screen in your internet browser.";
    private static final String _S6 = "Displays the printed card image. The images are downloaded to the 'cards' and 'tokens' folders.";
    private static final String _S7 = "Displays a proxy image that is generated as required using cropped images downloaded to the 'crops' folder.";

    private final JButton backgroundButton;
    private final OptionsPanel optionsPanel;
    private final List<DownloadPanel> downloadPanels = new ArrayList<>();
    private final ErrorPanel errorPanel = new ErrorPanel();
    private final ButtonsPanel buttonsPanel;
    private final HintPanel hintPanel;
    private final JPanel downloadPanel = new JPanel();

    DialogMainPanel() {

        hintPanel = new HintPanel(getDefaultHint());

        backgroundButton = new JButton(MText.get(_S3));
        backgroundButton.setFocusable(false);
        backgroundButton.addActionListener((a) -> doRunInBackground());
        hintPanel.addHintSource(backgroundButton, String.format("<b>%s</b><br>%s",
            MText.get(_S3),
            MText.get(_S1))
        );

        buttonsPanel = new ButtonsPanel();

        optionsPanel = new OptionsPanel(this);
        optionsPanel.addHintSources(hintPanel);

        setDownloadPanels();
        setLookAndFeel();
        refreshLayout();
        updateComponentState();

    }

    private String getDefaultHint() {
        return String.format("<b>%s</b><br>%s<br><br><b>%s</b><br>%s",
            CardImageDisplayMode.PRINTED.toString(), MText.get(_S6),
            CardImageDisplayMode.PROXY.toString(), MText.get(_S7)
        );
    }

    private void doRunInBackground() {
        firePropertyChange(CP_RUN_BACKGROUND, true, false);
    }

    private void refreshDownloadList() {
        CardImageDisplayMode mode = optionsPanel.getCardImageDisplayMode();
        for (DownloadPanel panel : downloadPanels) {
            panel.doCancel();
            panel.refreshDownloadList(mode);
        }
    }

    private void setDownloadPanels() {
        downloadPanels.add(getPlayableDownloaderPanel());
        downloadPanels.add(getUnimplementedDownloaderPanel());
        downloadPanel.setLayout(new MigLayout("flowy, insets 0 0 6 0, gapy 0"));
        downloadPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        for (DownloadPanel panel : downloadPanels) {
            downloadPanel.add(panel, "w 100%");
        }
    }

    private void refreshLayout() {
        removeAll();
        setLayout(new MigLayout("flowy, gap 0, insets 2 6 6 6"));
        add(optionsPanel, "w 100%, gapbottom 6");
        add(downloadPanel, "w 100%");
        add(errorPanel, "w 100%, h 100%, hidemode 3");
        add(hintPanel, "w 100%, h 100%, gaptop 10, hidemode 3");
        add(buttonsPanel, "w 100%, h 30!, pushy, aligny bottom");
        revalidate();
    }

    private void setLookAndFeel() {
        backgroundButton.setEnabled(false);
    }

    private DownloadPanel getPlayableDownloaderPanel() {
        final DownloadPanel playableDownloaderPanel = new PlayablePanel(
            optionsPanel.getCardImageDisplayMode(),
            this
        );
        playableDownloaderPanel.addPropertyChangeListener(DownloadPanel.CP_STATE_CHANGED, this);
        return playableDownloaderPanel;
    }

    private DownloadPanel getUnimplementedDownloaderPanel() {
        final DownloadPanel unimplementedDownloaderPanel = new UnimplementedPanel(
            optionsPanel.getCardImageDisplayMode(),
            this
        );
        unimplementedDownloaderPanel.addPropertyChangeListener(DownloadPanel.CP_STATE_CHANGED, this);
        return unimplementedDownloaderPanel;
    }

    private void updateComponentState() {
        boolean isScanning = false;
        boolean isDownloading = false;
        DownloadPanel activePanel = null;
        for (DownloadPanel panel : downloadPanels) {
            panel.setLocked(false);
            isScanning |= panel.getState() == DownloadState.SCANNING;
            isDownloading |= panel.getState() == DownloadState.DOWNLOADING;
            if (panel.getState() == DownloadState.DOWNLOADING) {
                activePanel = panel;
            }
        }
        if (optionsPanel != null) {
            optionsPanel.setEnabled(!isDownloading);
        }
        for (DownloadPanel panel : downloadPanels) {
            panel.setEnabled(!optionsPanel.isOnDemand());
        }
        if (activePanel != null) {
            for (DownloadPanel panel : downloadPanels) {
                panel.setLocked(panel != activePanel);
            }
        }
        buttonsPanel.setIsDownloading(isDownloading);
    }

    boolean isBusy() {
        for (DownloadPanel panel : downloadPanels) {
            if (panel.getState() == DownloadState.SCANNING ||
                panel.getState() == DownloadState.DOWNLOADING) {
                return true;
            }
        }
        return false;
    }

    void doOnImageFolderChanged() {
        refreshDownloadList();
        updateComponentState();
    }

    void doOnDisplayModeChanged() {
        refreshDownloadList();
        updateComponentState();
    }

    void doOnDemandChanged() {
        updateComponentState();
    }

    private class ButtonsPanel extends JPanel {

        private final JButton closeButton;

        ButtonsPanel() {

            closeButton = new CloseButton();
            closeButton.setFocusable(false);
            closeButton.addActionListener(getCancelAction());

            final JButton helpButton = new JButton();
            helpButton.setIcon(MagicImages.getIcon(MagicIcon.MISSING));
            helpButton.setFocusable(false);
            helpButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WikiPage.show(WikiPage.IMAGE_DOWNLOADS);
                }
            });
            hintPanel.addHintSource(helpButton, String.format("<b>%s</b><br>%s",
                MText.get(_S2), MText.get(_S4)
            ));

            setLayout(new MigLayout("insets 0, alignx right, aligny bottom"));
            add(helpButton);
            add(backgroundButton, "w 100%");
            add(closeButton);
        }

        void setIsDownloading(boolean b) {
            closeButton.setEnabled(!b);
            backgroundButton.setEnabled(b);
        }
    }

    private void doCancelAndClose() {
        for (DownloadPanel panel : downloadPanels) {
            panel.doCancel();
        }
        optionsPanel.saveSettings();
        firePropertyChange(CP_CLOSE, true, false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DownloadPanel.CP_STATE_CHANGED)) {
            updateComponentState();
        }
    }

    synchronized void setMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            errorPanel.setErrorText(message + "\n");
            if (!errorPanel.isVisible()) {
                hintPanel.setVisible(false);
                errorPanel.setVisible(true);
                refreshLayout();
            }
        });
    }

    AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!backgroundButton.isEnabled()) {
                    doCancelAndClose();
                }
            }
        };
    }

}

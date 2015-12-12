package magic.ui.image.download;

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
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.URLUtils;
import magic.ui.dialog.button.CloseButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DialogMainPanel extends JPanel implements PropertyChangeListener {

    static final String CP_CLOSE = "b0fbb0a7";
    static final String CP_RUN_BACKGROUND = "a88997e6";

    // translatable strings
    private static final String _S3 = "Run in background...";

    private final JButton backgroundButton;
    private final OptionsPanel optionsPanel;
    private final List<DownloadPanel> downloadPanels = new ArrayList<>();
    private final ErrorPanel errorPanel = new ErrorPanel();
    private final ButtonsPanel buttonsPanel;
    private final HintPanel hintPanel = new HintPanel();

    DialogMainPanel() {

        optionsPanel = new OptionsPanel();
        optionsPanel.addHintSources(hintPanel);
        optionsPanel.setEnabled(false);
        optionsPanel.addPropertyChangeListener(OptionsPanel.CP_OPTIONS_CHANGED, 
            (e) -> { refreshDownloadList(); }
        );

        backgroundButton = new JButton(UiString.get(_S3));
        backgroundButton.setFocusable(false);
        backgroundButton.addActionListener((a) -> { doRunInBackground(); });
        hintPanel.addHintSource(backgroundButton, String.format("<b>%s</b><br>%s",
            UiString.get(_S3),
            "Closes screen but continues to download images in the background.")
        );

        buttonsPanel = new ButtonsPanel();

        setDownloadPanels();
        setLookAndFeel();
        refreshLayout();
        
    }
    
    private void doRunInBackground() {
        firePropertyChange(CP_RUN_BACKGROUND, true, false);
    }

    private void refreshDownloadList() {
        final DownloadMode mode = optionsPanel.getDownloadMode();
        for (DownloadPanel panel : downloadPanels) {
            panel.refreshDownloadList(mode);
        }
    }

    private void setDownloadPanels() {
        downloadPanels.add(getPlayableDownloaderPanel());
        downloadPanels.add(getUnimplementedDownloaderPanel());
    }

    private void refreshLayout() {
        setLayout(new MigLayout("flowy, gap 0, insets 2 6 6 6"));
        add(optionsPanel, "w 100%, gapbottom 6");
        add(getDownloadPanel(), "w 100%");
        add(hintPanel, "w 100%, h 100%, gaptop 10");
        add(buttonsPanel, "w 100%, h 30!, pushy, aligny bottom");
    }

    private void setLookAndFeel() {
        backgroundButton.setEnabled(false);
    }

    private JPanel getDownloadPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowy, insets 6 0 6 0, gapy 0"));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
        for (DownloadPanel downloadPanel : downloadPanels) {
            panel.add(downloadPanel, "w 100%");
        }
        return panel;
    }

    private DownloadPanel getPlayableDownloaderPanel() {
        final DownloadPanel playableDownloaderPanel = new PlayablePanel(
            optionsPanel.getDownloadMode(),
            optionsPanel.getCardTextLanguage(),
            this
        );
        playableDownloaderPanel.addPropertyChangeListener(DownloadPanel.CP_STATE_CHANGED, this);
        return playableDownloaderPanel;
    }

    private DownloadPanel getUnimplementedDownloaderPanel() {
        final DownloadPanel  unimplementedDownloaderPanel = new UnimplementedPanel(
            optionsPanel.getDownloadMode(),
            optionsPanel.getCardTextLanguage(),
            this
        );
        unimplementedDownloaderPanel.addPropertyChangeListener(DownloadPanel.CP_STATE_CHANGED, this);
        return unimplementedDownloaderPanel;
    }

    private void updateComponentState() {
        boolean isScanning = false;
        boolean isDownloading = false;
        for (DownloadPanel panel : downloadPanels) {
            isScanning |= panel.getState() == DownloadState.SCANNING;
            isDownloading |= panel.getState() == DownloadState.DOWNLOADING;
        }
        optionsPanel.setEnabled(!isScanning && !isDownloading);
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

    private class ButtonsPanel extends JPanel {

        private final JButton closeButton;

        ButtonsPanel() {

            closeButton = new CloseButton();
            closeButton.setFocusable(false);
            closeButton.addActionListener(getCancelAction());

            final JButton helpButton = new JButton();
            helpButton.setIcon(MagicImages.getIcon(MagicIcon.MISSING_ICON));
            helpButton.setFocusable(false);
            helpButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    URLUtils.openURL(URLUtils.URL_WIKI + "UIDownloadImages");
                }
            });
            hintPanel.addHintSource(helpButton, "<b>Wiki help page</b><br>Opens a page containing more information on this screen in your internet browser.");

            setLayout(new MigLayout("insets 0, alignx right, aligny bottom"));
            add(helpButton);
            add(backgroundButton, "w 100%");
            add(closeButton);
        }

        void setIsDownloading(boolean b) {
            closeButton.setEnabled(b == false);
            backgroundButton.setEnabled(b);
        }
    }

    private void doCancelAndClose() {
        for (DownloadPanel panel : downloadPanels) {
            panel.doCancel();
        }
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
                errorPanel.setVisible(true);
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

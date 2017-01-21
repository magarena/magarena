package magic.ui.screen.images.download;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.CardTextLanguage;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel {

    private static final String _S1 = "Images folder:";
    private static final String _S2 = "Card text:";
    private static final String _S3 = "Display:";
    private static final String _S4 = "Preferred card text language";
    private static final String _S5 = "If a language other than English is selected then Magarena will try to find and download a card image for the given language. If no image is found then it will download the default English edition instead.";

    static final String CP_OPTIONS_CHANGED = "fc9a1955-7e59-47d5-83b4-14db558c731e";

    private final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JComboBox<CardTextLanguage> cboCardText = new JComboBox<>();
    private final JComboBox<CardImageDisplayMode> cboDisplayMode = new JComboBox<>();
    private final DirectoryChooser imagesFolderChooser;

    OptionsPanel() {

        imagesFolderChooser = getImagesFolderChooser();
        setCardTextCombo();
        setDisplayModeCombo();

        setLayout(new MigLayout("wrap 2, insets 0", "[right][]"));

        // image folder
        add(new JLabel(MText.get(_S1)));
        add(imagesFolderChooser, "w 100%");
        // card text language
        add(new JLabel(MText.get(_S2)));
        add(cboCardText);
        // download mode
        add(getBoldLabel(MText.get(_S3)));
        add(cboDisplayMode);
    }

    private JLabel getBoldLabel(String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
    }

    private void saveSettings() {
        CONFIG.setCardTextLanguage(getCardTextLanguage());
        CONFIG.setCardImagesPath(imagesFolderChooser.getPath());
        CONFIG.save();
    }

    private void doImageFolderChanged() {
        setEnabled(false);
        saveSettings();
        firePropertyChange(CP_OPTIONS_CHANGED, true, false);
    }

    private DirectoryChooser getImagesFolderChooser() {
        final DirectoryChooser chooser = new DirectoryChooser(CONFIG.getCardImagesPath());
        chooser.setFocusable(false);
        chooser.addPropertyChangeListener(DirectoryChooser.CP_FOLDER_CHANGED, (e) -> {
            doImageFolderChanged();
        });
        return chooser;
    }

    private void doTextLanguageChanged() {
        saveSettings();
        firePropertyChange(CP_OPTIONS_CHANGED, true, false);
    }

    private void setCardTextCombo() {
        cboCardText.setModel(new DefaultComboBoxModel<>(CardTextLanguage.values()));
        cboCardText.setSelectedItem(CONFIG.getCardTextLanguage());
        cboCardText.addItemListener((ItemEvent ev) -> {
            if (ev.getStateChange() == ItemEvent.SELECTED) {
                doTextLanguageChanged();
            }
        });
    }

    private void doDisplayModeChanged() {
        setEnabled(false);
        firePropertyChange(CP_OPTIONS_CHANGED, true, false);
        CONFIG.setCardImageDisplayMode((CardImageDisplayMode) cboDisplayMode.getSelectedItem());
    }

    private void setDisplayModeCombo() {
        cboDisplayMode.setFont(cboDisplayMode.getFont().deriveFont(Font.BOLD));
        cboDisplayMode.setModel(new DefaultComboBoxModel<>(CardImageDisplayMode.values()));
        cboDisplayMode.getModel().setSelectedItem(CONFIG.getCardImageDisplayMode());
        cboDisplayMode.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                doDisplayModeChanged();
            }
        });
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component c : getComponents()) {
            c.setEnabled(b);
        }
    }

    CardImageDisplayMode getDisplayMode() {
        return (CardImageDisplayMode) cboDisplayMode.getSelectedItem();
    }

    CardTextLanguage getCardTextLanguage() {
        return (CardTextLanguage) cboCardText.getSelectedItem();
    }

    void addHintSources(HintPanel hintPanel) {
        imagesFolderChooser.addHintSources(hintPanel);
        hintPanel.addHintSource(cboCardText, String.format("<b>%s</b><br>%s",
            MText.get(_S4), MText.get(_S5)
        ));
    }

}

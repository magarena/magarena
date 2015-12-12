package magic.ui.image.download;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.CardTextLanguage;
import magic.ui.widget.DirectoryChooser;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel {

    static final String CP_OPTIONS_CHANGED = "1b7206a7";

    private final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JComboBox<CardTextLanguage> cboCardText = new JComboBox<>();
    private final JComboBox<DownloadMode> cboDownloadMode = new JComboBox<>();
    private final DirectoryChooser imagesFolderChooser;

    OptionsPanel() {

        imagesFolderChooser = getImagesFolderChooser();
        setDownloadModeCombo();

        setLayout(new MigLayout("wrap 2, insets 0", "[right][]"));
        add(new JLabel(UiString.get("Images folder:")));
        add(imagesFolderChooser, "w 100%");
        add(new JLabel(UiString.get("Card text:")));
        add(getCardTextCombo());
        add(getBoldLabel(UiString.get("Download mode:")));
        add(cboDownloadMode);
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
        cboCardText.setToolTipText(getCardTextLanguage().isEnglish()
            ? ""
            : "<html><b>Preferred Language</b><br>Downloads selected translation of card if available.</html>"
        );
        saveSettings();
    }

    private JComboBox<CardTextLanguage> getCardTextCombo() {
        cboCardText.setModel(new DefaultComboBoxModel<>(CardTextLanguage.values()));
        cboCardText.setSelectedItem(CONFIG.getCardTextLanguage());
        cboCardText.addItemListener((ItemEvent ev) -> {
            if (ev.getStateChange() == ItemEvent.SELECTED) {
                doTextLanguageChanged();
            }
        });
        return cboCardText;
    }

    private void doDownloadModeChanged() {
        setEnabled(false);
        firePropertyChange(CP_OPTIONS_CHANGED, true, false);
    }

    private void setDownloadModeCombo() {
        cboDownloadMode.setFont(cboDownloadMode.getFont().deriveFont(Font.BOLD));
        cboDownloadMode.setModel(new DefaultComboBoxModel<>(DownloadMode.values()));
        cboDownloadMode.getModel().setSelectedItem(cboDownloadMode.getItemAt(0));
        cboDownloadMode.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                doDownloadModeChanged();
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

    JComboBox<DownloadMode> getDownloadTypeCombo() {
        return cboDownloadMode;
    }

    DownloadMode getDownloadMode() {
        return (DownloadMode) cboDownloadMode.getSelectedItem();
    }

    CardTextLanguage getCardTextLanguage() {
        return (CardTextLanguage) cboCardText.getSelectedItem();
    }

}

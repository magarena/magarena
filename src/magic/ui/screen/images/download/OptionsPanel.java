package magic.ui.screen.images.download;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.CardTextLanguage;
import magic.ui.dialog.prefs.ImageSizePresets;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel {

    private static final String _S2 = "Preferred card text:";
    private static final String _S3 = "Display card image as:";
    private static final String _S4 = "Preferred card text language";
    private static final String _S5 = "If a language other than English is selected then Magarena will first try to download a card image with text in that language. If no image is found then it will download the default English card image instead.";
    private static final String _S6 = "Download on demand:";
    private static final String _S7 = "Downloads card images as needed. Recommended if you have a fast internet connection.";
    private static final String _S8 = "Preferred display size:";
    private static final String _S9 = "Sets the size of the card image displayed in card explorer and deck editor as well as popup images during a game. Select 'Default' to display the card image at its native size. Otherwise it will be scaled up or down to the selected preset.";
    private static final String _S10 = "*** This has no affect on the size of an image that is downloaded ***";
    private static final String _S11 = "*** Not applicable to rendered proxy images ***";
    private static final String _S12 = "Yes";
    private static final String _S13 = "No";

    private final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JComboBox<CardTextLanguage> cboCardText = new JComboBox<>();
    private final JComboBox<CardImageDisplayMode> cboDisplayMode = new JComboBox<>();
    private final DirectoryChooser imagesFolderChooser;
    private final JComboBox<Boolean> cboOnDemand = new JComboBox<>();
    private final JComboBox<ImageSizePresets> cboDisplaySize = new JComboBox<>();
    private final DialogMainPanel listener;

    OptionsPanel(DialogMainPanel listener) {

        this.listener = listener;

        imagesFolderChooser = getImagesFolderChooser();
        imagesFolderChooser.setBold(true);

        setDisplayModeCombo();
        setDisplaySizeCombo();
        setCardTextCombo();
        setOnDemandCombo();

        setLayout(new MigLayout("wrap 2, insets 0", "[right, 40%][60%]"));

        add(imagesFolderChooser, "w 100%, spanx 2");
        add(getBoldLabel(MText.get(_S3)));
        add(cboDisplayMode);
        add(getBoldLabel(MText.get(_S2)));
        add(cboCardText);
        add(getBoldLabel(MText.get(_S8)));
        add(cboDisplaySize);
        add(getBoldLabel(MText.get(_S6)));
        add(cboOnDemand);

        setOptionsState();
    }

    private JLabel getBoldLabel(String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
    }

    void saveSettings() {
        CONFIG.setCardImageDisplayMode(getCardImageDisplayMode());
        CONFIG.setPreferredImageSize(getPreferredImageSize());
        CONFIG.setImagesOnDemand(isOnDemand());
        if (getCardImageDisplayMode() == CardImageDisplayMode.PRINTED) {
            CONFIG.setCardTextLanguage(getCardTextLanguage());
        }
    }

    private DirectoryChooser getImagesFolderChooser() {
        final DirectoryChooser chooser = new DirectoryChooser(CONFIG.getCardImagesPath());
        chooser.setFocusable(false);
        chooser.addPropertyChangeListener(DirectoryChooser.CP_FOLDER_CHANGED, (e) -> {
            CONFIG.setCardImagesPath(chooser.getPath());
            listener.doOnImageFolderChanged();
        });
        return chooser;
    }

    private void setCardTextCombo() {
        cboCardText.setModel(new DefaultComboBoxModel<>(CardTextLanguage.values()));
        cboCardText.setSelectedItem(CONFIG.getCardTextLanguage());
        cboCardText.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (getCardImageDisplayMode() == CardImageDisplayMode.PRINTED) {
                    CONFIG.setCardTextLanguage(getCardTextLanguage());
                }
            }
        });
    }

    private void setOptionsState() {
        if (getCardImageDisplayMode() == CardImageDisplayMode.PROXY) {
            cboCardText.setSelectedItem(CardTextLanguage.ENGLISH);
            cboCardText.setEnabled(false);
        } else {
            cboCardText.setSelectedItem(CONFIG.getCardTextLanguage());
            cboCardText.setEnabled(true);
        }
    }

    private void doDisplayModeChanged() {
        setOptionsState();
        listener.doOnDisplayModeChanged();
    }

    private void doOnDemandChanged() {
        CONFIG.setImagesOnDemand(isOnDemand());
        listener.doOnDemandChanged();
    }

    private void setOnDemandCombo() {
        cboOnDemand.setModel(new DefaultComboBoxModel<>(new Boolean[]{true, false}));
        cboOnDemand.getModel().setSelectedItem(CONFIG.getImagesOnDemand());
        cboOnDemand.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText((Boolean) value == true ? MText.get(_S12) : MText.get(_S13));
                setForeground(cboOnDemand.isEnabled() ? getForeground() : Color.GRAY);
                return this;
            }
        });
        cboOnDemand.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                doOnDemandChanged();
            }
        });
    }

    private void setDisplayModeCombo() {
        cboDisplayMode.setModel(new DefaultComboBoxModel<>(CardImageDisplayMode.values()));
        cboDisplayMode.getModel().setSelectedItem(CONFIG.getCardImageDisplayMode());
        cboDisplayMode.addItemListener((final ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                doDisplayModeChanged();
            }
        });
    }

    private void setDisplaySizeCombo() {
        cboDisplaySize.setModel(new DefaultComboBoxModel<>(ImageSizePresets.values()));
        cboDisplaySize.getModel().setSelectedItem(CONFIG.getPreferredImageSize());
    }

    void addHintSources(HintPanel hintPanel) {
        imagesFolderChooser.addHintSources(hintPanel);
        hintPanel.addHintSource(cboCardText, String.format("<b>%s</b><br>%s<br><br><b>%s</b>",
            MText.get(_S4), MText.get(_S5), MText.get(_S11)
        ));
        hintPanel.addHintSource(cboOnDemand, String.format("<b>%s</b><br>%s",
            MText.get(_S6), MText.get(_S7)
        ));
        hintPanel.addHintSource(cboDisplaySize, String.format("<b>%s</b><br>%s<br><br><b>%s</b>",
            MText.get(_S8), MText.get(_S9), MText.get(_S10)
        ));
    }

    boolean isOnDemand() {
        return (Boolean) cboOnDemand.getSelectedItem();
    }

    CardTextLanguage getCardTextLanguage() {
        return (CardTextLanguage) cboCardText.getSelectedItem();
    }

    CardImageDisplayMode getCardImageDisplayMode() {
        return (CardImageDisplayMode) cboDisplayMode.getSelectedItem();
    }

    private ImageSizePresets getPreferredImageSize() {
        return (ImageSizePresets) cboDisplaySize.getSelectedItem();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        imagesFolderChooser.setEnabled(b);
        cboDisplayMode.setEnabled(b);
        cboCardText.setEnabled(b && getCardImageDisplayMode() == CardImageDisplayMode.PRINTED);
        cboDisplaySize.setEnabled(b);
        cboOnDemand.setEnabled(b);
    }

}

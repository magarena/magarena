package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.screen.images.download.DirectoryChooser;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MCheckBox;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class GeneralPanel extends JPanel {

    // translatable strings.
    private static final String _S1 = "Download images on demand.";
    private static final String _S2 = "Only downloads card images as needed. Switch off if you want to download all images in one go, use proxy frames with cropped images or download non-English card images.";
    private static final String _S56 = "Restart required. Only applies to the general UI, it does not affect card data.";
    private static final String _S57 = "Deck Editor split view.";
    private static final String _S58 = "Use the old style split view in the Deck Editor instead of the new tabbed view. This option is provided for convenience, any new features will only be added to the tabbed view.";
    private static final String _S59 = "Preview card on select only.";
    private static final String _S60 = "By default, as you move the mouse cursor over a card entry it will display the image. If you find this a bit too sensitive then this setting will only change the image when the card entry is selected.";
    private static final String _S61 = "Show unimplemented card data.";
    private static final String _S62 = "If set then the Card Explorer will display extra data for each unimplemented card otherwise it will only show the card name. This setting can affect the time it takes the Card Explorer screen to open the first time it is accessed.";
    private static final String _S63 = "User Interface";
    private static final String _S64 = "Card Explorer & Deck Editor";
    private static final String _S83 = "Card images";

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private final TranslationPanel langPanel;
    private final MCheckBox splitViewDeckEditorCheckBox;
    private final MCheckBox previewCardOnSelectCheckBox;
    private final MCheckBox missingCardDataCheckbox;
    private final PreferredSizePanel preferredSizePanel;
    private final DirectoryChooser imagesFolderChooser;
    private final MCheckBox imagesOnDemandCheckbox;

    GeneralPanel(MouseListener aListener) {

        langPanel = new TranslationPanel();
        langPanel.setToolTipText(MText.get(_S56));
        langPanel.setFocusable(false);
        langPanel.addMouseListener(aListener);

        imagesFolderChooser = new DirectoryChooser(config.getCardImagesPath());
        imagesFolderChooser.setFocusable(false);
        imagesFolderChooser.addMouseListener(aListener);

        preferredSizePanel = new PreferredSizePanel(aListener);
        preferredSizePanel.setFocusable(false);

        imagesOnDemandCheckbox = new MCheckBox(MText.get(_S1), config.getImagesOnDemand());
        imagesOnDemandCheckbox.setToolTipText(MText.get(_S2));
        imagesOnDemandCheckbox.setFocusable(false);
        imagesOnDemandCheckbox.addMouseListener(aListener);

        splitViewDeckEditorCheckBox = new MCheckBox(MText.get(_S57), config.isSplitViewDeckEditor());
        splitViewDeckEditorCheckBox.setToolTipText(MText.get(_S58));
        splitViewDeckEditorCheckBox.setFocusable(false);
        splitViewDeckEditorCheckBox.addMouseListener(aListener);

        previewCardOnSelectCheckBox = new MCheckBox(MText.get(_S59), config.isPreviewCardOnSelect());
        previewCardOnSelectCheckBox.setToolTipText(MText.get(_S60));
        previewCardOnSelectCheckBox.setFocusable(false);
        previewCardOnSelectCheckBox.addMouseListener(aListener);

        missingCardDataCheckbox = new MCheckBox(MText.get(_S61), config.showMissingCardData());
        missingCardDataCheckbox.setToolTipText(MText.get(_S62));
        missingCardDataCheckbox.setFocusable(false);
        missingCardDataCheckbox.addMouseListener(aListener);

        setLayout(new MigLayout("flowy, gapy 4, insets 16"));

        // lang
        add(getCaptionLabel(MText.get(_S63)));
        add(langPanel, "w 100%");
        // images
        add(getCaptionLabel(MText.get(MText.get(_S83))), "gaptop 10");
        add(imagesFolderChooser, "w 100%");
        add(preferredSizePanel, "w 100%");
        add(imagesOnDemandCheckbox.component());
        // explorer & editor
        add(getCaptionLabel(MText.get(_S64)), "gaptop 10");
        add(splitViewDeckEditorCheckBox.component());
        add(previewCardOnSelectCheckBox.component());
        add(missingCardDataCheckbox.component());
    }

    void saveSettings() {
        preferredSizePanel.saveSettings();
        config.setCardImagesPath(imagesFolderChooser.getPath());
        config.setTranslation(langPanel.getSelectedLanguage());
        config.setIsSplitViewDeckEditor(splitViewDeckEditorCheckBox.isSelected());
        config.setPreviewCardOnSelect(previewCardOnSelectCheckBox.isSelected());
        config.setShowMissingCardData(missingCardDataCheckbox.isSelected());
        config.setImagesOnDemand(imagesOnDemandCheckbox.isSelected());
    }

    private JLabel getCaptionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

    String getLanguage() {
        return langPanel.getSelectedLanguage();
    }

    void refreshLanguageCombo() {
        langPanel.refreshLanguageCombo();
    }

}

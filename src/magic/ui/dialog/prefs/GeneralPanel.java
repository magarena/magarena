package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.widget.M.MCheckBox;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class GeneralPanel extends JPanel {

    // translatable strings.
    private static final String _S56 = "Restart required. Only applies to the general UI, it does not affect card data.";
    private static final String _S57 = "Deck Editor split view.";
    private static final String _S58 = "Use the old style split view in the Deck Editor instead of the new tabbed view. This option is provided for convenience, any new features will only be added to the tabbed view.";
    private static final String _S59 = "Preview card on select only.";
    private static final String _S60 = "By default, as you move the mouse cursor over a card entry it will display the image. If you find this a bit too sensitive then this setting will only change the image when the card entry is selected.";
    private static final String _S63 = "User Interface";
    private static final String _S64 = "Card Explorer & Deck Editor";
    private static final String _S10 = "Save game statistics.";
    private static final String _S11 = "Keeps a record of each game played to generate Played/Won/Lost totals and play history for each deck. The data is stored in \\Magarena\\stats.";

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private final TranslationPanel langPanel;
    private final MCheckBox splitViewDeckEditorCheckBox;
    private final MCheckBox previewCardOnSelectCheckBox;
    private final MCheckBox gameStatsCheckbox;

    GeneralPanel(MouseListener aListener) {

        langPanel = new TranslationPanel();
        langPanel.setToolTipText(MText.get(_S56));
        langPanel.setFocusable(false);
        langPanel.addMouseListener(aListener);

        splitViewDeckEditorCheckBox = new MCheckBox(MText.get(_S57), config.isSplitViewDeckEditor());
        splitViewDeckEditorCheckBox.setToolTipText(MText.get(_S58));
        splitViewDeckEditorCheckBox.setFocusable(false);
        splitViewDeckEditorCheckBox.addMouseListener(aListener);

        previewCardOnSelectCheckBox = new MCheckBox(MText.get(_S59), config.isPreviewCardOnSelect());
        previewCardOnSelectCheckBox.setToolTipText(MText.get(_S60));
        previewCardOnSelectCheckBox.setFocusable(false);
        previewCardOnSelectCheckBox.addMouseListener(aListener);

        gameStatsCheckbox = new MCheckBox(MText.get(_S10), config.isGameStatsEnabled());
        gameStatsCheckbox.setToolTipText(MText.get(_S11));
        gameStatsCheckbox.setFocusable(false);
        gameStatsCheckbox.addMouseListener(aListener);

        setLayout(new MigLayout("flowy, gapy 4, insets 16"));

        // lang
        add(getCaptionLabel(MText.get(_S63)));
        add(langPanel, "w 100%");
        // explorer & editor
        add(getCaptionLabel(MText.get(_S64)), "gaptop 10");
        add(splitViewDeckEditorCheckBox.component());
        add(previewCardOnSelectCheckBox.component());
        //
        add(gameStatsCheckbox.component(), "w 100%, gaptop 20");
    }

    void saveSettings() {
        config.setTranslation(langPanel.getSelectedLanguage());
        config.setIsSplitViewDeckEditor(splitViewDeckEditorCheckBox.isSelected());
        config.setPreviewCardOnSelect(previewCardOnSelectCheckBox.isSelected());
        config.setGameStatsEnabled(gameStatsCheckbox.isSelected());
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

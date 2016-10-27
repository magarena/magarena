package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.widget.M.MCheckBox;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class GameplayImagesPanel extends JPanel {

    // translatable strings.
    private static final String _S28 = "Popup card image using mouse wheel.";
    private static final String _S29 = "Manually display the card image popup by moving the mouse wheel forwards. Overrides the Auto-Popup delay.";
    private static final String _S30 = "Popup Delay";
    private static final String _S31 = "Automatically displays the card popup image after the specified number of milliseconds that the mouse cursor hovers over a card.";
    private static final String _S34 = "Pause game on popup.";
    private static final String _S35 = "Pauses the game while the popup is open.";
    private static final String _S41 = "Highlight";
    private static final String _S42 = "none";
    private static final String _S43 = "overlay";
    private static final String _S44 = "border";
    private static final String _S45 = "theme";
    private static final String _S46 = "Determines the style in which cards are highlighted during a game.";

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MCheckBox mouseWheelPopupCheckBox;
    private final SliderPanel popupDelaySlider;
    private final MCheckBox pauseGamePopupCheckBox;
    private final JComboBox<String> highlightComboBox;

    GameplayImagesPanel(final MouseListener aListener) {

        // Card highlight setting.
        final JLabel highlightLabel = new JLabel(MText.get(_S41));
        final String[] Highlightchoices = {
            MText.get(_S42),
            MText.get(_S43),
            MText.get(_S44),
            MText.get(_S45)
        };
        highlightComboBox = new JComboBox<>(Highlightchoices);
        highlightComboBox.setSelectedItem(CONFIG.getHighlight());
        highlightComboBox.setToolTipText(MText.get(_S46));
        highlightComboBox.setFocusable(false);
        highlightComboBox.addMouseListener(aListener);

        mouseWheelPopupCheckBox = new MCheckBox(MText.get(_S28), CONFIG.isMouseWheelPopup());
        mouseWheelPopupCheckBox.setFocusable(false);
        mouseWheelPopupCheckBox.setToolTipText(MText.get(_S29));
        mouseWheelPopupCheckBox.addMouseListener(aListener);

        popupDelaySlider = new SliderPanel(MText.get(_S30), 0, 5000, 100, CONFIG.getPopupDelay());
        popupDelaySlider.setToolTipText(MText.get(_S31));
        popupDelaySlider.addMouseListener(aListener);

        pauseGamePopupCheckBox = new MCheckBox(MText.get(_S34), CONFIG.isGamePausedOnPopup());
        pauseGamePopupCheckBox.setFocusable(false);
        pauseGamePopupCheckBox.setToolTipText(MText.get(_S35));
        pauseGamePopupCheckBox.addMouseListener(aListener);

        setLayout(new MigLayout("flowx, wrap 2, insets 16, gapy 10"));
        add(highlightLabel);
        add(highlightComboBox);
        add(pauseGamePopupCheckBox.component(), "spanx 2");
        add(mouseWheelPopupCheckBox.component(), "spanx 2");
        add(popupDelaySlider, "w 100%, spanx 2");
    }

    void saveSettings() {
        CONFIG.setMouseWheelPopup(mouseWheelPopupCheckBox.isSelected());
        CONFIG.setPopupDelay(popupDelaySlider.getValue());
        CONFIG.setIsGamePausedOnPopup(pauseGamePopupCheckBox.isSelected());
        CONFIG.setHighlight(highlightComboBox.getItemAt(highlightComboBox.getSelectedIndex()));
    }

}

package magic.ui.prefs;

import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.UiString;
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

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final PreferredSizePanel preferredSizePanel;
    private final JCheckBox mouseWheelPopupCheckBox;
    private final SliderPanel popupDelaySlider;
    private final JCheckBox pauseGamePopupCheckBox;
    
    GameplayImagesPanel(final MouseListener aListener) {

        preferredSizePanel = new PreferredSizePanel(aListener);

        mouseWheelPopupCheckBox = new JCheckBox(UiString.get(_S28), CONFIG.isMouseWheelPopup());
        mouseWheelPopupCheckBox.setFocusable(false);
        mouseWheelPopupCheckBox.setToolTipText(UiString.get(_S29));
        mouseWheelPopupCheckBox.addMouseListener(aListener);

        popupDelaySlider = new SliderPanel(UiString.get(_S30), 0, 5000, 100, CONFIG.getPopupDelay());
        popupDelaySlider.setToolTipText(UiString.get(_S31));
        popupDelaySlider.addMouseListener(aListener);

        pauseGamePopupCheckBox = new JCheckBox(UiString.get(_S34), CONFIG.isGamePausedOnPopup());
        pauseGamePopupCheckBox.setFocusable(false);
        pauseGamePopupCheckBox.setToolTipText(UiString.get(_S35));
        pauseGamePopupCheckBox.addMouseListener(aListener);

        setLayout(new MigLayout("flowy, insets 16, gapy 10"));
        add(preferredSizePanel);
        add(pauseGamePopupCheckBox);
        add(mouseWheelPopupCheckBox);
        add(popupDelaySlider, "w 100%");
    }
    
    void saveSettings() {
        preferredSizePanel.saveSettings();
        CONFIG.setMouseWheelPopup(mouseWheelPopupCheckBox.isSelected());
        CONFIG.setPopupDelay(popupDelaySlider.getValue());
        CONFIG.setIsGamePausedOnPopup(pauseGamePopupCheckBox.isSelected());
    }

}

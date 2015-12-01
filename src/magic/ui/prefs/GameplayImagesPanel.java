package magic.ui.prefs;

import java.awt.event.MouseListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class GameplayImagesPanel extends JPanel {

    // translatable strings.
    private static final String _S1  = "Preferred image size:";
    private static final String _S2  = "Select 'Original' to display popup image at its native size otherwise it will be scaled up or down to the selected preset. The image will also be resized to fit if there is not enough room to display it at its preferred size.";
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

    private class PreferredSizePanel extends JPanel {

        private final String[] PRESETS = new String[] {
                "Original",
                "223 x 310",
                "265 x 370",
                "312 x 445",    // magiccards.info
                "480 x 680",    // mtgimage.com
                "680 x 960",
                "745 x 1040"
        };

        private final JComboBox<String> cboPresets = new JComboBox<>();

        PreferredSizePanel(MouseListener aListener) {
            setLayout(new MigLayout("insets 0"));
            add(new JLabel(UiString.get(_S1)));
            add(getSizePresetsCombo(aListener));
            addMouseListener(aListener);
        }

        private String getTooltip() {
            return UiString.get(_S2);
        }

        private JComboBox<String> getSizePresetsCombo(MouseListener aListener) {
            cboPresets.setModel(new DefaultComboBoxModel<>(PRESETS));
            cboPresets.getModel().setSelectedItem(CONFIG.getPreferredImageSize());
            cboPresets.setToolTipText(getTooltip());
            cboPresets.addMouseListener(aListener);
            return cboPresets;
        }

        void saveSettings() {
            CONFIG.setPreferredImageSize((String)cboPresets.getSelectedItem());
        }

        @Override
        public String getToolTipText() {
            return getTooltip();
        }

    }

}

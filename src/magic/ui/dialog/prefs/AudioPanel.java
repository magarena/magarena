package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.MagicSound;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AudioPanel extends JPanel {

    private static final String _S1 = "UI volume";
    private static final String _S2 = "Game volume";
    private static final String _S9 = "Enables or disables sound effects used by the user interface in general. For example, this affects whether a sound is played when the missing images alert is displayed.";
    private static final String _S11 = "Enables or disables sound effects during a game, such as end of turn, stack resolution, combat damage and win/lose sounds.";

    private final static GeneralConfig config = GeneralConfig.getInstance();

    private final VolumeSliderPanel uiVolumeSlider;
    private final VolumeSliderPanel gameVolumeSlider;

    AudioPanel(MouseListener aListener) {

        uiVolumeSlider = new VolumeSliderPanel(config.getUiVolume(), MagicSound.ALERT);
        uiVolumeSlider.setToolTipText(UiString.get(_S9));
        uiVolumeSlider.setFocusable(false);
        uiVolumeSlider.addMouseListener(aListener);

        gameVolumeSlider = new VolumeSliderPanel(config.getGameVolume(), MagicSound.COMBAT);
        gameVolumeSlider.setToolTipText(UiString.get(_S11));
        gameVolumeSlider.setFocusable(false);
        gameVolumeSlider.addMouseListener(aListener);

        setLayout(new MigLayout("flowy, gapy 0, insets 16"));
        add(getCaptionLabel(UiString.get(_S1)));
        add(uiVolumeSlider, "w 100%");
        add(getCaptionLabel(UiString.get(_S2)), "gaptop 16");
        add(gameVolumeSlider, " w 100%");
        addMouseListener(aListener);
    }

    void saveSettings() {
        config.setUiVolume(uiVolumeSlider.getValue());
        config.setGameVolume(gameVolumeSlider.getValue());
    }

    private JLabel getCaptionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

}

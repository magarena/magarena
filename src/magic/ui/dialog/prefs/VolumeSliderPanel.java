package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import magic.ui.MagicSound;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class VolumeSliderPanel extends JPanel {

    private final JSlider slider;

    public VolumeSliderPanel(int initialValue, MagicSound aSound) {

        slider = new JSlider(0, 100, initialValue);
        slider.setFocusable(false);
        slider.addChangeListener((event) -> {
            if (!slider.getValueIsAdjusting()) {
                aSound.play(slider.getValue());
            }
        });

        setLayout(new MigLayout());
        add(new JLabel("Off"));
        add(slider, "w 100%");
        add(new JLabel("100%"));
    }

    public int getValue() {
        return slider.getValue();
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        slider.addMouseListener(l);
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text); //To change body of generated methods, choose Tools | Templates.
        slider.setToolTipText(text);
    }

}

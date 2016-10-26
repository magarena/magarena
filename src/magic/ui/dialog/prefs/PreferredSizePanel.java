package magic.ui.dialog.prefs;

import java.awt.event.MouseListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.translate.MText;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class PreferredSizePanel extends JPanel {

    private static final String _S1 = "Preferred size:";
    private static final String _S2 = "Select 'Original' to display card image at its native size otherwise it will be scaled up or down to the selected preset. The image will also be resized to fit if there is not enough room to display it at its preferred size.";

    private final static GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JComboBox<ImageSizePresets> cboPresets = new JComboBox<>();

    PreferredSizePanel(MouseListener aListener) {
        setLayout(new MigLayout("insets 0"));
        add(new JLabel(MText.get(_S1)));
        add(getSizePresetsCombo(aListener));
        addMouseListener(aListener);
    }

    private String getTooltip() {
        return MText.get(_S2);
    }

    private JComboBox<ImageSizePresets> getSizePresetsCombo(MouseListener aListener) {
        cboPresets.setModel(new DefaultComboBoxModel<>(ImageSizePresets.values()));
        cboPresets.getModel().setSelectedItem(CONFIG.getPreferredImageSize());
        cboPresets.setToolTipText(getTooltip());
        cboPresets.addMouseListener(aListener);
        return cboPresets;
    }

    void saveSettings() {
        CONFIG.setPreferredImageSize((ImageSizePresets) cboPresets.getSelectedItem());
    }

    @Override
    public String getToolTipText() {
        return getTooltip();
    }

}

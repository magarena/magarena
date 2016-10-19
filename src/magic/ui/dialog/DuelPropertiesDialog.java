package magic.ui.dialog;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import magic.data.MagicFormat;
import magic.translate.UiString;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.widget.SliderPanel;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelPropertiesDialog extends MagicDialog {

    // translatable strings
    private static final String _S1 = "Duel Properties";
    private static final String _S2 = "Initial life:";
    private static final String _S3 = "Hand size:";
    private static final String _S4 = "Max. games:";
    private static final String _S5 = "Cube:";

    private final SliderPanel handSizeSliderPanel;
    private final SliderPanel lifeSliderPanel;
    private final SliderPanel winsSliderPanel;
    private final JComboBox<MagicFormat> cubeComboBox;
    private boolean isCancelled = false;

    // CTR : edit an existing profile.
    public DuelPropertiesDialog(
        final int handSize,
        final int initialLife,
        final int maxGames,
        final MagicFormat cube
    ) {

        super(UiString.get(_S1), new Dimension(380, 260));

        lifeSliderPanel = new SliderPanel(UiString.get(_S2), (MagicSystem.isDevMode() ? 1 : 10), 30, 5, initialLife, false);
        handSizeSliderPanel = new SliderPanel(UiString.get(_S3), 6, 8, 1, handSize);
        winsSliderPanel = new SliderPanel(UiString.get(_S4), 1, 11, 2, maxGames);

        cubeComboBox = new JComboBox<>(MagicFormat.getDuelFormatsArray());
        cubeComboBox.setLightWeightPopupEnabled(false);
        cubeComboBox.setFocusable(false);
        cubeComboBox.setSelectedItem(cube);
        cubeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                MagicFormat fmt = (MagicFormat)value;
                value = fmt.getLabel();
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        refreshLayout();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        setVisible(true);
    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0 10"));
        panel.add(lifeSliderPanel, "w 100%");
        panel.add(handSizeSliderPanel, "w 100%");
        panel.add(winsSliderPanel, "w 100%");
        panel.add(getCubePanel(), "w 100%");
        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    private JPanel getCubePanel() {
        final JPanel panel = new JPanel(new MigLayout());
        final JLabel lbl = new JLabel(UiString.get(_S5));
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        panel.add(lbl);
        panel.add(cubeComboBox, "w 100%");
        return panel;
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right"));
        buttonPanel.add(getCancelButton());
        buttonPanel.add(getSaveButton());
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
        btn.addActionListener(getCancelAction());
        return btn;
    }

    private JButton getSaveButton() {
        final JButton btn = new SaveButton();
        btn.addActionListener(getSaveAction());
        return btn;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getStartLife() {
        return lifeSliderPanel.getValue();
    }

    public int getHandSize() {
        return handSizeSliderPanel.getValue();
    }

    public int getNrOfGames() {
        return winsSliderPanel.getValue();
    }

    public MagicFormat getCube() {
        return cubeComboBox.getItemAt(cubeComboBox.getSelectedIndex());
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose();
            }
        };
    }

    private AbstractAction getSaveAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
    }

}

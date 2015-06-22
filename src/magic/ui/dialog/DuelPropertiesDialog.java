package magic.ui.dialog;

import magic.utility.MagicSystem;
import magic.data.MagicFormat;
import magic.ui.MagicFrame;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DuelPropertiesDialog extends JDialog {

    private final SliderPanel handSizeSliderPanel;
    private final SliderPanel lifeSliderPanel;
    private final SliderPanel winsSliderPanel;
    private final JComboBox<MagicFormat> cubeComboBox;
    private boolean isCancelled = false;

    // CTR : edit an existing profile.
    public DuelPropertiesDialog(
        final MagicFrame frame,
        final int handSize,
        final int initialLife,
        final int maxGames,
        final MagicFormat cube
    ) {

        super(frame, true);
        this.setTitle("Duel Properties");
        this.setSize(300, 280);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        lifeSliderPanel = new SliderPanel("Initial life:", null, (MagicSystem.isDevMode() ? 1 : 10), 30, 5, initialLife, false);
        handSizeSliderPanel = new SliderPanel("Hand size:", null, 6, 8, 1, handSize);
        winsSliderPanel = new SliderPanel("Max. games:", null, 1, 11, 2, maxGames);

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

        getContentPane().setLayout(new MigLayout("flowy", "", "[][45!]"));
        getContentPane().add(lifeSliderPanel, "w 100%");
        getContentPane().add(handSizeSliderPanel, "w 100%");
        getContentPane().add(winsSliderPanel, "w 100%");
        getContentPane().add(getCubePanel(), "w 100%");
        getContentPane().add(getButtonPanel(), "w 100%, h 40!");

        setEscapeKeyAction();

        setVisible(true);
    }

    private JPanel getCubePanel() {
        final JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel("Cube:"));
        panel.add(cubeComboBox, "w 100%");
        return panel;
    }

    private void setEscapeKeyAction() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");
        root.getActionMap().put("closeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("alignx right"));
        buttonPanel.add(getSaveButton(), "w 80!");
        buttonPanel.add(getCancelButton(), "w 80!");
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new JButton("Cancel");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose();
            }
        });
        return btn;
    }

    private JButton getSaveButton() {
        final JButton btn = new JButton("Save");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
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

}

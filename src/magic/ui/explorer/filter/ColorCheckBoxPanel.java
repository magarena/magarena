package magic.ui.explorer.filter;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicColor;
import magic.ui.MagicImages;
import magic.ui.MagicUI;
import magic.ui.explorer.filter.buttons.FilterPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ColorCheckBoxPanel extends JPanel {

    private final JCheckBox cb;
    private final JLabel lbl;

    ColorCheckBoxPanel(MagicColor color, FilterPanel fbp) {

        setOpaque(false);

        cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setFocusPainted(true);
        cb.setAlignmentY(Component.CENTER_ALIGNMENT);
        cb.setActionCommand(Character.toString(color.getSymbol()));
        cb.addActionListener((e) -> {
            MagicUI.showBusyCursorFor(cb);
            fbp.filterChanged();
            MagicUI.showDefaultCursorFor(cb);
        });

        final MouseAdapter onMouseClicked = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MagicUI.showBusyCursorFor(e.getComponent());
                cb.doClick();
                MagicUI.showDefaultCursorFor(e.getComponent());
            }
        };

        lbl = new JLabel();
        lbl.setIcon(MagicImages.getIcon(color.getManaType()));
        lbl.addMouseListener(onMouseClicked);

        addMouseListener(onMouseClicked);

        setLayout(new MigLayout("insets 4"));
        add(cb);
        add(lbl);
    }

    boolean isSelected() {
        return cb.isSelected();
    }

    void setSelected(boolean b) {
        cb.setSelected(b);
    }

}

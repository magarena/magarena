package magic.ui.widget.card.filter;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicColor;
import magic.ui.MagicImages;
import magic.ui.helpers.MouseHelper;
import magic.ui.widget.card.filter.button.FilterPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ColorCheckBoxPanel extends JPanel
    implements IFilterCheckBox {

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
            MouseHelper.showBusyCursor(cb);
            fbp.filterChanged();
            MouseHelper.showDefaultCursor(cb);
        });

        final MouseAdapter onMouseClicked = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MouseHelper.showBusyCursor(e.getComponent());
                cb.doClick();
                MouseHelper.showDefaultCursor(e.getComponent());
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

    @Override
    public boolean isSelected() {
        return cb.isSelected();
    }

    @Override
    public void setSelected(boolean b) {
        cb.setSelected(b);
    }

    @Override
    public String getText() {
        return cb.getText();
    }

}

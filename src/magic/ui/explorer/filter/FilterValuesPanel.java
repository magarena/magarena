package magic.ui.explorer.filter;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import magic.ui.theme.ThemeFactory;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class FilterValuesPanel extends JPanel {

    private final JCheckBox[] checkboxes;

    FilterValuesPanel(Object[] values, ActionListener aListener) {

        setLayout(new MigLayout("flowy, insets 2"));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setOpaque(false);

        checkboxes = new JCheckBox[values.length];
        for (int i = 0; i < values.length; i++) {
            checkboxes[i] = new JCheckBox(values[i].toString().replace('_', ' '));
            checkboxes[i].addActionListener(aListener);
            checkboxes[i].setOpaque(false);
            checkboxes[i].setForeground(ThemeFactory.getTheme().getTextColor());
            checkboxes[i].setFocusPainted(true);
            checkboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            add(checkboxes[i]);
        }
    }

    JCheckBox[] getCheckboxes() {
        return checkboxes;
    }
}

package magic.ui.explorer.filter;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import magic.ui.MagicUI;
import magic.ui.explorer.filter.buttons.FilterPanel;

@SuppressWarnings("serial")
public class CheckboxValuesPanel extends FilterValuesPanel {

    private final JCheckBox[] checkboxes;

    CheckboxValuesPanel(Object[] values, FilterPanel fbp) {
        super(fbp);
        setAlignmentX(Component.LEFT_ALIGNMENT);        

        checkboxes = new JCheckBox[values.length];
        for (int i = 0; i < values.length; i++) {
            final JCheckBox cb = new FilterCheckBox(values[i].toString());
            cb.addActionListener((e) -> {
                MagicUI.showBusyCursorFor(cb);
                fbp.filterChanged();
                MagicUI.showDefaultCursorFor(cb);
            });
            checkboxes[i] = cb;
            add(checkboxes[i]);
        }
    }

    List<Integer> getSelectedItemIndexes() {
        final List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                selected.add(i);
            }
        }
        return selected;
    }

    int getItemsCount() {
        return checkboxes.length;
    }

    boolean isItemSelected(int i) {
        return checkboxes[i].isSelected();
    }

    void reset() {
        for (JCheckBox cb : checkboxes) {
            cb.setSelected(false);
        }
    }

    boolean hasSelectedItem() {
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                return true;
            }
        }
        return false;
    }

    String getItemText(int i) {
        return checkboxes[i].getText();
    }

}

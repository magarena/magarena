package magic.ui.widget.card.filter;

import java.awt.Component;
import magic.ui.MouseHelper;
import magic.ui.widget.card.filter.button.FilterPanel;

@SuppressWarnings("serial")
public class CheckboxValuesPanel extends CheckBoxFilterValuesPanel {

    private final FilterCheckBox[] checkboxes;

    CheckboxValuesPanel(Object[] values, FilterPanel fbp) {
        super(fbp);
        setAlignmentX(Component.LEFT_ALIGNMENT);        

        checkboxes = new FilterCheckBox[values.length];
        for (int i = 0; i < values.length; i++) {
            final FilterCheckBox cb = new FilterCheckBox(values[i].toString());
            cb.addActionListener((e) -> {
                MouseHelper.showBusyCursor(cb);
                fbp.filterChanged();
                MouseHelper.showDefaultCursor(cb);
            });
            checkboxes[i] = cb;
            add(checkboxes[i]);
        }
    }

    @Override
    protected IFilterCheckBox[] getCheckBoxes() {
        return checkboxes;
    }

}

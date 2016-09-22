package magic.ui.explorer.filter;

import java.awt.Component;
import magic.ui.MagicUI;
import magic.ui.explorer.filter.buttons.FilterPanel;

@SuppressWarnings("serial")
public class CheckboxValuesPanel extends FilterValuesPanel {

    private final FilterCheckBox[] checkboxes;

    CheckboxValuesPanel(Object[] values, FilterPanel fbp) {
        super(fbp);
        setAlignmentX(Component.LEFT_ALIGNMENT);        

        checkboxes = new FilterCheckBox[values.length];
        for (int i = 0; i < values.length; i++) {
            final FilterCheckBox cb = new FilterCheckBox(values[i].toString());
            cb.addActionListener((e) -> {
                MagicUI.showBusyCursorFor(cb);
                fbp.filterChanged();
                MagicUI.showDefaultCursorFor(cb);
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

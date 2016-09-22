package magic.ui.explorer.filter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import magic.ui.explorer.filter.buttons.FilterPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public abstract class FilterValuesPanel extends TexturedPanel
    implements IMultiSelectFilter {

    private static final Color OPACITY_COLOR = new Color(255, 255, 255, 160);

    protected abstract IFilterCheckBox[] getCheckBoxes();

    public FilterValuesPanel(FilterPanel fbp) {
        setBackground(OPACITY_COLOR);
        setLayout(fbp.getFilterPanelLayout());
    }

    @Override
    public boolean hasSelectedItem() {
        for (IFilterCheckBox cb : getCheckBoxes()) {
            if (cb.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemsCount() {
        return getCheckBoxes().length;
    }

    @Override
    public boolean isItemSelected(int i) {
        return getCheckBoxes()[i].isSelected();
    }

    @Override
    public List<Integer> getSelectedItemIndexes() {
        final List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < getCheckBoxes().length; i++) {
            if (getCheckBoxes()[i].isSelected()) {
                selected.add(i);
            }
        }
        return selected;
    }

    @Override
    public void reset() {
        for (IFilterCheckBox cb : getCheckBoxes()) {
            cb.setSelected(false);
        }
    }

    @Override
    public String getItemText(int i) {
        return getCheckBoxes()[i].getText();
    }
}

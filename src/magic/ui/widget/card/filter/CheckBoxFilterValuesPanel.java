package magic.ui.widget.card.filter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import magic.ui.widget.TexturedPanel;
import magic.ui.widget.card.filter.button.FilterPanel;

@SuppressWarnings("serial")
abstract class CheckBoxFilterValuesPanel extends TexturedPanel
    implements IMultiSelectFilter {

    private static final Color OPACITY_COLOR = new Color(255, 255, 255, 160);

    protected abstract IFilterCheckBox[] getCheckBoxes();

    CheckBoxFilterValuesPanel(FilterPanel fbp) {
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

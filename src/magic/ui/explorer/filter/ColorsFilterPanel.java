package magic.ui.explorer.filter;

import java.util.ArrayList;
import java.util.List;
import magic.model.MagicColor;
import magic.ui.explorer.filter.buttons.FilterPanel;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class ColorsFilterPanel extends FilterValuesPanel
    implements IMultiSelectFilter {

    private final ColorCheckBoxPanel[] colorPanels;

    public ColorsFilterPanel(FilterPanel fbp) {
        super(fbp);
        setBorder(FontsAndBorders.DOWN_BORDER);

        colorPanels = new ColorCheckBoxPanel[MagicColor.NR_COLORS];

        for (int i = 0; i < MagicColor.NR_COLORS; i++) {            
            colorPanels[i] = new ColorCheckBoxPanel(MagicColor.values()[i], fbp);
            add(colorPanels[i]);
        }
    }

    @Override
    public boolean hasSelectedItem() {
        for (ColorCheckBoxPanel cb : colorPanels) {
            if (cb.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> getSelectedItemIndexes() {
        final List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < colorPanels.length; i++) {
            if (colorPanels[i].isSelected()) {
                selected.add(i);
            }
        }
        return selected;
    }

    @Override
    public int getItemsCount() {
        return colorPanels.length;
    }

    @Override
    public boolean isItemSelected(int i) {
        return colorPanels[i].isSelected();
    }

    @Override
    public void reset() {
        for (ColorCheckBoxPanel cb : colorPanels) {
            cb.setSelected(false);
        }
    }

    @Override
    public String getItemText(int i) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

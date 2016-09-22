package magic.ui.explorer.filter;

import magic.model.MagicColor;
import magic.ui.explorer.filter.buttons.FilterPanel;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class ColorValuesPanel extends FilterValuesPanel {

    private final ColorCheckBoxPanel[] colorPanels;

    public ColorValuesPanel(FilterPanel fbp) {
        super(fbp);
        setBorder(FontsAndBorders.DOWN_BORDER);

        colorPanels = new ColorCheckBoxPanel[MagicColor.NR_COLORS];

        for (int i = 0; i < MagicColor.NR_COLORS; i++) {            
            colorPanels[i] = new ColorCheckBoxPanel(MagicColor.values()[i], fbp);
            add(colorPanels[i]);
        }
    }

    @Override
    protected IFilterCheckBox[] getCheckBoxes() {
        return colorPanels;
    }
}

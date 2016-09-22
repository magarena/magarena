package magic.ui.explorer.filter;

import java.awt.Color;
import magic.ui.explorer.filter.buttons.FilterPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public abstract class FilterValuesPanel extends TexturedPanel {

    private static final Color OPACITY_COLOR = new Color(255, 255, 255, 160);

    public FilterValuesPanel(FilterPanel fbp) {
        setBackground(OPACITY_COLOR);
        setLayout(fbp.getFilterPanelLayout());
    }

}

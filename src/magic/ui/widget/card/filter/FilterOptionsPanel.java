package magic.ui.widget.card.filter;

import magic.ui.widget.card.filter.button.FilterPanel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FilterOptionsPanel extends JPanel {

    private final SearchOperandCombo searchOptionCombo;

    public FilterOptionsPanel(final FilterPanel fbp) {

        searchOptionCombo = new SearchOperandCombo(fbp);

        setLayout(new MigLayout("insets 0, flowy, gap 0",
                "[fill, grow, center]", // columns
                "[fill, grow, center]" // rows
        ));
        add(searchOptionCombo);
        add(new FilterResetButton(fbp));

        setOpaque(false);
    }

    public void reset() {
        searchOptionCombo.setSelectedIndex(0);
    }

    public boolean isSelected(SearchOperand searchOption) {
        return searchOptionCombo.isSelected(searchOption);
    }

    public String getSearchOperandText() {
        return searchOptionCombo.getSelectedOperandText();
    }

}

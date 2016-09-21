package magic.ui.explorer.filter;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class FilterOptionsPanel extends JPanel {

    private final SearchOperandCombo searchOptionCombo;

    FilterOptionsPanel(final FilterButtonPanel fbp) {

        searchOptionCombo = new SearchOperandCombo(fbp);

        setLayout(new MigLayout("insets 0, flowy, gap 0",
                "[fill, grow, center]", // columns
                "[fill, grow, center]" // rows
        ));
        add(searchOptionCombo);
        add(new FilterResetButton(fbp));

        setOpaque(false);
    }

    void reset() {
        searchOptionCombo.setSelectedIndex(0);
    }

    boolean isSelected(SearchOperand searchOption) {
        return searchOptionCombo.isSelected(searchOption);
    }

    String getSearchOperandText() {
        return searchOptionCombo.getSelectedOperandText();
    }

}

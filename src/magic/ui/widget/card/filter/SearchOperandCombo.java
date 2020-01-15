package magic.ui.widget.card.filter;

import java.awt.event.ItemEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import magic.ui.helpers.MouseHelper;
import magic.ui.widget.card.filter.button.FilterPanel;

@SuppressWarnings("serial")
class SearchOperandCombo extends JComboBox<SearchOperand> {

    SearchOperandCombo(final FilterPanel fbp) {
        setModel(new DefaultComboBoxModel<>(SearchOperand.getValues(fbp.hideSearchOperandAND())));
        setLightWeightPopupEnabled(false);
        setFocusable(false);
        ((JLabel) getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        setSelectedIndex(0);
        addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(() -> {
                    MouseHelper.showBusyCursor();
                    fbp.filterChanged();
                    MouseHelper.showDefaultCursor();
                });
            }
        });
    }

    private SearchOperand getSelectedOperand() {
        return ((SearchOperand) getSelectedItem());
    }

    boolean isSelected(SearchOperand searchOption) {
        return getSelectedOperand() == searchOption;
    }

    String getSelectedOperandText() {
        return getSelectedOperand().toString();
    }
}

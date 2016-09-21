package magic.ui.explorer.filter;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.ui.MagicImages;
import magic.ui.MagicUI;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
class ColorFilterDialog extends FilterDialog {

    private class FilterValuesPanel extends JPanel {

        private final JCheckBox[] checkboxes;

        FilterValuesPanel() {
            checkboxes = new JCheckBox[MagicColor.values().length];
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(FontsAndBorders.DOWN_BORDER);
            setOpaque(false);
            for (int i = 0; i < MagicColor.NR_COLORS; i++) {
                final MagicColor color = MagicColor.values()[i];
                final JPanel colorPanel = new JPanel();
                colorPanel.setOpaque(false);
                final JCheckBox cb = new JCheckBox("", false);
                cb.setOpaque(false);
                cb.setFocusPainted(true);
                cb.setAlignmentY(Component.CENTER_ALIGNMENT);
                cb.setActionCommand(Character.toString(color.getSymbol()));
                cb.addActionListener((e) -> {
                    MagicUI.showBusyCursorFor(cb);
                    fbp.filterChanged();
                    MagicUI.showDefaultCursorFor(cb);
                });
                checkboxes[i] = cb;
                colorPanel.add(checkboxes[i]);
                colorPanel.add(new JLabel(MagicImages.getIcon(color.getManaType())));
                add(colorPanel);
            }
        }

        private JCheckBox[] getCheckboxes() {
            return checkboxes;
        }

        private boolean hasSelectedCheckbox() {
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) {
                    return true;
                }
            }
            return false;
        }

        private List<Integer> getSelected() {
            final List<Integer> selected = new ArrayList<>();
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    selected.add(i);
                }
            }
            return selected;
        }
    }

    private final FilterButtonPanel fbp;
    private final FilterValuesPanel filterPanel;
    private final FilterOptionsPanel filterOptionsPanel;

    ColorFilterDialog(final FilterButtonPanel fbp, Object[] filterValues) {

        this.fbp = fbp;
        this.filterPanel = new FilterValuesPanel();
        this.filterOptionsPanel = new FilterOptionsPanel(fbp);

        setSize(fbp.getFilterDialogSize());

        setLayout(fbp.getFilterDialogLayout());
        add(filterPanel);
        add(filterOptionsPanel);        
    }

    @Override
    protected boolean isFiltering() {
        return filterPanel.hasSelectedCheckbox();
    }

    @Override
    List<Integer> getSelectedItemIndexes() {
        return filterPanel.getSelected();
    }

    @Override
    boolean filterMatches(MagicCardDefinition aCard) {

        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i = 0; i < filterPanel.getCheckboxes().length; i++) {
            if (filterPanel.getCheckboxes()[i].isSelected()) {
                somethingSelected = true;
                if (fbp.isCardValid(aCard, i)) {
                    resultOR = true;
                } else {
                    resultAND = false;
                }
            }
        }
        if (filterOptionsPanel.isSelected(SearchOperand.EXCLUDE)) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected
                || ((filterOptionsPanel.isSelected(SearchOperand.MATCH_ANY) && resultOR)
                || (filterOptionsPanel.isSelected(SearchOperand.MATCH_ALL) && resultAND));
    }

    @Override
    void reset() {
        for (JCheckBox checkbox : filterPanel.getCheckboxes()) {
            checkbox.setSelected(false);
        }
        filterOptionsPanel.reset();
    }

    @Override
    String getSearchOperandText() {
        return filterOptionsPanel.getSearchOperandText();
    }


}

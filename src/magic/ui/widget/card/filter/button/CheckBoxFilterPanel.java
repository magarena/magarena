package magic.ui.widget.card.filter.button;

import java.awt.LayoutManager;
import java.util.List;
import magic.model.MagicCardDefinition;
import magic.ui.widget.card.filter.dialog.CheckboxFilterDialog;
import magic.ui.widget.card.filter.dialog.FilterDialog;
import magic.ui.widget.card.filter.IFilterListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class CheckBoxFilterPanel extends FilterPanel {

    protected CheckboxFilterDialog cbDialog;
    protected String[] values;

    protected abstract String[] getFilterValues();
    public abstract boolean isCardValid(final MagicCardDefinition card, final int i);

    CheckBoxFilterPanel(String text, IFilterListener aListener) {
        super(text, aListener);
    }

    protected String getFilterTooltip(Object[] values, List<Integer> selected) {
        final StringBuilder sb = new StringBuilder();
        if (!selected.isEmpty()) {
            sb.append("<html><b><p>").append(cbDialog.getSearchOperandText()).append("</p></b>");
            for (Integer i : selected) {
                sb.append("• ").append(values[i]).append("<br>");
            }
            sb.append("</html>");
        }
        return sb.toString();
    }

    @Override
    protected boolean isFiltering() {
        return cbDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, cbDialog.getSelectedItemIndexes());
    }

    @Override
    public boolean matches(MagicCardDefinition aCard) {
        return cbDialog == null ? true : cbDialog.filterMatches(aCard);
    }

    protected CheckboxFilterDialog getCheckBoxFilterDialog() {
        return new CheckboxFilterDialog(this, values);
    }

    @Override
    protected FilterDialog getFilterDialog() {
        if (cbDialog == null) {
            values = getFilterValues();
            cbDialog = getCheckBoxFilterDialog();
        }
        return cbDialog;
    }

    @Override
    public LayoutManager getFilterPanelLayout() {
        return new MigLayout("flowy, insets 2, gapy 4");
    }
}

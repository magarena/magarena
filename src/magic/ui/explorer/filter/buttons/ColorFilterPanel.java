package magic.ui.explorer.filter.buttons;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.List;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.translate.UiString;
import magic.ui.explorer.filter.dialogs.ColorFilterDialog;
import magic.ui.explorer.filter.dialogs.FilterDialog;
import magic.ui.explorer.filter.IFilterListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ColorFilterPanel extends FilterPanel {

    // translatable strings
    private static final String _S11 = "Color";

    private ColorFilterDialog cbDialog;

    public ColorFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S11), aListener);
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(280, 94);
    }

    public boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasColor(MagicColor.values()[i]);
    }

    @Override
    protected boolean isFiltering() {
        return cbDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(
                MagicColor.values(),
                cbDialog.getSelectedItemIndexes()
        );
    }

    @Override
    protected FilterDialog getFilterDialog() {
        if (cbDialog == null) {
            cbDialog = new ColorFilterDialog(this, MagicColor.values());
        }
        return cbDialog;
    }

    @Override
    public boolean matches(MagicCardDefinition aCard) {
        return cbDialog == null ? true : cbDialog.filterMatches(aCard);
    }

    private String getFilterTooltip(Object[] values, List<Integer> selected) {
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
    public LayoutManager getFilterPanelLayout() {
        return new MigLayout("insets 0");
    }
}

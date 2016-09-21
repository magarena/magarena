package magic.ui.explorer.filter;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ManaCostFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S12 = "Cost";

    private static final String[] COST_VALUES = new String[MagicManaCost.MAXIMUM_MANA_COST + 1];
    static {
        for (int i = 0; i <= MagicManaCost.MAXIMUM_MANA_COST; i++) {
            COST_VALUES[i] = Integer.toString(i);
        }
    }

    private final ScrollableFilterPane filterPane;

    ManaCostFBP(IFilterListener aListener) {
        super(UiString.get(_S12));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(COST_VALUES, this);
        this.filterPane.setMigLayout(new MigLayout("flowx, wrap 6, insets 2, gap 8"));
        setPopupContent();
    }

    @Override
    protected Dimension getPopupDialogSize() {
        return new Dimension(300, 130);
    }

    @Override
    protected boolean hideSearchOptionsAND() {
        return true;
    }

    @Override
    protected IFilterListener getSearchOptionsListener() {
        return filterListener;
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        return filterPane.getCheckboxes();
    }

    @Override
    protected JComponent getFilterValuesComponent() {
        return filterPane;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(COST_VALUES, filterPane.getSelected());
    }

}

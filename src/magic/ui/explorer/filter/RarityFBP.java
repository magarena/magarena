package magic.ui.explorer.filter;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.model.MagicCardDefinition;
import magic.model.MagicRarity;
import magic.translate.UiString;

@SuppressWarnings("serial")
class RarityFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S14 = "Rarity";

    private final ScrollableFilterPane filterPane;
    private final String[] values;

    RarityFBP(IFilterListener aListener) {
        super(UiString.get(_S14));
        this.values = MagicRarity.getDisplayNames();
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(values, this);
        setPopupContent();
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
        return card.isRarity(MagicRarity.values()[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, filterPane.getSelected());
    }

}

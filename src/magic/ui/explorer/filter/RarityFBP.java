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

    RarityFBP(IFilterListener aListener) {
        super(UiString.get(_S14));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(MagicRarity.getDisplayNames(), this);
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

}

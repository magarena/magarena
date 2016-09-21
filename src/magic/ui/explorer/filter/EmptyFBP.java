package magic.ui.explorer.filter;

import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class EmptyFBP extends FilterButtonPanel {

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    void reset() {
        // not applicable.
    }

    @Override
    protected boolean hasActiveFilterValue() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected boolean matches(MagicCardDefinition aCard) {
        return true;
    }

    @Override
    protected String getSearchOperandText() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected FilterDialog getFilterDialog() {
        throw new UnsupportedOperationException("Not supported.");
    }
}

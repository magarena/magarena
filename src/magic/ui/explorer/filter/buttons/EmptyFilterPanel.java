package magic.ui.explorer.filter.buttons;

import magic.model.MagicCardDefinition;
import magic.ui.explorer.filter.dialogs.FilterDialog;

@SuppressWarnings("serial")
public class EmptyFilterPanel extends FilterPanel {

    @Override
    public void reset() {
        // not applicable.
    }

    @Override
    protected boolean isFiltering() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean matches(MagicCardDefinition aCard) {
        return true;
    }

    @Override
    protected FilterDialog getFilterDialog() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected String getFilterTooltip() {
        throw new UnsupportedOperationException("Not supported.");
    }
}

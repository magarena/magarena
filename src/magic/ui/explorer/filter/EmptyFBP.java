package magic.ui.explorer.filter;

import javax.swing.JCheckBox;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class EmptyFBP extends FilterButtonPanel {

    @Override
    boolean doesNotInclude(MagicCardDefinition cardDefinition) {
        return false;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    void reset() {
        // not applicable.
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    protected boolean hasActiveFilterValue() {
        throw new UnsupportedOperationException("Not applicable.");
    }
}

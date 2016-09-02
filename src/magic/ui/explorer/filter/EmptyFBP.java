package magic.ui.explorer.filter;

import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class EmptyFBP extends FilterButtonPanel {

    EmptyFBP() {
        super("");
    }

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

}

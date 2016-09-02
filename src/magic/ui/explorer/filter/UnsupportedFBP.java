package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class UnsupportedFBP extends FilterButtonPanel {

    private final String[] statuses;

    UnsupportedFBP(ActionListener aListener) {
        super("Unsupported");
        statuses = MagicCardDefinition.getUnsupportedStatuses();
        setPopupContent(statuses, false, aListener);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasStatus(statuses[i]);
    }

}

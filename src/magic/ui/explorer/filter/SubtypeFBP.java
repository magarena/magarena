package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.model.MagicCardDefinition;
import magic.model.MagicSubType;
import magic.translate.UiString;

@SuppressWarnings("serial")
class SubtypeFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S13 = "Subtype";

    SubtypeFBP(ActionListener aListener) {
        super(UiString.get(_S13));
        setPopupContent(MagicSubType.values(), true, aListener);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasSubType(MagicSubType.values()[i]);
    }

}

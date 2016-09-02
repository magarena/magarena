package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class FormatFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S7 = "Format";

    FormatFBP(ActionListener aListener) {
        super(UiString.get(_S7));
        setPopupContent(MagicPredefinedFormat.getFilterValues(), false, aListener);
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicPredefinedFormat.values().get(i);
        return fmt.isCardLegal(card);
    }

}

package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class CubeFBP extends FilterButtonPanel {

    // translatable strings
     private static final String _S6 = "Cube";

    CubeFBP(ActionListener aListener) {
        super(UiString.get(_S6));
        setPopupContent(MagicFormat.getCubeFilterLabels(), false, aListener);
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
        return fmt.isCardLegal(card);
    }

}

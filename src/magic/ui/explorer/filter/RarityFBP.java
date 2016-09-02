package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.model.MagicCardDefinition;
import magic.model.MagicRarity;
import magic.translate.UiString;

@SuppressWarnings("serial")
class RarityFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S14 = "Rarity";

    RarityFBP(ActionListener aListener) {
        super(UiString.get(_S14));
        setPopupContent(MagicRarity.getDisplayNames(), true, aListener);
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.isRarity(MagicRarity.values()[i]);
    }

}

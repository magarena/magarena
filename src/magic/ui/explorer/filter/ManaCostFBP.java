package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.translate.UiString;

@SuppressWarnings("serial")
class ManaCostFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S12 = "Cost";

    private static final String[] COST_VALUES = new String[MagicManaCost.MAXIMUM_MANA_COST + 1];
    static {
        for (int i = 0; i <= MagicManaCost.MAXIMUM_MANA_COST; i++) {
            COST_VALUES[i] = Integer.toString(i);
        }
    }

    ManaCostFBP(ActionListener aListener) {
        super(UiString.get(_S12));
        setPopupContent(
            "flowx, wrap 5, insets 2, gap 8",
            COST_VALUES,
            true,
            aListener
        );
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
    }

}

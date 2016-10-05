package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.model.MagicRarity;
import magic.translate.UiString;
import magic.ui.widget.card.filter.IFilterListener;

@SuppressWarnings("serial")
public class RarityFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S14 = "Rarity";

    public RarityFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S14), aListener);
    }

    @Override
    public boolean hideSearchOperandAND() {
        return true;
    }

    @Override
    public boolean isCardValid(MagicCardDefinition card, int i) {
        return card.isRarity(MagicRarity.values()[i]);
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(260, 200);
    }

    @Override
    protected String[] getFilterValues() {
        return MagicRarity.getDisplayNames();
    }
}

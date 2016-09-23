package magic.ui.explorer.filter.buttons;

import magic.model.MagicCardDefinition;
import magic.model.MagicSubType;
import magic.translate.UiString;
import magic.ui.explorer.filter.IFilterListener;

@SuppressWarnings("serial")
public class SubTypeFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S13 = "Subtype";

    public SubTypeFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S13), aListener);
    }

    @Override
    public  boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasSubType(MagicSubType.values()[i]);
    }

    @Override
    protected String[] getFilterValues() {
        return MagicSubType.FILTER_SUBTYPES.stream()
                .map(s -> s.name().replace('_', ' '))
                .toArray(size -> new String[size]);
    }
}

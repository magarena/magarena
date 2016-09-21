package magic.ui.explorer.filter.buttons;

import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.explorer.filter.IFilterListener;

@SuppressWarnings("serial")
public class CubeFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S6 = "Cube";

    public CubeFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S6), aListener);
    }

    @Override
    public  boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
        return fmt.isCardLegal(card);
    }

    @Override
    protected String[] getFilterValues() {
        return MagicFormat.getCubeFilterLabels();
    }
}

package magic.ui.explorer.filter.buttons;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.ui.explorer.filter.IFilterListener;

@SuppressWarnings("serial")
public class UnsupportedFilterPanel extends CheckBoxFilterPanel {

    public UnsupportedFilterPanel(IFilterListener aListener) {
        super("Unsupported", aListener);
    }

    @Override
    public boolean hideSearchOperandAND() {
        return true;
    }

    @Override
    public boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasStatus(values[i]);
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(300, 300);
    }

    @Override
    protected String[] getFilterValues() {
        return MagicCardDefinition.getUnsupportedStatuses();
    }
}

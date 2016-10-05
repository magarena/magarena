package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.ui.widget.card.filter.IFilterListener;

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

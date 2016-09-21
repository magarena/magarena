package magic.ui.explorer.filter;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class UnsupportedFBP extends FilterButtonPanel {

    private final String[] statuses;
    private final ScrollableFilterPane filterPane;

    UnsupportedFBP(IFilterListener aListener) {
        super("Unsupported");
        statuses = MagicCardDefinition.getUnsupportedStatuses();
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(statuses, this);
        setPopupContent();
    }

    @Override
    protected Dimension getPopupDialogSize() {
        return new Dimension(300, 300);
    }
        
    @Override
    protected boolean hideSearchOptionsAND() {
        return true;
    }

    @Override
    protected IFilterListener getSearchOptionsListener() {
        return filterListener;
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        return filterPane.getCheckboxes();
    }

    @Override
    protected JComponent getFilterValuesComponent() {
        return filterPane;
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasStatus(statuses[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(statuses, filterPane.getSelected());
    }

}

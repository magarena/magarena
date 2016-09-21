package magic.ui.explorer.filter;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class CubeFBP extends FilterButtonPanel {

    // translatable strings
     private static final String _S6 = "Cube";

    private final ScrollableFilterPane filterPane;

    CubeFBP(IFilterListener aListener) {
        super(UiString.get(_S6));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(MagicFormat.getCubeFilterLabels(), this);
        setPopupContent();
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
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
        return fmt.isCardLegal(card);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

}

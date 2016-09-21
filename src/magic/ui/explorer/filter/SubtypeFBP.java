package magic.ui.explorer.filter;

import java.util.EnumSet;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.model.MagicCardDefinition;
import magic.model.MagicSubType;
import magic.translate.UiString;

@SuppressWarnings("serial")
class SubtypeFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S13 = "Subtype";

    private final ScrollableFilterPane filterPane;

    SubtypeFBP(IFilterListener aListener) {
        super(UiString.get(_S13));
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(
                EnumSet.allOf(MagicSubType.class).stream()
                .map(s -> s.name().replace('_', ' ')),
                this
        );
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
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasSubType(MagicSubType.values()[i]);
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

}

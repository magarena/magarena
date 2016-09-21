package magic.ui.explorer.filter;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import magic.model.MagicCardDefinition;
import magic.model.MagicType;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
class TypeFBP extends FilterButtonPanel {

    // translatable strings
    @StringContext(eg = "Type filter in cards explorer")
    private static final String _S8 = "Type";
    private static final String _S20 = "Token";
    private static final String _S21 = "Transform";
    private static final String _S22 = "Flip";
    private static final String _S23 = "Hidden";
    private static final String _S24 = "Split";

    private final ScrollableFilterPane filterPane;
    private final Object[] values;

    TypeFBP(IFilterListener aListener, boolean isDeckEditor) {
        super(UiString.get(_S8));
        this.values = getTypeFilterValues(isDeckEditor);
        this.filterListener = aListener;
        this.filterPane = new ScrollableFilterPane(values, this);
        setPopupContent();
    }

    private String[] getTypeFilterValues(boolean isDeckEditor) {

        final List<String> types = MagicType.FILTER_TYPES.stream()
            .map(MagicType::getDisplayName)
            .collect(Collectors.toList());

        if (!isDeckEditor) {
            types.add(UiString.get(_S20));
            types.add(UiString.get(_S21));
            types.add(UiString.get(_S22));
            if (MagicSystem.isDevMode()) {
                types.add(UiString.get(_S23));
            }
            types.add(UiString.get(_S24));
        }
        return types.toArray(new String[types.size()]);
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
        if (getCheckboxes()[i].getText().equals(UiString.get(_S20))) {
            return card.isToken();
        } else if (getCheckboxes()[i].getText().equals(UiString.get(_S21))) {
            return card.isDoubleFaced();
        } else if (getCheckboxes()[i].getText().equals(UiString.get(_S22))) {
            return card.isFlipCard();
        } else if (getCheckboxes()[i].getText().equals(UiString.get(_S23))) {
            return card.isHidden();
        } else if (getCheckboxes()[i].getText().equals(UiString.get(_S24))) {
            return card.isSplitCard();
        } else {
            return card.hasType(MagicType.FILTER_TYPES.toArray(new MagicType[0])[i]);
        }
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterPane.hasSelectedCheckbox();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, filterPane.getSelected());
    }

}

package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.util.List;
import java.util.stream.Collectors;
import magic.model.MagicCardDefinition;
import magic.model.MagicType;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

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

    private static final MagicType[] MAGIC_TYPES =
            MagicType.FILTER_TYPES.toArray(new MagicType[0]);

    private Object[] values;
    private final boolean isDeckEditor;

    TypeFBP(IFilterListener aListener, boolean isDeckEditor) {
        super(UiString.get(_S8), aListener);
        this.isDeckEditor = isDeckEditor;
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
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        
        final String text = filterDialog.getItemText(i);

        // check first - custom types specifically for use with filter.
        if (UiString.get(_S20).equals(text))
            return card.isToken();        
        else if (UiString.get(_S21).equals(text))
            return card.isDoubleFaced();
        else if (UiString.get(_S22).equals(text))
            return card.isFlipCard();
        else if (UiString.get(_S23).equals(text))
            return card.isHidden();
        else if (UiString.get(_S24).equals(text))
            return card.isSplitCard();
        else
            // "official" types
            return card.hasType(MAGIC_TYPES[i]);

    }

    @Override
    protected boolean hasActiveFilterValue() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return getFilterTooltip(values, filterDialog.getSelectedItemIndexes());
    }

    @Override
    protected Dimension getFilterDialogSize() {
        return new Dimension(260, 300);
    }

    @Override
    protected MigLayout getFilterDialogLayout() {
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow][50!, fill]");
    }

    @Override
    protected String getSearchOperandText() {
        return filterDialog.getSearchOperandText();
    }

    @Override
    protected FilterDialog getFilterDialog() {
        this.values = getTypeFilterValues(isDeckEditor);
        return new CheckboxFilterDialog(this, values);
    }
}

package magic.ui.widget.card.filter.button;

import java.util.List;
import java.util.stream.Collectors;
import magic.model.MagicCardDefinition;
import magic.model.MagicType;
import magic.translate.StringContext;
import magic.translate.MText;
import magic.ui.widget.card.filter.IFilterListener;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class TypeFilterPanel extends CheckBoxFilterPanel {

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

    private final boolean isDeckEditor;

    public TypeFilterPanel(IFilterListener aListener, boolean isDeckEditor) {
        super(MText.get(_S8), aListener);
        this.isDeckEditor = isDeckEditor;
    }

    @Override
    public boolean isCardValid(MagicCardDefinition card, int i) {
        
        final String text = cbDialog.getItemText(i);

        // check first - custom types specifically for use with filter.
        if (MText.get(_S20).equals(text))
            return card.isToken();        
        else if (MText.get(_S21).equals(text))
            return card.isDoubleFaced();
        else if (MText.get(_S22).equals(text))
            return card.isFlipCard();
        else if (MText.get(_S23).equals(text))
            return card.isHidden();
        else if (MText.get(_S24).equals(text))
            return card.isSplitCard();
        else
            // "official" types
            return card.hasType(MAGIC_TYPES[i]);

    }

    @Override
    protected String[] getFilterValues() {

        final List<String> types = MagicType.FILTER_TYPES.stream()
            .map(MagicType::getDisplayName)
            .collect(Collectors.toList());

        if (!isDeckEditor) {
            types.add(MText.get(_S20));
            types.add(MText.get(_S21));
            types.add(MText.get(_S22));
            if (MagicSystem.isDevMode()) {
                types.add(MText.get(_S23));
            }
            types.add(MText.get(_S24));
        }
        return types.toArray(new String[types.size()]);
    }
}

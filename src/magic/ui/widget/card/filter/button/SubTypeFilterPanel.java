package magic.ui.widget.card.filter.button;

import java.util.List;
import java.util.stream.Collectors;
import magic.model.MagicCardDefinition;
import magic.model.MagicSubType;
import magic.translate.UiString;
import magic.ui.widget.card.filter.IFilterListener;

@SuppressWarnings("serial")
public class SubTypeFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S13 = "Subtype";

    private List<MagicSubType> subtypes;

    public SubTypeFilterPanel(IFilterListener aListener) {
        super(UiString.get(_S13), aListener);
    }

    @Override
    public  boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasSubType(subtypes.get(i));
    }

    @Override
    protected String[] getFilterValues() {
        if (subtypes == null) {
            subtypes = MagicSubType.FILTER_SUBTYPES.stream()
                    .sorted((o1, o2) -> o1.name().compareTo(o2.name()))
                    .collect(Collectors.toList());
        }
        return subtypes.stream()
                .map(s -> s.name().replace('_', ' '))
                .toArray(size -> new String[size]);
    }
}

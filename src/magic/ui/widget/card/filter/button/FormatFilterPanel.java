package magic.ui.widget.card.filter.button;

import java.util.stream.Collectors;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.widget.card.filter.IFilterListener;

@SuppressWarnings("serial")
public class FormatFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S7 = "Format";

    public FormatFilterPanel(IFilterListener aListener) {
        super(MText.get(_S7), aListener);
    }

    @Override
    public boolean isCardValid(final MagicCardDefinition card, final int i) {
        return MagicPredefinedFormat.values().get(i).isCardLegal(card);
    }

    @Override
    protected String[] getFilterValues() {
        return MagicPredefinedFormat.values().stream()
                .map(MagicFormat::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }
}

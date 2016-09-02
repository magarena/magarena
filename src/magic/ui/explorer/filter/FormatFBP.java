package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class FormatFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S7 = "Format";

    public FormatFBP(ActionListener aListener) {
        super(UiString.get(_S7));
        setPopupContent(
            MagicPredefinedFormat.getFilterValues(),
            new JCheckBox[MagicPredefinedFormat.values().size()],
            new JRadioButton[FILTER_CHOICES.length],
            false,
            aListener
        );
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicPredefinedFormat.values().get(i);
        return fmt.isCardLegal(card);
    }

}

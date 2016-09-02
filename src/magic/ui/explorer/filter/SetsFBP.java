package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import magic.data.MagicSetDefinitions;
import magic.data.MagicSets;
import magic.model.MagicCardDefinition;
import magic.translate.StringContext;
import magic.translate.UiString;

@SuppressWarnings("serial")
class SetsFBP extends FilterButtonPanel {

    // translatable strings
    @StringContext(eg = "Set filter in Cards Explorer")
    private static final String _S1 = "Set";

    SetsFBP(ActionListener aListener) {
        super(UiString.get(_S1));
        setPopupContent(
            MagicSetDefinitions.getFilterValues(),
            new JCheckBox[MagicSets.values().length],
            new JRadioButton[FILTER_CHOICES.length],
            false,
            aListener
        );
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return  MagicSetDefinitions.isCardInSet(card, MagicSets.values()[i]);
    }

}

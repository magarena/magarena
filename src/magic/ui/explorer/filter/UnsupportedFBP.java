package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
class UnsupportedFBP extends FilterButtonPanel {

    UnsupportedFBP(ActionListener aListener) {
        super("Unsupported");
        setPopupContent(
            MagicCardDefinition.getUnsupportedStatuses().toArray(new String[0]),
            new JCheckBox[MagicCardDefinition.getUnsupportedStatuses().size()],
            false,
            aListener
        );
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        return card.hasStatus(getCheckboxes()[i].getText());
    }

}

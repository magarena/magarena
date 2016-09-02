package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.MagicLogs;

@SuppressWarnings("serial")
class StatusFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S4 = "Status";
    private static final String _S17 = "New cards";
    private static final String _S18 = "Playable";
    private static final String _S19 = "Unimplemented";
    private static final String _S25 = "Potential";

    StatusFBP(ActionListener aListener, boolean isDeckEditor) {
        super(UiString.get(_S4));
        String[] filterValues = getStatusFilterValues(isDeckEditor);
        setPopupContent(
            filterValues,
            new JCheckBox[filterValues.length],
            false,
            aListener
        );
    }

    private String[] getStatusFilterValues(boolean isDeckEditor) {
        return isDeckEditor
            ? new String[]{UiString.get(_S17)}
            : new String[]{UiString.get(_S17), UiString.get(_S18), UiString.get(_S19), UiString.get(_S25)};
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        final String status = getCheckboxes()[i].getText();
        if (status.equals(UiString.get(_S17))) {
            return MagicLogs.isCardInDownloadsLog(card);
        } else if (status.equals(UiString.get(_S18))) {
            return CardDefinitions.isCardPlayable(card);
        } else if (status.equals(UiString.get(_S19))) {
            return CardDefinitions.isCardMissing(card);
        } else if (status.equals(UiString.get(_S25))) {
            return CardDefinitions.isPotential(card);
        } else {
            return true;
        }
    }

}

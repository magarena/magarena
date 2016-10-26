package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.MagicLogs;
import magic.ui.widget.card.filter.IFilterListener;

@SuppressWarnings("serial")
public class StatusFilterPanel extends CheckBoxFilterPanel {

    // translatable strings
    private static final String _S4 = "Status";
    private static final String _S17 = "New cards";
    private static final String _S18 = "Playable";
    private static final String _S19 = "Unimplemented";
    private static final String _S25 = "Potential";

    private final boolean isDeckEditor;

    public StatusFilterPanel(IFilterListener aListener, boolean isDeckEditor) {
        super(MText.get(_S4), aListener);
        this.isDeckEditor = isDeckEditor;
    }

    @Override
    public boolean isCardValid(MagicCardDefinition card, int i) {
        
        final String status = cbDialog.getItemText(i);

        if (MText.get(_S17).equals(status))
            return MagicLogs.isCardInDownloadsLog(card);
        else if (MText.get(_S18).equals(status))
            return CardDefinitions.isCardPlayable(card);
        else if (MText.get(_S19).equals(status))
            return CardDefinitions.isCardMissing(card);
        else if (MText.get(_S25).equals(status))
            return CardDefinitions.isPotential(card);
        else
            return true;
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(260, 160);
    }

    @Override
    protected String[] getFilterValues() {
        return isDeckEditor
            ? new String[]{MText.get(_S17)}
            : new String[]{
                MText.get(_S17),
                MText.get(_S18),
                MText.get(_S19),
                MText.get(_S25)};
    }
}

package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.MagicLogs;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class StatusFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S4 = "Status";
    private static final String _S17 = "New cards";
    private static final String _S18 = "Playable";
    private static final String _S19 = "Unimplemented";
    private static final String _S25 = "Potential";

    private String[] values;
    private final boolean isDeckEditor;

    StatusFBP(IFilterListener aListener, boolean isDeckEditor) {
        super(UiString.get(_S4), aListener);
        this.isDeckEditor = isDeckEditor;
    }

    private String[] getStatusFilterValues(boolean isDeckEditor) {
        return isDeckEditor
            ? new String[]{UiString.get(_S17)}
            : new String[]{UiString.get(_S17), UiString.get(_S18), UiString.get(_S19), UiString.get(_S25)};
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        final String status = filterDialog.getItemText(i);
        if (UiString.get(_S17).equals(status))
            return MagicLogs.isCardInDownloadsLog(card);
        else if (UiString.get(_S18).equals(status))
            return CardDefinitions.isCardPlayable(card);
        else if (UiString.get(_S19).equals(status))
            return CardDefinitions.isCardMissing(card);
        else if (UiString.get(_S25).equals(status))
            return CardDefinitions.isPotential(card);
        else
            return true;
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
        return new Dimension(260, 160);
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
        this.values = getStatusFilterValues(isDeckEditor);
        return new CheckboxFilterDialog(this, values);
    }
}

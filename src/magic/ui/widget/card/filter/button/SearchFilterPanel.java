package magic.ui.widget.card.filter.button;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.translate.MText;
import magic.ui.widget.card.filter.CardFilterTextField;
import magic.ui.widget.card.filter.IFilterListener;
import magic.ui.widget.card.filter.dialog.FilterDialog;
import magic.ui.widget.card.filter.dialog.TextFilterDialog;

@SuppressWarnings("serial")
public class SearchFilterPanel extends FilterPanel {

    // translatable strings
    private static final String _S9 = "Search";
    private static final String _S10 = "Searches name, type, subtype and oracle text.";

    private final CardFilterTextField nameTextField;
    private FilterDialog filterDialog;

    public SearchFilterPanel(IFilterListener aListener) {
        super(MText.get(_S9), MText.get(_S10), aListener);
        this.nameTextField = new CardFilterTextField(this);
    }

    @Override
    public Dimension getFilterDialogSize() {
        return new Dimension(260, 38);
    }

    private boolean isCardValid(MagicCardDefinition card) {
        if (!nameTextField.getSearchTerms().isEmpty()) {
            for (String searchTerm : nameTextField.getSearchTerms()) {
                if (!card.hasText(searchTerm)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected boolean isFiltering() {
        return filterDialog.isFiltering();
    }

    @Override
    protected String getFilterTooltip() {
        return nameTextField.getText().trim();
    }

    @Override
    public boolean matches(MagicCardDefinition aCard) {
        return isCardValid(aCard);
    }

    @Override
    protected FilterDialog getFilterDialog() {
        if (filterDialog == null) {
            filterDialog = new TextFilterDialog(this, this.nameTextField);
        }
        return filterDialog;
    }
}

package magic.ui.explorer.filter;

import java.awt.Dimension;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class TextSearchFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S9 = "Search";
    private static final String _S10 = "Searches name, type, subtype and oracle text.";

    private final CardFilterTextField nameTextField;

    TextSearchFBP(IFilterListener aListener) {
        super(UiString.get(_S9), UiString.get(_S10), aListener);
        this.nameTextField = new CardFilterTextField(this);
    }

    @Override
    protected Dimension getFilterDialogSize() {
        return new Dimension(260, 38);
    }

    @Override
    protected MigLayout getFilterDialogLayout() {
        return new MigLayout("flowy, gap 0, insets 0", "[fill, grow]", "[fill, grow]");
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
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return nameTextField.getText().trim().isEmpty() == false;
    }

    @Override
    protected String getFilterTooltip() {
        return nameTextField.getText().trim();
    }

    @Override
    protected boolean matches(MagicCardDefinition aCard) {
        return isCardValid(aCard);
    }

    @Override
    protected String getSearchOperandText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected FilterDialog getFilterDialog() {
        return new TextFilterDialog(this, this.nameTextField);
    }

}

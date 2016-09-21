package magic.ui.explorer.filter;

import javax.swing.JCheckBox;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class TextSearchFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S9 = "Search";
    private static final String _S10 = "Searches name, type, subtype and oracle text.";

    private final CardFilterTextField nameTextField;

    TextSearchFBP(IFilterListener aListener) {
        super(UiString.get(_S9), UiString.get(_S10));
        this.filterListener = aListener;
        this.nameTextField = new CardFilterTextField(this);
        doLayoutTextSearchPanel();
    }

    private void doLayoutTextSearchPanel() {
        popupDialog.setSize(260, 38);
        popupDialog.getContentPane().add(nameTextField);
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
    boolean doesNotInclude(MagicCardDefinition cardDefinition) {
        return !isCardValid(cardDefinition);
    }

    @Override
    void reset() {
        nameTextField.setText("");
        hidePopup();
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected boolean isFilterActive() {
        return nameTextField.getText().trim().isEmpty() == false;
    }

    @Override
    protected JCheckBox[] getCheckboxes() {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    protected boolean hasActiveFilterValue() {
        return isFilterActive();
    }

}

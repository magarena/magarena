package magic.ui.explorer.filter;

import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.CardFilterTextField;
import magic.ui.ICardFilterPanelListener;

@SuppressWarnings("serial")
class TextSearchFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S9 = "Search";
    private static final String _S10 = "Searches name, type, subtype and oracle text.";

    private CardFilterTextField nameTextField;

    TextSearchFBP(ICardFilterPanelListener aListener) {
        super(UiString.get(_S9), UiString.get(_S10));
        doLayoutTextSearchPanel(aListener);
    }

    private void doLayoutTextSearchPanel(ICardFilterPanelListener aListener) {

        nameTextField = new CardFilterTextField(aListener);

        final JPanel dialogPanel = new DialogContentPanel();
        dialogPanel.add(nameTextField);

        dialog.setSize(260, 38);
        dialog.add(dialogPanel);
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
        dialog.setVisible(false);
        nameTextField.setText("");
    }

    @Override
    protected boolean isCardValid(MagicCardDefinition card, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

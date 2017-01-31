package magic.ui.screen.deck.editor;

import java.util.List;
import magic.model.MagicCardDefinition;
import magic.ui.screen.widget.ActionBarButton;

public interface IDeckEditorView {
    MagicCardDefinition getSelectedCard();
    void setVisible(boolean b);
    void doPlusButtonAction();
    void doMinusButtonAction();
    List<ActionBarButton> getActionButtons();
    public void notifyShowing();
}

package magic.model;

import java.util.Set;
import java.util.concurrent.Callable;
import javax.swing.JComponent;
import magic.exception.UndoClickedException;

public interface IGameController {
    
    <E extends JComponent> E waitForInput(final Callable<E> func) throws UndoClickedException;
    <T> T getChoiceClicked();
    boolean isActionClicked();
    void clearCards();
    void clearValidChoices();
    void disableActionButton(final boolean thinking);
    void enableForwardButton();
    void focusViewers(final int handGraveyard);
    void pause(final int t);
    void setSourceCardDefinition(final MagicSource source);
    void setValidChoices(final Set<?> aValidChoices,final boolean aCombatChoice);
    void showCards(final MagicCardList cards);
    void showMessage(final MagicSource source,final String message);
    void updateGameView();
    void waitForInput() throws UndoClickedException;

    // Choices
    MagicSubType getLandSubTypeChoice(final MagicSource source) throws UndoClickedException;
    boolean getPayBuyBackCostChoice(final MagicSource source, final String costText) throws UndoClickedException;
    MagicColor getColorChoice(final MagicSource source) throws UndoClickedException;
    
}

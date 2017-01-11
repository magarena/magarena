package magic.model;

import java.util.List;
import java.util.Set;
import magic.exception.UndoClickedException;
import magic.model.choice.MagicPlayChoiceResult;

public interface IUIGameController extends IGameController {

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
    void refreshSidebarLayout();
    void setStackFastForward(boolean b);
    boolean isStackFastForward();
    void doStackItemPause();

    // Choices
    MagicSubType getLandSubTypeChoice(final MagicSource source) throws UndoClickedException;
    boolean getPayBuyBackCostChoice(final MagicSource source, final String costText) throws UndoClickedException;
    MagicColor getColorChoice(final MagicSource source) throws UndoClickedException;
    int getMultiKickerCountChoice(final MagicSource source, final MagicManaCost cost, final int maximumCount, final String name) throws UndoClickedException;
    int getSingleKickerCountChoice(final MagicSource source, final MagicManaCost cost, final String name) throws UndoClickedException;
    boolean getMayChoice(final MagicSource source, final String description) throws UndoClickedException;
    boolean getTakeMulliganChoice(final MagicSource source, final MagicPlayer player) throws UndoClickedException;
    int getModeChoice(final MagicSource source, final List<Integer> availableModes) throws UndoClickedException;
    int getPayManaCostXChoice(final MagicSource source, final int maximumX) throws UndoClickedException;
    MagicPlayChoiceResult getPlayChoice(final MagicSource source, final List<MagicPlayChoiceResult> results) throws UndoClickedException;
}

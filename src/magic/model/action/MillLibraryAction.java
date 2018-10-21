package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

/**
 * Action that removes fixed amount of cards from players library and moves them to that player's graveyard.
 */
public class MillLibraryAction extends AbstractMillAction {

    private final int amount;

    /**
     * Mill target player's library (move cards to that player's graveyard).
     *
     * @param aPlayer target player
     * @param aAmount number of cards to move
     */
    public MillLibraryAction(final MagicPlayer aPlayer,final int aAmount) {
        super(aPlayer);
        amount = aAmount;
    }

    protected void setCardsToMill(MagicGame game) {
        final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
        for (final MagicCard card : topN) {
            milledCards.add(card);
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Graveyard
            ));
        }
    }
}

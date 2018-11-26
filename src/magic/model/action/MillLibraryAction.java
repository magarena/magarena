package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicPlayer;

import java.util.List;

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

    @Override
    protected List<MagicCard> getMilledCards() {
        return player.getLibrary().getCardsFromTop(amount);
    }
}

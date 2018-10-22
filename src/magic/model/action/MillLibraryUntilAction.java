package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicType;

import java.util.List;

/**
 * Action that removes cards from players library and moves them to that player's graveyard,
 * until certain card type is seen given number of times (or whole library is milled).
 */
public class MillLibraryUntilAction extends AbstractMillAction {
    private final MagicType cardType;
    private final int cards;

    /**
     * Mill target player's library (move cards to that player's graveyard) until certain amount of certain cards are
     * moved.
     *
     * @param aPlayer   target player
     * @param aCardType type of card to check
     * @param aCards    number of those cards to encounter in order for milling to stop
     */
    public MillLibraryUntilAction(final MagicPlayer aPlayer, final MagicType aCardType, int aCards) {
        super(aPlayer);
        cardType = aCardType;
        cards = aCards;
    }

    @Override
    protected String getMillDescription(int finalCount) {
        return String.format("top %d cards - until %d %s(s) are seen -", finalCount, cards, cardType.getDisplayName());
    }

    @Override
    protected List<MagicCard> getMilledCards() {
        final MagicCardList all = player.getLibrary().getCardsFromTop(Integer.MAX_VALUE);
        final MagicCardList milledCards = new MagicCardList();
        int seenTargets = 0;
        for (final MagicCard card : all) {
            milledCards.add(card);
            if (card.hasType(cardType)) {
                seenTargets++;
            }
            if (seenTargets >= cards) {
                break;
            }
        }
        return milledCards;
    }
}

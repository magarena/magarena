package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;

import java.util.Set;
import java.util.HashSet;

public class ShuffleCardsIntoLibraryAction extends MagicAction {

    private MagicCardList cards;
    private MagicLocationType from;

    public ShuffleCardsIntoLibraryAction(final MagicCardList aCards, final MagicLocationType aFrom) {
        cards = aCards;
        from = aFrom;
    }

    @Override
    public void doAction(final MagicGame game) {
        final boolean[] shouldShuffle = {false, false};
        for (final MagicCard card : cards) {
            game.doAction(new ShiftCardAction(card, from, MagicLocationType.TopOfOwnersLibrary));
            shouldShuffle[card.getOwner().getIndex()] = true;
        }
        for (final MagicPlayer player : game.getAPNAP()) {
            if (shouldShuffle[player.getIndex()]) {
                game.doAction(new ShuffleLibraryAction(player));
            }
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

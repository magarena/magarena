package magic.model.action;

import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class ShuffleCardsIntoLibraryAction extends MagicAction {

    private List<MagicCard> cards;
    private MagicLocationType from;

    public ShuffleCardsIntoLibraryAction(final List<MagicCard> aCards, final MagicLocationType aFrom) {
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

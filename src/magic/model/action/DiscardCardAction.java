package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class DiscardCardAction extends MagicAction {

    private final MagicCard card;
    private final MagicLocationType toLocation;

    public DiscardCardAction(final MagicPlayer aPlayer,final MagicCard aCard) {
        this(aPlayer, aCard, MagicLocationType.Graveyard);
    }

    public DiscardCardAction(final MagicPlayer aPlayer,final MagicCard aCard, final MagicLocationType aToLocation) {
        card = aCard;
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersHand));
        game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,toLocation));
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

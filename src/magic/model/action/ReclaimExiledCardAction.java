package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class ReclaimExiledCardAction extends MagicAction {
    private final MagicPermanent source;
    private final MagicCard card;

    public ReclaimExiledCardAction(final MagicPermanent source,final MagicCard card){
        this.source = source;
        this.card = card;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new ShiftCardAction(card, MagicLocationType.Exile, MagicLocationType.OwnersHand));
        source.removeExiledCard(card);
    }

    @Override
    public void undoAction(final MagicGame game) {
        source.addExiledCard(card);
    }
}

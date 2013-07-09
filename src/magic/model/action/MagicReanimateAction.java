package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;

public class MagicReanimateAction extends MagicAction {

    private final MagicPlayer controller;
    private final MagicCard card;
    private final int modification;
    private MagicPermanent permanent = MagicPermanent.NONE;

    public MagicReanimateAction(final MagicCard aCard, final MagicPlayer aController, final int aModification) {
        card = aCard;
        controller = aController;
        modification = aModification;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (card.getOwner().getGraveyard().contains(card)) {
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            final MagicPlayCardAction action = new MagicPlayCardAction(card,controller,modification);
            game.doAction(action);
            permanent = action.getPermanent();
        }
    }
    
    public MagicPermanent getPermanent() {
        return permanent;
    }

    @Override
    public void undoAction(final MagicGame game) {

    }
}

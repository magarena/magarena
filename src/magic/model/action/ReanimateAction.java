package magic.model.action;

import java.util.Arrays;
import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class ReanimateAction extends MagicAction {

    private final MagicPlayer controller;
    private final MagicCard card;
    private final List<? extends MagicPermanentAction> modifications;

    public ReanimateAction(final MagicCard aCard, final MagicPlayer aController, final List<? extends MagicPermanentAction> aModifications) {
        card = aCard;
        controller = aController;
        modifications = aModifications;
    }
    
    public ReanimateAction(final MagicCard aCard, final MagicPlayer aController, final MagicPermanentAction... aModifications) {
        this(aCard, aController, Arrays.asList(aModifications));
    }
    
    @Override
    public void doAction(final MagicGame game) {
        if (card.isInGraveyard() && card.isPermanentCard()) {
            game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new PlayCardAction(card,controller,modifications));
        }
    }
    
    @Override
    public void undoAction(final MagicGame game) {}
}

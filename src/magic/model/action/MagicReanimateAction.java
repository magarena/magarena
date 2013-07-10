package magic.model.action;

import java.util.List;
import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;

public class MagicReanimateAction extends MagicAction {

    private final MagicPlayer controller;
    private final MagicCard card;
    private final List<MagicPlayMod> modifications;

    public MagicReanimateAction(final MagicCard aCard, final MagicPlayer aController, final List<MagicPlayMod> aModifications) {
        card = aCard;
        controller = aController;
        modifications = aModifications;
    }
    
    public MagicReanimateAction(final MagicCard aCard, final MagicPlayer aController, final MagicPlayMod... aModifications) {
        this(aCard, aController, Arrays.asList(aModifications));
    }
    
    @Override
    public void doAction(final MagicGame game) {
        if (card.isInGraveyard()) {
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MagicPlayCardAction(card,controller,modifications));
        }
    }
    
    @Override
    public void undoAction(final MagicGame game) {}
}

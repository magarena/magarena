package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.stack.MagicCardOnStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MagicPlayCardAction extends MagicPutIntoPlayAction {

    private final MagicCard card;
    private final MagicPlayer controller;

    public MagicPlayCardAction(final MagicCard aCard, final MagicPlayer aController,final List<? extends MagicPermanentAction> aModifications) {
        card = aCard;
        controller = aController;
        setModifications(aModifications);
    }
    
    public MagicPlayCardAction(final MagicCard aCard, final MagicPlayer aController,final MagicPermanentAction... aModifications) {
        this(aCard, aController, Arrays.asList(aModifications));
    }
    
    public MagicPlayCardAction(final MagicCard card, final MagicPlayer player) {
        this(card, player, Collections.<MagicPermanentAction>emptyList());
    }
    
    public MagicPlayCardAction(final MagicCard card, final List<? extends MagicPermanentAction> modifications) {
        this(card, card.getController(), modifications);
    }
    
    public MagicPlayCardAction(final MagicCard card) {
        this(card, card.getController(), Collections.<MagicPermanentAction>emptyList());
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        return game.createPermanent(card,controller);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (card.getCardDefinition().isAura()) {
            final MagicCardOnStack cardOnStack = new MagicCardOnStack(card, controller, MagicPayedCost.NOT_SPELL);
            game.addEvent(cardOnStack.getEvent());
        } else {
            super.doAction(game);
        }
    }
}

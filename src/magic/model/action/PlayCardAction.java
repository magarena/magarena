package magic.model.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

public class PlayCardAction extends MagicAction {

    protected final MagicCard card;
    protected final MagicPlayer controller;
    protected final List<? extends MagicPermanentAction> modifications;

    public PlayCardAction(final MagicCard aCard, final MagicPlayer aController,final List<? extends MagicPermanentAction> aModifications) {
        card = aCard;
        controller = aController;
        modifications = aModifications;
    }

    public PlayCardAction(final MagicCard aCard, final MagicPlayer aController,final MagicPermanentAction... aModifications) {
        this(aCard, aController, Arrays.asList(aModifications));
    }

    public PlayCardAction(final MagicCard card, final MagicPlayer player) {
        this(card, player, Collections.<MagicPermanentAction>emptyList());
    }

    public PlayCardAction(final MagicCard card, final List<? extends MagicPermanentAction> modifications) {
        this(card, card.getController(), modifications);
    }

    public PlayCardAction(final MagicCard card) {
        this(card, card.getController(), Collections.<MagicPermanentAction>emptyList());
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardOnStack cardOnStack = new MagicCardOnStack(card, controller, MagicPayedCost.NOT_SPELL, modifications);
        game.addEvent(cardOnStack.getEvent());
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

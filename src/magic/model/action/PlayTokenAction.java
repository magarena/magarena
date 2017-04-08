package magic.model.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

public class PlayTokenAction extends MagicAction {

    private final MagicCard card;
    private final List<? extends MagicPermanentAction> modifications;

    public PlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition, final List<? extends MagicPermanentAction> aModifications) {
        card = MagicCard.createTokenCard(cardDefinition,player);
        modifications = aModifications;
    }

    public PlayTokenAction(final MagicCard aCard) {
        card = aCard;
        modifications = Collections.<MagicPermanentAction>emptyList();
    }

    public PlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition) {
        this(player, cardDefinition, Collections.<MagicPermanentAction>emptyList());
    }

    public PlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition,final MagicPermanentAction... aModifications) {
        this(player, cardDefinition, Arrays.asList(aModifications));
    }

    public PlayTokenAction(final MagicPlayer player,final MagicObject obj) {
        this(player, obj.getCardDefinition(), Collections.<MagicPermanentAction>emptyList());
    }

    public PlayTokenAction(final MagicPlayer player,final MagicObject obj, final List<? extends MagicPermanentAction> aModifications) {
        this(player, obj.getCardDefinition(), aModifications);
    }

    public PlayTokenAction(final MagicPlayer player,final MagicObject obj, final MagicPermanentAction... aModifications) {
        this(player, obj.getCardDefinition(), Arrays.asList(aModifications));
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardOnStack cardOnStack = new MagicCardOnStack(card, card.getController(), MagicPayedCost.NOT_SPELL, modifications);
        game.addEvent(cardOnStack.getEvent());
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

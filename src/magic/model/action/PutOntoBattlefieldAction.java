package magic.model.action;

import java.util.Arrays;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class PutOntoBattlefieldAction extends MagicAction {

    private final MagicPlayer controller;
    private final MagicCard card;
    private final MagicLocationType from;
    private final List<? extends MagicPermanentAction> modifications;

    public PutOntoBattlefieldAction(final MagicLocationType aFrom, final MagicCard aCard, final MagicPlayer aController, final List<? extends MagicPermanentAction> aModifications) {
        card = aCard;
        controller = aController;
        from = aFrom;
        modifications = aModifications;
    }

    public PutOntoBattlefieldAction(final MagicLocationType aFrom, final MagicCard aCard, final MagicPlayer aController, final MagicPermanentAction... aModifications) {
        this(aFrom, aCard, aController, Arrays.asList(aModifications));
    }

    @Override
    public void doAction(final MagicGame game) {
        if (card.isIn(from) && card.isPermanentCard()) {
            game.doAction(new RemoveCardAction(card,from));
            game.doAction(new PlayCardAction(card,controller,modifications));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

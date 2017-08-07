package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPutCardOnStackEvent;

public class CastCardAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCard card;
    private final boolean withoutManaCost;
    private final MagicLocationType from;
    private final MagicLocationType to;
    private final MagicPermanentAction mod;

    public CastCardAction(final MagicPlayer aPlayer, final MagicCard aCard, final MagicLocationType aFrom, final MagicLocationType aTo) {
        this(aPlayer, aCard, false, aFrom, aTo, MagicPlayMod.NONE);
    }

    public static CastCardAction WithoutManaCost(final MagicPlayer aPlayer, final MagicCard aCard, final MagicLocationType aFrom, final MagicLocationType aTo) {
        return new CastCardAction(aPlayer, aCard, true, aFrom, aTo, MagicPlayMod.NONE);
    }

    public static CastCardAction WithoutManaCost(final MagicPlayer aPlayer, final MagicCard aCard, final MagicLocationType aFrom, final MagicLocationType aTo, final MagicPermanentAction mod) {
        return new CastCardAction(aPlayer, aCard, true, aFrom, aTo, mod);
    }

    private CastCardAction(final MagicPlayer aPlayer, final MagicCard aCard, final boolean aWithoutManaCost, final MagicLocationType aFrom, final MagicLocationType aTo, final MagicPermanentAction aMod) {
        player = aPlayer;
        card = aCard;
        withoutManaCost = aWithoutManaCost;
        from = aFrom;
        to = aTo;
        mod = aMod;
    }

    @Override
    public void doAction(final MagicGame game) {
        for (final MagicEvent event : withoutManaCost ? card.getAdditionalCostEvent() : card.getCostEvent()) {
            if (event.isSatisfied() == false) {
                game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay casting costs.");
                return;
            }
        }
        for (final MagicEvent event : withoutManaCost ? card.getAdditionalCostEvent() : card.getCostEvent()) {
            game.addCostEvent(event);
        }
        game.addEvent(new MagicPutCardOnStackEvent(card, player, from, to, mod));
    }

    @Override
    public void undoAction(final MagicGame game) {}

    @Override
    public String toString() {
        return getClass().getSimpleName()+" (" +player + ',' + card +')';
    }
}

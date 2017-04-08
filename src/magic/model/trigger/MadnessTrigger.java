package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.CastCardAction;
import magic.model.action.MoveCardAction;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;

public class MadnessTrigger extends ThisPutIntoGraveyardTrigger {

    private final MagicManaCost cost;

    public MadnessTrigger(final MagicManaCost aCost) {
        super(MagicTrigger.REPLACEMENT);
        cost = aCost;
    }

    @Override
    public boolean usesStack() {
        return true;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
        return super.accept(permanent, act) && act.from(MagicLocationType.OwnersHand);
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
        // activate discard triggers
        game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyard, act);

        // change discard location to Exile
        act.setToLocation(MagicLocationType.Exile);

        final MagicManaCost modCost = game.modCost(act.card, cost);

        return new MagicEvent(
            act.card,
            new MagicMayChoice(
                "Cast " + act.card + " by paying " + modCost + "? (Madness)",
                new MagicPayManaCostChoice(modCost)
            ),
            this,
            "PN may$ cast SN for its madness cost. If PN doesn't, put SN into into his or her graveyard."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        if (card.isInExile()) {
            if (event.isYes()) {
                game.doAction(CastCardAction.WithoutManaCost(
                    event.getPlayer(),
                    card,
                    MagicLocationType.Exile,
                    MagicLocationType.Graveyard
                ));
            } else {
                game.doAction(new ShiftCardAction(card,MagicLocationType.Exile,MagicLocationType.Graveyard));
            }
        }
    }
}

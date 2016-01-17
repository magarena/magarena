package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.action.ShiftCardAction;
import magic.model.action.CastCardAction;
import magic.model.action.EnqueueTriggerAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MadnessTrigger extends ThisPutIntoGraveyardTrigger {

    private final MagicManaCost cost;

    public MadnessTrigger(final MagicManaCost aCost) {
        super(MagicTrigger.REPLACEMENT);
        cost = aCost;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
        return super.accept(permanent, act) && act.from(MagicLocationType.OwnersHand);
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
        //Activate discard triggers
        game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyard, act);

        //Change discard location so that MoveCardAction does nothing
        act.setToLocation(MagicLocationType.Battlefield);

        return new MagicEvent(
            act.card,
            new MagicMayChoice(
                "Exile " + act.card + " instead of putting into your graveyard? (Madness)"
            ),
            this,
            "PN may$ exile SN instead of putting it into his or her graveyard."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        final MagicManaCost modCost = game.modCost(card, cost);
        if (event.isYes()) {
            game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.Exile));
            game.doAction(new EnqueueTriggerAction(new MagicEvent(
                card,
                new MagicMayChoice(
                    "Cast " + card + " by paying " + modCost + "? (Madness)",
                    new MagicPayManaCostChoice(modCost)
                ),
                EVENT_ACTION,
                "PN may$ cast SN for its madness cost. If PN doesn't, put SN into into his or her graveyard."
            )));
        } else {
            // cannot be from OwnersHand as it will trigger itself again, so we use from Play instead
            game.doAction(new MoveCardAction(card,MagicLocationType.Battlefield,MagicLocationType.Graveyard));
        }
    }

    private MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
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
                game.doAction(new ShiftCardAction(event.getCard(),MagicLocationType.Exile,MagicLocationType.Graveyard));
            }
        }
    };
}

package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.action.EnqueueTriggerAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.stack.MagicCardOnStack;

public class MagicMadnessTrigger extends MagicWhenPutIntoGraveyardTrigger {

    private final MagicManaCost cost;
    
    public MagicMadnessTrigger(final MagicManaCost aCost) {
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
        act.setToLocation(MagicLocationType.Play);
        
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
        if (event.isYes()) {
            game.doAction(new MoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.Exile));
            game.doAction(new EnqueueTriggerAction(new MagicEvent(
                card,
                new MagicMayChoice(
                    "Cast " + card + " by paying " + cost + "? (Madness)",
                    new MagicPayManaCostChoice(cost)
                ),
                EVENT_ACTION,
                "PN may$ cast SN for its madness cost. If PN doesn't, put SN into into his or her graveyard."
            )));
        } else {
            // cannot be from OwnersHand as it will trigger itself again, so we use from Play instead
            game.doAction(new MoveCardAction(card,MagicLocationType.Play,MagicLocationType.Graveyard));
        }
    }
    
    private MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            if (card.isInExile()) {
                game.doAction(new RemoveCardAction(card, MagicLocationType.Exile));
                if (event.isYes()) {
                    final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(),MagicPayedCost.NO_COST);
                    cardOnStack.setFromLocation(MagicLocationType.Exile);
                    game.doAction(new PutItemOnStackAction(cardOnStack));
                } else {
                    game.doAction(new MoveCardAction(event.getCard(),MagicLocationType.Exile,MagicLocationType.Graveyard));
                }
            }
        }
    };
}

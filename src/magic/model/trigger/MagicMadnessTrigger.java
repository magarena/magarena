package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicMadnessTrigger extends MagicWhenPutIntoGraveyardTrigger {

    private final MagicManaCost cost;
    
    public MagicMadnessTrigger(final MagicManaCost aCost) {
        super(MagicTrigger.REPLACEMENT);
        cost = aCost;
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
        if (act.from(MagicLocationType.OwnersHand) && act.to(MagicLocationType.Graveyard)) {
            game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyard, act); //Activate discard triggers
            act.setToLocation(MagicLocationType.Exile); //Change discard location
            final MagicCard card = act.card;
            return new MagicEvent(
                card,
                new MagicMayChoice(
                    "Cast for its madness cost?",
                    new MagicPayManaCostChoice(cost)
                ),
                this,
                "PN may$ cast SN for its madness cost instead of putting it into his or her graveyard."
            );
        } else {
            return MagicEvent.NONE;
        }
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRemoveCardAction(event.getCard(),MagicLocationType.Exile));
        if (event.isYes()) {
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(event.getCard(),event.getPlayer(),MagicPayedCost.NO_COST);
            game.doAction(new MagicPutItemOnStackAction(cardOnStack));
        } else {
            game.doAction(new MagicMoveCardAction(event.getCard(),MagicLocationType.Exile,MagicLocationType.Graveyard));
        }
    }
}

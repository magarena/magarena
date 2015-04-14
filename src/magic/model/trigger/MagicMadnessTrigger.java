package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;
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
    public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
        return super.accept(permanent, act) && act.from(MagicLocationType.OwnersHand);
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
        game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyard, act); //Activate discard triggers
        act.setToLocation(MagicLocationType.Exile); //Change discard location
        return new MagicEvent(
            act.card,
            new MagicMayChoice(
                "Cast for its madness cost?",
                new MagicPayManaCostChoice(cost)
            ),
            this,
            "PN may$ cast SN for its madness cost instead of putting it into his or her graveyard."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicRemoveCardAction(event.getCard(),MagicLocationType.Exile));
        if (event.isYes()) {
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(event.getCard(),event.getPlayer(),MagicPayedCost.NO_COST);
            cardOnStack.setFromLocation(MagicLocationType.Exile);
            game.doAction(new MagicPutItemOnStackAction(cardOnStack));
        } else {
            game.doAction(new MoveCardAction(event.getCard(),MagicLocationType.Exile,MagicLocationType.Graveyard));
        }
    }
}

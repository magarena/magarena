package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicPayedCost;
import magic.model.MagicManaCost;
import magic.model.MagicLocationType;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicAddEventAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.event.MagicCardActivation;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenDrawnTrigger;

public class MagicMiracleTrigger extends MagicWhenDrawnTrigger {

    private final MagicManaCost cost;

    public MagicMiracleTrigger(final MagicManaCost cost) {
        this.cost = cost;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
        return data.getOwner().getDrawnCards() == 1 ?
            new MagicEvent(
                data,
                data.getOwner(),
                new MagicMayChoice(
                        "You may pay " + cost + ".",
                        new MagicPayManaCostChoice(MagicManaCost.ONE_BLUE)),
                MagicEvent.NO_DATA,
                this,
                "You may$ cast this card for it's miracle cost") :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        final MagicCard card = (MagicCard)event.getSource();
        final MagicCardActivation act = card.getCardDefinition().getCardActivation();
        game.doAction(new MagicAddEventAction(act.getEvent(card)));
    }
}

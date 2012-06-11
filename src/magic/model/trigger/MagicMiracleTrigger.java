package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.action.MagicAddEventAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicCardActivation;
import magic.model.trigger.MagicWhenDrawnTrigger;

public class MagicMiracleTrigger extends MagicWhenDrawnTrigger {

    private final MagicManaCost cost;

    public MagicMiracleTrigger(final MagicManaCost cost) {
        this.cost = cost;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent none,final MagicCard card) {
        return card.getOwner().getDrawnCards() == 1 ?
            new MagicEvent(
                card,
                card.getOwner(),
                new MagicMayChoice(
                        "You may pay " + cost + ".",
                        new MagicPayManaCostChoice(cost)),
                MagicEvent.NO_DATA,
                this,
                "You may$ cast this card for its miracle cost") :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            final MagicCard card = (MagicCard)event.getSource();
            final MagicCardActivation act = card.getCardDefinition().getCardActivation();
            game.doAction(new MagicAddEventAction(act.getEvent(card)));
        }
    }
}

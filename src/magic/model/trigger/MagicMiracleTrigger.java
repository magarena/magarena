package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;

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
                    new MagicPayManaCostChoice(cost)
                ),
                this,
                "You may$ cast this card for its miracle cost"
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            final MagicCard card = event.getCard();
            final MagicActivation act = card.getCardDefinition().getCastActivation();
            game.addEvent(act.getEvent(card));
        }
    }
}

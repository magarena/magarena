package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Unhallowed_Pact {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent data) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            return (enchanted == data) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{enchanted.getCard()},
                    this,
                    "Return " + enchanted + " to the battlefield under your control."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicCard card = (MagicCard)data[0];
            if (card.getOwner().getGraveyard().contains(card)) {
                game.doAction(new MagicReanimateAction(
                        event.getPlayer(),
                        card,
                        MagicPlayCardAction.NONE));
            }
        }
    };
}

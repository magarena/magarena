package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Loyal_Sentry {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent defender) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == defender && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    new Object[]{blocked},
                    this,
                    "Destroy both " + blocked + "and SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
            game.doAction(new MagicDestroyAction(event.getPermanent()));
        }
    };
}

package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Loyal_Sentry {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == data && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocked,permanent},
                    this,
                    "Destroy " + blocked + ". Destroy " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
            game.doAction(new MagicDestroyAction((MagicPermanent)data[1]));
        }
    };
}

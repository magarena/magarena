package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLifeChangeTriggerData;
import magic.model.trigger.MagicWhenLifeIsLostTrigger;

public class Exquisite_Blood {
    public static final MagicWhenLifeIsLostTrigger T = new MagicWhenLifeIsLostTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isOpponent(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    new Object[]{amount},
                    this,
                    "PN gains " + amount + " life."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    (Integer)data[0]));
        }
    };
}

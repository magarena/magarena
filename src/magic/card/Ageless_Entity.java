package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;
import magic.model.trigger.MagicLifeChangeTriggerData;

public class Ageless_Entity {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isController(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    new Object[]{amount},
                    this,
                    amount > 1 ?
                        "Put " + amount + " +1/+1 counters on SN." :
                        "Put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    (Integer)data[0],
                    true));
        }
    };
}

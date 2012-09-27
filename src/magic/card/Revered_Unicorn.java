package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Revered_Unicorn {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            return (permanent == data) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains life equal to the number of age counters on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    amount));
        }
    };
}

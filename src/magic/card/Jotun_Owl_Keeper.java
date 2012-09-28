package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Jotun_Owl_Keeper {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            return (permanent == data && amount > 0) ?
                new MagicEvent(
                    permanent,
                    new Object[]{amount},
                    this,
                    amount > 1 ?
                        "PN puts " + amount + " 1/1 white Bird creature tokens with flying onto the battlefield." :
                        "PN puts a 1/1 white Bird creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            int amount = (Integer)data[0];
            for (;amount>0;amount--) {
                game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Bird1")));
            }
        }
    };
}

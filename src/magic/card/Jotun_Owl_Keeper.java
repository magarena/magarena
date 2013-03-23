package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayTokensAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Jotun_Owl_Keeper {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            return (permanent == left && amount > 0) ?
                new MagicEvent(
                    permanent,
                    amount,
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
                final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Bird1"),
                event.getRefInt()
            ));
        }
    };
}

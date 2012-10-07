package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Tel_Jilad_Wolf {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            int amount = 0;
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (blocker.isArtifact() && blocker.isCreature()) {
                    amount += 3;
                }
            }
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN gets +" + amount + "/+" +  amount + " until end of turn."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                event.getPermanent(),
                event.getRefInt(),
                event.getRefInt()
            ));
        }
    };
}

package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Berserk_Murlodont {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final int amount = blocker.getBlockingCreatures().size();
            return blocker.hasSubType(MagicSubType.Beast) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocker,amount},
                    this,
                    blocker + " gets +" + amount + "/+" +  amount + " until end of turn."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    (Integer)data[1],
                    (Integer)data[1]));
        }
    };
}

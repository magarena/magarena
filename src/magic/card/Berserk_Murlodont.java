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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return blocked.hasSubType(MagicSubType.Beast) ?
                new MagicEvent(
                    permanent,
                    new Object[]{blocked},
                    this,
                    blocked + " gets +1/+1 until end of turn for each creature blocking it."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent blocked = (MagicPermanent)data[0];
            final int amount = blocked.getBlockingCreatures().size();
            game.doAction(new MagicChangeTurnPTAction(
                blocked,
                amount,
                amount
            ));
        }
    };
}

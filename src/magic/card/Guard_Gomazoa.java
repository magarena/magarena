package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Guard_Gomazoa {
    public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget()==permanent&&damage.isCombat()) {
                // Replacement effect. Generates no event or action.
                damage.setAmount(0);    
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            
        }
    };
}

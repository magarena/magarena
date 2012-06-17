package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Szadek__Lord_of_Secrets {
    public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(6) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getAmount();
            if (amount>0 && 
                damage.isCombat() && 
                permanent==damage.getSource() && 
                damage.getTarget().isPlayer()) {
                // Replacement effect.
                damage.setAmount(0);
                game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
                game.doAction(new MagicMillLibraryAction((MagicPlayer)damage.getTarget(),amount));
            }            
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
        
        }
    };
}

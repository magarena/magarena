package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Retaliator_Griffin {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount=damage.getDealtAmount();
            final MagicPlayer player=permanent.getController();
            return (amount > 0 && 
                    damage.getTarget() == player &&
                    damage.getSource().getController() != player) ?
                new MagicEvent(
                    permanent,
                    new Object[]{amount},
                    this,
                    "Put "+amount+" +1/+1 counters on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        (Integer)data[0],true));
        }        
    };
}

package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Lightning_Reaver {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        this,
                        "Put a charge counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
        }
    };

    public static final MagicAtEndOfTurnTrigger T2 = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final MagicPlayer player=permanent.getController();
            final int counters=permanent.getCounters(MagicCounterType.Charge);
            return (player==eotPlayer && counters>0) ?
                new MagicEvent(
                        permanent,
                        player.getOpponent(),
                        this,
                        permanent + " deals damage equal to the number of " + 
                        "charge counters on it to " + player.getOpponent() + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            final int counters=permanent.getCounters(MagicCounterType.Charge);
            final MagicDamage damage=new MagicDamage(permanent,event.getPlayer(),counters,false);
            game.doAction(new MagicDealDamageAction(damage));
        }        
    };
}

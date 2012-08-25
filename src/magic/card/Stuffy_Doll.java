package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Stuffy_Doll {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    source + " deals 1 damage to itself.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[0];
            final MagicDamage damage=new MagicDamage(permanent,permanent,1,false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
    
    public static final MagicWhenDamageIsDealtTrigger T1 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {  
            if (damage.getTarget() == permanent) {
                final MagicPlayer player = permanent.getController();
                final int amount = damage.getDealtAmount();
                final MagicTarget target = permanent.getChosenTarget();
                return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,target,amount},
                        this,
                        permanent + " deals " + amount +
                        " damage to " + target + ".");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    (MagicSource)data[0],
                    (MagicTarget)data[1],
                    (Integer)data[2],
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            permanent.setChosenTarget(player.getOpponent());
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

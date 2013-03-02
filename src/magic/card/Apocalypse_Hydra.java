package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Apocalypse_Hydra {
    public static final MagicWhenComesIntoPlayTrigger ETB = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            final int count = permanent.getKicker() >= 5 ? 
                    2 * permanent.getKicker() :
                    permanent.getKicker();
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.PlusOne,
                count,
                true
            ));
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[] {
                    MagicConditionFactory.ManaCost("{1}{R}"),
                    MagicCondition.PLUS_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}{R}")),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
                    this,
                    "SN deals 1 damage to target creature or player$.");
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),target,1);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}

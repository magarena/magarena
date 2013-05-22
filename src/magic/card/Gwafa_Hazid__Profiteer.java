package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Gwafa_Hazid__Profiteer {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotAttackOrBlock);
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            return target.getCounters(MagicCounterType.Bribery) > 0;
        }
    };
        
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicCondition.CAN_TAP_CONDITION,
                    MagicConditionFactory.ManaCost("{W}{U}")
            },
            new MagicActivationHints(MagicTiming.Tapping),
            "Disable"
            ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,"{W}{U}")
            };
        }

        @Override
                public MagicEvent getPermanentEvent(
                        final MagicPermanent source,
                        final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    new MagicNoCombatTargetPicker(true,true,true),
                    this,
                    "PN puts a bribery counter on target creature$. " +
                    "Its controller draws a card.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeCountersAction(
                            creature,
                            MagicCounterType.Bribery,
                            1,
                            true));
                    game.doAction(new MagicDrawAction(creature.getController(),1));
                }
            });
        }
    };
}

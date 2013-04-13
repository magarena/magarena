package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Grimgrin__Corpse_Born {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.TWO_CREATURES_CONDITION
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"a creature other than " + source + " to sacrifice");
            return new MagicEvent[]{new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    targetChoice)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Untap SN and put a +1/+1 counter on it.");
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    1,
                    true));
        }
    };
    
    public static final MagicWhenAttacksTrigger T1 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                        new MagicDestroyTargetPicker(false),
                        this,
                        "Destroy target creature$ your opponent controls, " +
                        "then put a +1/+1 counter on SN."):
                MagicEvent.NONE;           
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    1,
                    true));
        }
    };
}

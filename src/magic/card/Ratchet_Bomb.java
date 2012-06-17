package magic.card;

import java.util.Collection;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDestroyAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Ratchet_Bomb {
    
    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Charge") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return new MagicEvent[]{new MagicTapEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Put a charge counter on " + source + ".");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        1,
                        true));
        }        
    };

    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicSacrificeEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Destroy each nonland permanent with converted mana cost equal to the number of charge counters on " + 
                    source + ".");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent source=(MagicPermanent)data[0];
            final int amount=source.getCounters(MagicCounterType.Charge);
            final Collection<MagicTarget> targets=
                game.filterTargets(source.getController(), 
                        new MagicTargetFilter.MagicCMCTargetFilter(
                            MagicTargetFilter.TARGET_NONLAND_PERMANENT, 
                            MagicTargetFilter.MagicCMCTargetFilter.EQUAL, 
                            amount));
            game.doAction(new MagicDestroyAction(targets));
        }
    };
}

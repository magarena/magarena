package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Kargan_Dragonlord {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 8) {
                pt.set(8,8);
            } else if (charges >= 4) {
                pt.set(4,4);
            }
        }        
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 8) {
                return flags |
                    MagicAbility.Flying.getMask() |
                    MagicAbility.Trample.getMask();
            } else if (charges >= 4) {
                return flags | MagicAbility.Flying.getMask();
            } else {
                return flags;
            }
        }
    };

    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.EIGHT_CHARGE_COUNTERS_CONDITION,
                MagicManaCost.RED.getCondition()
            },
            new MagicActivationHints(MagicTiming.Pump),
            "+1/+0") {

        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                    new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.RED)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    source + " gets +1/+0 until end of turn.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,0));
        }
    };
}

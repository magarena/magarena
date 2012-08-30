package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Golden_Urn {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                            player + " may put a charge counter on " + permanent + ".",
                            MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES),
                        this,
                        player + " may$ put a charge counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
            }    
        }
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Gain life") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicSacrificeEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            return new MagicEvent(
                source,
                player,
                this,
                player + " gains life equal to the number of charge counters on " + source + ".");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount));
        }
    };
}

package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicSacrificeAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.event.MagicWeakenCreatureActivation;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Serrated_Arrows {

    public static final MagicPermanentActivation A1 = new MagicWeakenCreatureActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
        }
    };

    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();    
            return (player == data && permanent.getCounters(MagicCounterType.Charge) == 0) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    "Sacrifice " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
        }
    };
}

package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Twilight_Drover {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent left) {
            final MagicPlayer player = permanent.getController();
            return (left.isCreature() && left.isToken()) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        player + " puts a +1/+1 counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    1,
                    true));            
        }    
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[] {
                    MagicManaCost.TWO_WHITE.getCondition(),
                    MagicCondition.PLUS_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_WHITE),
                new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.PlusOne,1)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            return new MagicEvent(
                    source,
                    player,
                    this,
                    player + " puts two 1/1 white Spirit creature " +
                    "tokens with flying onto the battlefield.");
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Spirit2")));
            game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Spirit2")));
        }
    };
}

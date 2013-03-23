package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokensAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
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
            return (left.isCreature() && left.isToken()) ?
                new MagicEvent(
                        permanent,
                        this,
                        "PN puts a +1/+1 counter on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));            
        }    
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[] {
                    MagicConditionFactory.ManaCost("{2}{W}"),
                    MagicCondition.PLUS_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{2}{W}")),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "PN puts two 1/1 white Spirit creature " +
                    "tokens with flying onto the battlefield.");
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Spirit2"),
                2
            ));
        }
    };
}

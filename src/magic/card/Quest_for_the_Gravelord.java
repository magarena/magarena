package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Quest_for_the_Gravelord {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ChargeCountersAtLeast(3)},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,3),
                new MagicSacrificeEvent(source)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Put a 5/5 black Zombie Giant creature token onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Zombie Giant")));
        }
    };
    
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ put a quest counter on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    1,
                    true
                ));
            }
        }
    };
}

package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Angelheart_Vial {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicConditionFactory.ChargeCountersAtLeast(4),
                MagicConditionFactory.ManaCost("{2}")
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{2}")),
                    new MagicRemoveCounterEvent(
                        source,
                        MagicCounterType.Charge,
                        4)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "PN gains 2 life and draws a card");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };

    
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = damage.getTarget();
            final int amount = damage.getDealtAmount();
            return (target.isPlayer() && target == player) ?
                new MagicEvent(
                        permanent,
                        player,
                        amount,
                        this,
                        "Put " + amount + " charge counters on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    event.getRefInt(),
                    false));
        }
    };
}

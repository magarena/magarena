package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Golden_Urn {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Gain life") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent = source;
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
                this,
                "PN gains life equal to the number of charge counters on SN.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),amount));
        }
    };
}

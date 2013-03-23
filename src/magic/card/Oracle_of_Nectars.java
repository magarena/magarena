package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Oracle_of_Nectars {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{X}")},
            new MagicActivationHints(MagicTiming.Draw),
            "Life+X") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{X}"))};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                amount,
                this,
                "PN gains " + amount + " life."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),event.getRefInt()));
        }
    };
}

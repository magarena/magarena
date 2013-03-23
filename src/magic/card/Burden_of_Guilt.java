package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Burden_of_Guilt {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.ENCHANTED_IS_UNTAPPED_CONDITION,
                MagicConditionFactory.ManaCost("{1}")
            },
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(
                source,
                source.getController(),
                MagicManaCost.create("{1}")
            )};
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedCreature(),
                this,
                "Tap " + source.getEnchantedCreature() + "."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicTapAction(event.getRefPermanent(),true));
        }
    };
}

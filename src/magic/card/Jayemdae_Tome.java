package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Jayemdae_Tome {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[] {
                MagicCondition.CAN_TAP_CONDITION, 
                MagicConditionFactory.ManaCost("{4}") 
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[] {
                new MagicPayManaCostTapEvent(
                    source,
                    source.getController(),
                    MagicManaCost.create("{4}")
                )
            };
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "PN draws a card.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}

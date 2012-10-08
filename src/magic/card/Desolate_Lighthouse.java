package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Desolate_Lighthouse {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[] {
                    MagicCondition.CAN_TAP_CONDITION,
                    MagicManaCost.ONE_BLUE_RED.getCondition()
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[] {
                new MagicPayManaCostTapEvent(
                    source,
                    source.getController(),
                    MagicManaCost.ONE_BLUE_RED
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
                "PN draws a card, then discards a card."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
            game.addEvent(new MagicDiscardEvent(event.getPermanent(),player,1,false));
        }
    };
}

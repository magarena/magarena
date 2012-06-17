package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Grim_Backwoods {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[]{
                MagicManaCost.THREE_BLACK_GREEN.getCondition(), //add ONE for the card itself
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.ONE_CREATURE_CONDITION
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                    new MagicTapEvent((MagicPermanent)source),
                    new MagicPayManaCostEvent(
                            source,
                            source.getController(),
                            MagicManaCost.TWO_BLACK_GREEN),
                    new MagicSacrificePermanentEvent(
                            source,
                            source.getController(),
                            MagicTargetChoice.SACRIFICE_CREATURE)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source.getController()},
                    this,
                    source.getController() + " draws a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
        }
    };
}

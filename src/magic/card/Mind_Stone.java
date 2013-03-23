package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;

public class Mind_Stone {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{2}")},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent=source;
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}")),
                new MagicSacrificeEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player=source.getController();
            return new MagicEvent(
                    source,
                    player,
                    this,
                    "Draw a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}

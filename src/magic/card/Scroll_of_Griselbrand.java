package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;

public class Scroll_of_Griselbrand {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicConditionFactory.ManaCost("{1}")
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent = source;
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}")),
                new MagicSacrificeEvent(permanent)
            };
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Opponent discards a card. If PN controls a Demon, opponent loses 3 life."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player.getOpponent(),
                    1,
                    false));
            if (player.controlsPermanentWithSubType(MagicSubType.Demon)) {
                game.doAction(new MagicChangeLifeAction(player.getOpponent(),-3));
            }
        }
    };
}

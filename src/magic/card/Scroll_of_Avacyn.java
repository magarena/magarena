package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;

public class Scroll_of_Avacyn {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicManaCost.ONE.getCondition()
            },
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE),
                new MagicSacrificeEvent(permanent)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            return new MagicEvent(
                source,
                player,
                MagicEvent.NO_DATA,
                this,
                player + " draws a card. If he or she controls an Angel, " +
                player + " gains 5 life.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,1));
            if (player.controlsPermanentWithSubType(MagicSubType.Angel)) {
                game.doAction(new MagicChangeLifeAction(player,5));
            }
        }
    };
}

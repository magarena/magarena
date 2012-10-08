package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;

public class Alchemist_s_Apprentice {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicActivation.NO_COND,
            new MagicActivationHints(MagicTiming.Draw),
            "Draw"
            ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicSacrificeEvent(source)};
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
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}

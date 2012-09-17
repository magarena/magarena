package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Arc_Runner {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final MagicPlayer player = permanent.getController();
            return (player == eotPlayer) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Sacrifice SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
}

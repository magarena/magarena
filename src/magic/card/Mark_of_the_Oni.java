package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Mark_of_the_Oni {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) &&
                    eotPlayer.getNrOfPermanentsWithSubType(MagicSubType.Demon) == 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
}

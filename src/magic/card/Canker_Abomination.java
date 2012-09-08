package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Canker_Abomination {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            final int amount = player.getOpponent().getNrOfPermanentsWithType(MagicType.Creature);
            game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.MinusOne,
                        amount,
                        true));
            return MagicEvent.NONE;
        }
    };
}

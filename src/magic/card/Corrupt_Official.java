package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Corrupt_Official {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return (permanent == blocked) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "PN discards a card at random."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.addEvent(new MagicDiscardEvent(
                event.getSource(),
                event.getPlayer(),
                1,
                true
            ));
        }
    };
}

package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Thresher_Beast {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return (permanent == blocked) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "PN sacrifices a land."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPlayer opponent = event.getPlayer();
            if (opponent.controlsPermanentWithType(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    opponent,
                    MagicTargetChoice.SACRIFICE_LAND
                ));
            }
        }
    };
}

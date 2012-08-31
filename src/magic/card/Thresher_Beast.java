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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent == data ) ?
                    new MagicEvent(
                            permanent,
                            player,
                            this,
                            player.getOpponent() + " sacrifices a land."):
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            if (opponent.controlsPermanentWithType(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    opponent,
                    MagicTargetChoice.SACRIFICE_LAND));
            }
        }
    };
}

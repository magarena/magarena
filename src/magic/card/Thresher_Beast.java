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
                            new Object[]{permanent,player.getOpponent()},
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
            final MagicPlayer opponent = (MagicPlayer)data[1];
            if (opponent.controlsPermanentWithType(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    (MagicPermanent)data[0],
                    opponent,
                    MagicTargetChoice.SACRIFICE_LAND));
            }
        }
    };
}

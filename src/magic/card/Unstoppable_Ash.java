package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Unstoppable_Ash {
    public static final MagicWhenBecomesBlockedTrigger T3 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (player == data.getController() ) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new Object[]{data},
                            this,
                            data + " gets +0/+5 until end of turn."):
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    0,
                    5));
        }
    };
}

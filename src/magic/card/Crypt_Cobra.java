package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangePoisonAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Crypt_Cobra {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPlayer player = permanent.getController();
                final MagicPlayer opponent = player.getOpponent();
                return new MagicEvent(
                        permanent,
                        player,
                        this,
                        opponent + " gets a poison counter.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangePoisonAction(event.getPlayer().getOpponent(),1));
        }
    };
}

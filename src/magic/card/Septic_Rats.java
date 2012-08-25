package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Septic_Rats {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player=permanent.getController();
            return (permanent==data && player.getOpponent().getPoison()>0) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new Object[]{permanent},
                            this,
                            permanent + " gets +1/+1 until end of turn.") :
                    MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,1));
        }
    };
}

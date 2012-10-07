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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent==attacker && 
                    permanent.getOpponent().getPoison()>0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,1));
        }
    };
}

package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Lavaborn_Muse {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.getController()!=player&&player.getHandSize()<3) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        "SN deals 3 damage to PN."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),3,false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

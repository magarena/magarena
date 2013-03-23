package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Hellrider {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (creature.getController() == player) ?
                new MagicEvent(
                        permanent,
                        player.getOpponent(),
                        this,
                        "SN deals 1 damage to PN.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getPermanent(),
                event.getPlayer(),
                1
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

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
                        player,
                        new Object[]{permanent,game.getOpponent(player)},
                        this,
                        permanent + " deals 1 damage to " + game.getOpponent(player) + ".") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    (MagicPermanent)data[0],
                    (MagicPlayer)data[1],
                    1,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Balefire_Dragon {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicPlayer player = permanent.getController();
            return (amount > 0 &&
                    damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player.getOpponent(),amount},
                        this,
                        permanent + " deals " + amount + 
                        " damage to each creature defending player controls."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> creatures=
                    game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicTarget creature : creatures) {
                    final MagicDamage damage = new MagicDamage(
                            (MagicPermanent)data[0],
                            creature,
                            (Integer)data[2],
                            false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            
        }        
    };
}

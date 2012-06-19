package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Enslave {
    public static final Object S = Control_Magic.S;
    
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent.getEnchantedCreature()},
                    this,
                    permanent.getEnchantedCreature() + " deals 1 damage to " +
                    permanent.getEnchantedCreature().getOwner() + ".") :
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent enchanted = (MagicPermanent)data[0];
            final MagicDamage damage = new MagicDamage(
                    enchanted,
                    enchanted.getOwner(),
                    1,
                    false);
            game.doAction(new MagicDealDamageAction(damage));            
        }
    };
}

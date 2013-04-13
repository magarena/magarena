package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Enslave {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            final MagicPlayer player = permanent.getController();
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            return (player == upkeepPlayer && enchantedCreature.isCreature()) ?
                new MagicEvent(
                    enchantedCreature,
                    enchantedCreature.getOwner(),
                    this,
                    enchantedCreature + " deals 1 damage to " +
                    enchantedCreature.getOwner() + ".") :
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                1
            );
            game.doAction(new MagicDealDamageAction(damage));            
        }
    };
}

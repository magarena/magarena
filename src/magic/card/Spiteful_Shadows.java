package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Spiteful_Shadows {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final int amount = damage.getDealtAmount();
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            return (damage.getTarget() == enchantedCreature) ?
                new MagicEvent(
                    enchantedCreature,
                    enchantedCreature.getController(),
                    new Object[]{amount},
                    this,
                    enchantedCreature + " deals " + amount +
                    " damage to " + enchantedCreature.getController() + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    event.getPlayer(),
                    (Integer)data[0],
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

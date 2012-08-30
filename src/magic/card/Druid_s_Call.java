package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Druid_s_Call {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            final MagicPlayer player = enchantedCreature.getController();
            return (damage.getTarget() == enchantedCreature) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{amount},
                    this,
                    player + " puts " + amount +
                    " 1/1 green Squirrel creature tokens onto the battlefield.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            for (int i=(Integer)data[0];i>0;i--) {
                game.doAction(new MagicPlayTokenAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Squirrel1")));
            }
        }
    };
}

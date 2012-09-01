package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Phyrexian_Obliterator {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicPlayer player = damage.getSource().getController();
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new Object[]{amount},
                            this,
                            player + " sacrifices " + amount + " permanents."):
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            int amount = (Integer)data[0];
            while (amount > 0 && player.getPermanents().size() > 0) {
                game.addEvent(new MagicSacrificePermanentEvent(
                        permanent,
                        player,
                        MagicTargetChoice.SACRIFICE_PERMANENT));
                amount--;
            }
        }
    };
}

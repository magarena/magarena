package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicCDA;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Sturmgeist {
    public static final MagicCDA CDA = new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getHandSize();
            pt.set(amount,amount);
        }
    };
    
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        player + " draws a card."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}

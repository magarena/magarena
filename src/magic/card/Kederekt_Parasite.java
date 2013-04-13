package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherDrawnTrigger;

public class Kederekt_Parasite {
    
    private static boolean controlsRedPermanent(final MagicPlayer player) {
        for (final MagicPermanent permanent : player.getPermanents()) {
            if (permanent.hasColor(MagicColor.Red)) {
                return true;
            }
        }
        return false;
    }

    public static final Object T = new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return (permanent.isEnemy(card) && 
                    controlsRedPermanent(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    card.getController(),
                    this,
                    "SN deals 1 damage to PN."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),1);
            game.doAction(new MagicDealDamageAction(damage));
        }        
    };
}

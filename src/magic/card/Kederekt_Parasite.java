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
    
    private static boolean isValid(final MagicPermanent owner) {
        for (final MagicPermanent permanent : owner.getController().getPermanents()) {
            if (permanent != owner && MagicColor.Red.hasColor(permanent.getColorFlags())) {
                return true;
            }
        }
        return false;
    }

    public static final Object T = new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
            final MagicPlayer player = data.getOwner();
            return (permanent.getController() != player && isValid(permanent)) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN deals 1 damage to PN."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),1,false);
            game.doAction(new MagicDealDamageAction(damage));
        }        
    };
}

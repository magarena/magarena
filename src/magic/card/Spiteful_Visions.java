package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenOtherDrawnTrigger;

public class Spiteful_Visions {
    public static final Object T1 = Rites_of_Flourishing.T3;
    
    public static final Object T2 = new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            final MagicPlayer player = card.getOwner();
            return new MagicEvent(
                permanent,
                player,
                this,
                "SN deals 1 damage to PN.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),1,false);
            game.doAction(new MagicDealDamageAction(damage));
        }        
    };
}

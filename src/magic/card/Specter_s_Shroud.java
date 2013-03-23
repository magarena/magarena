package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Specter_s_Shroud {
    public static final MagicWhenDamageIsDealtTrigger T =new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            return (damage.getSource() == equippedCreature && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        damage.getTarget(),
                        this,
                        damage.getTarget() + " discards a card.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getRefPlayer(),1,false));
        }
    };
}

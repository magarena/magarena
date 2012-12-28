package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Dimir_Cutpurse {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    damage.getTarget(),
                    this,
                    damage.getTarget() + " discards a card and PN draws a card"
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getRefPlayer(), 1, false));
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}

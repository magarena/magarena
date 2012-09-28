package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Snake_Umbra {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            return (enchanted == damage.getSource() && 
                    enchanted.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    enchanted,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE),
                    this,
                    "PN may$ draw a card.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(event.getPlayer(),1));
            }
        }
    };
}

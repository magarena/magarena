package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Molten_Rain {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_LAND,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target land$. " + 
                    "If that land was nonbasic, SN deals 2 damage to the land's controller.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                     game.doAction(new MagicDestroyAction(permanent));
                     if (!permanent.isBasic()) {
                         final MagicDamage damage = new MagicDamage(event.getSource(),permanent.getController(),2);
                         game.doAction(new MagicDealDamageAction(damage));
                     }
                }
            });
        }
    };
}

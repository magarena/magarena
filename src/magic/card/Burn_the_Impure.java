package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;

public class Burn_the_Impure {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(3),
                    this,
                    "SN deals 3 damage to target creature$. If that creature " +
                    "has infect, SN deals 3 damage to that creature's controller.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage1 = new MagicDamage(event.getSource(),creature,3,false);
                    game.doAction(new MagicDealDamageAction(damage1));
                    if (creature.hasAbility(MagicAbility.Infect)) {
                        final MagicDamage damage2 = new MagicDamage(event.getSource(),creature.getController(),3,false);
                        game.doAction(new MagicDealDamageAction(damage2));
                    }
                }
            });
        }
    };
}

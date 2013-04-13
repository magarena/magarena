package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Chandra_s_Outrage {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(2),
                    this,
                    "SN deals 4 damage to target creature$ and " + 
                             "2 damage to that creature's controller.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    MagicDamage damage = new MagicDamage(event.getSource(),target,4);
                    game.doAction(new MagicDealDamageAction(damage));
                    damage = new MagicDamage(event.getSource(),target.getController(),2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}

package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Burst_Lightning {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,MagicManaCost.FOUR,false),
                    new MagicDamageTargetPicker(2),
                    this,
                    "SN deals 2 damage to target creature or player$. "+
                    "If SN was kicked$, it deals 4 damage to that creature or player instead.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final int amount=((Integer)choiceResults[1])>0?4:2;
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,amount,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}

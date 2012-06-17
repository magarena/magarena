package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Rally_the_Forces {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack},
                    this,
                    "Attacking creatures get +1/+0 and gain first strike until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];            
            game.doAction(new MagicMoveCardAction(cardOnStack));
            final Collection<MagicTarget> targets=
                game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature=(MagicPermanent)target;
                game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
            }
        }
    };
}

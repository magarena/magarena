package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Paraselene {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new Object[]{cardOnStack},
                    this,
                    "Destroy all enchantments. " + cardOnStack.getController() +
                    " gains 1 life for each enchantment destroyed this way.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final MagicPlayer player = cardOnStack.getController();
            game.doAction(new MagicMoveCardAction(cardOnStack));
            final Collection<MagicTarget> targets =
                game.filterTargets(player,MagicTargetFilter.TARGET_ENCHANTMENT);
            game.doAction(new MagicDestroyAction(targets));
            if (targets.size() > 0) {
                game.doAction(new MagicChangeLifeAction(player,targets.size()));                
            }

        }
    };
}

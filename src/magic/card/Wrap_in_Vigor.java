package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Wrap_in_Vigor {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player=cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack},
                    this,
                    "Regenerate each creature you control.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));            
            final Collection<MagicTarget> targets = 
                game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                final MagicPermanent creature=(MagicPermanent)target;
                if (creature.canRegenerate()) {
                    game.doAction(new MagicRegenerateAction(creature));
                }
            }
        }
    };
}

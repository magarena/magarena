package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Echoing_Calm {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                    new MagicDestroyTargetPicker(false),
                    new Object[]{cardOnStack},
                    this,
                    "Destroy target enchantment$ and all other enchantments " +
                    "with the same name as that enchantment.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter targetFilter = 
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicTarget> targets = 
                        game.filterTargets(cardOnStack.getController(),targetFilter);
                    for (final MagicTarget target : targets) {
                    	final MagicPermanent permanent = (MagicPermanent)target;
                    	if (permanent.isEnchantment()) {
                    		game.doAction(new MagicDestroyAction(permanent));
                    	}
                    }
                }
			});
		}
	};
}

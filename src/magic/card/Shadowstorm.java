package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Shadowstorm {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    cardOnStack.getController(),
                    new Object[]{cardOnStack},
                    this,
                    card + " deals 2 damage to each creature with shadow.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source = cardOnStack.getCard();
			final Collection<MagicTarget> targets =
                game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITH_SHADOW);
			for (final MagicTarget target : targets) {
				final MagicDamage damage = new MagicDamage(source,target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
}

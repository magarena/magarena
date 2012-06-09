package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Bonfire_of_the_Damned {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
				final MagicCardOnStack cardOnStack,
				final MagicPayedCost payedCost) {
			final int amount = payedCost.getX();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    new Object[]{cardOnStack,amount},
                    this,
                    cardOnStack.getCard() + " deals " + amount +
                    " damage to target player$ and each creature he or she controls.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                	final int amount = (Integer)data[1];
					MagicDamage damage = new MagicDamage(
							cardOnStack.getCard(),
							player,
							amount,
							false);
                    game.doAction(new MagicDealDamageAction(damage));
					final Collection<MagicTarget> targets = game.filterTargets(
							player,
							MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
					for (final MagicTarget target : targets) {
						damage = new MagicDamage(
								cardOnStack.getCard(),
								target,
								amount,
								false);
						game.doAction(new MagicDealDamageAction(damage));
					}
                }
			});
		}
	};
}

package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Aggravate {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{cardOnStack,player},
                    this,
                    card + " deals 1 damage to each creature target player$ " +
                    "controls. Each creature dealt damage this way attacks " +
                    "this turn if able.");
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
                public void doAction(final MagicPlayer targetPlayer) {
                	final Collection<MagicTarget> targets = game.filterTargets(
                			targetPlayer,
                			MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                	for (final MagicTarget target : targets) {
						final MagicDamage damage = new MagicDamage(
								cardOnStack.getCard(),
								target,
								1,
								false);
						game.doAction(new MagicDealDamageAction(damage));
						if (damage.getDealtAmount() > 0) {
							game.doAction(new MagicSetAbilityAction(
									(MagicPermanent)target,
									MagicAbility.AttacksEachTurnIfAble));
						}
                	}
                }
			});
		}
	};
}

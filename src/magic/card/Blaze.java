package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicTargetAction;

public class Blaze {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    new Object[]{cardOnStack,amount},
                    this,
                    cardOnStack.getCard() + " deals "+amount+" damage to target creature or player$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,(Integer)data[1],false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
	};
}

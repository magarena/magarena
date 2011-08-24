package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Death_Grasp {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    new Object[]{cardOnStack,player,amount},
                    this,
                    card + " deals " + amount + " damage to target creature or player$. " +
                    		player + " gains "+amount+" life.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)data[2];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],amount));
		}
	};
}

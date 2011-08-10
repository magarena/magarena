package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Blightning {

	public static final MagicSpellCardEvent V5245 =new MagicSpellCardEvent("Blightning") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{cardOnStack},this,"Blightning deals 3 damage to target player$. That player discards two cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),player,3,false);
				game.doAction(new MagicDealDamageAction(damage));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,2,false));
			}
		}
	};

}

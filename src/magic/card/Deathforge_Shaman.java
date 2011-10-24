package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Deathforge_Shaman {  		
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_PLAYER,MagicManaCost.RED,true),
                    new Object[]{cardOnStack},
                    this,
                    "Play " + card + ". " + card + " deals damage to target " +
                    "player$ equal to twice the number of times it was kicked$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final int kickerCount = (Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			if (kickerCount > 0) {
				event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
	                public void doAction(final MagicPlayer player) {
	                    final MagicDamage damage = new MagicDamage(
	                    		cardOnStack.getCard(),
	                    		player,
	                    		kickerCount * 2,
	                    		false);
	                    game.doAction(new MagicDealDamageAction(damage));
	                }
				});
			}
		}
	};
}

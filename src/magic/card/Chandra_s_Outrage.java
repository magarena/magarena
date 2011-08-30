package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicTargetAction;

public class Chandra_s_Outrage {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(2),
                    new Object[]{cardOnStack,player},
                    this,
                    card + " deals 4 damage to target creature$ and 2 damage " +
                    		"to that creature's controller.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    MagicDamage damage = new MagicDamage(cardOnStack.getCard(),target,4,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    damage = new MagicDamage(cardOnStack.getCard(),target.getController(),2,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
	};
}

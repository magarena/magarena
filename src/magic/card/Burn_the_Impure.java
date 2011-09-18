package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;

public class Burn_the_Impure {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
					card,
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(3),
                    new Object[]{cardOnStack},
                    this,
                    card + " deals 3 damage to target creature$. If that creature " +
                    "has infect, " + card + " deals 3 damage to that creature's controller.");
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
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage1 = new MagicDamage(cardOnStack.getCard(),creature,3,false);
                    game.doAction(new MagicDealDamageAction(damage1));
                    if (creature.hasAbility(game,MagicAbility.Infect)) {
                    	final MagicDamage damage2 = new MagicDamage(cardOnStack.getCard(),creature.getController(),3,false);
                        game.doAction(new MagicDealDamageAction(damage2));
                    }
                }
			});
		}
	};
}

package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Burst_Lightning {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,MagicManaCost.FOUR,false),
                    new MagicDamageTargetPicker(2),
                    new Object[]{cardOnStack},
                    this,
                    "Burst Lightning deals 2 damage to target creature or player$. "+
                    "If Burst Lightning was kicked$, it deals 4 damage to that creature or player instead.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final int amount=((Integer)choiceResults[1])>0?4:2;
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
}

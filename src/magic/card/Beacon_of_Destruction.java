package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicShuffleIntoLibraryAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Beacon_of_Destruction {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(5),
                    new Object[]{card},
                    this,
                    card + " deals 5 damage to target creature or player$. " + 
                    "Shuffle " + card + " into its owner's library.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCard card=(MagicCard)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(card,target,5,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicShuffleIntoLibraryAction(card));
		}
	};
}

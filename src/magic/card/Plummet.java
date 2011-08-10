package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;

public class Plummet {

	public static final MagicSpellCardEvent V4306 =new MagicSpellCardEvent("Plummet") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,"Destroy target creature$ with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};

}

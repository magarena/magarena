package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Pongify {

	public static final MagicSpellCardEvent V4282 =new MagicSpellCardEvent("Pongify") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_CREATURE,
				new MagicDestroyTargetPicker(true),new Object[]{cardOnStack},this,
				"Destroy target creature$. It can't be regenerated. That creature's controller puts a 3/3 green Ape creature onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer controller=creature.getController();
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicPlayTokenAction(controller,TokenCardDefinitions.APE_TOKEN_CARD));
			}
		}
	};
	
}
